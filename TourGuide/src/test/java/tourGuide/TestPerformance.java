package tourGuide;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import gpsUtil.location.Location;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.BeforeClass;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import utils.TourGuideTestUtil;

public class TestPerformance {

	@BeforeClass
	public static void beforeTest() {
		LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(Logger.ROOT_LOGGER_NAME, LogLevel.INFO);
	}
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	
	//@Ignore
	@Test
	public void highVolumeTrackLocation() {
		//Added to fix NumberFormatException due to decimal number separator
		Locale.setDefault(new Locale("en", "US"));

		// ARRANGE
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100000);

		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService mockRewardsService = Mockito.spy(new RewardsService(gpsUtil,rewardCentral ));
		doNothing().when(mockRewardsService).calculateRewards(any(User.class));
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, mockRewardsService);
		List<User> allUsers = tourGuideService.getAllUsers();
		tourGuideService.tracker.stopTracking();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// ACT
		ForkJoinPool forkJoinPool = new ForkJoinPool(100);
		allUsers.forEach((user)-> {
			CompletableFuture
					.runAsync(()->tourGuideService.trackUserLocation(user), forkJoinPool)
					.thenAccept(unused->mockRewardsService.calculateRewards(user));
		});
		boolean result = forkJoinPool.awaitQuiescence(15,TimeUnit.MINUTES);
		stopWatch.stop();
		forkJoinPool.shutdown();

		// ASSERT
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(result);
	}
	
	//@Ignore
	@Test
	public void highVolumeGetRewards() {
		//Added to fix NumberFormatException due to decimal number separator
		Locale.setDefault(new Locale("en", "US"));

		// ARRANGE
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100000);

		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		VisitedLocation visitedLocationRandom = new VisitedLocation(UUID.randomUUID(), new Location(TourGuideTestUtil.generateRandomLatitude(), TourGuideTestUtil.generateRandomLongitude()), TourGuideTestUtil.getRandomTime());
		TourGuideService mockTourGuideService = Mockito.spy(new TourGuideService(gpsUtil,rewardsService));
		doReturn(visitedLocationRandom).when(mockTourGuideService).trackUserLocation(any(User.class));
		List<User> allUsers = mockTourGuideService.getAllUsers();
		mockTourGuideService.tracker.stopTracking();
		Attraction attraction = gpsUtil.getAttractions().get(0);
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// ACT
		ForkJoinPool forkJoinPool = new ForkJoinPool(100);
		allUsers.forEach((user)-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
			CompletableFuture
					.runAsync(()->mockTourGuideService.trackUserLocation(user), forkJoinPool)
					.thenAccept(unused->rewardsService.calculateRewards(user));
		});
		boolean result = forkJoinPool.awaitQuiescence(20,TimeUnit.MINUTES);
		forkJoinPool.shutdown();
		stopWatch.stop();

		// ASSERT
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(result);
	}

	// New test added : highVolumeTrackLocationAndGetRewards in actual conditions
	//@Ignore
	@Test
	public void highVolumeTrackLocationAndGetRewards() {
		//Added to fix NumberFormatException due to decimal number separator
		Locale.setDefault(new Locale("en", "US"));

		// ARRANGE
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(100);

		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		List<User> allUsers = tourGuideService.getAllUsers();
		tourGuideService.tracker.stopTracking();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// ACT
		ForkJoinPool forkJoinPool = new ForkJoinPool(100);
		allUsers.forEach((user)-> {
			CompletableFuture
					.runAsync(()->tourGuideService.trackUserLocation(user), forkJoinPool)
					.thenAccept(unused->rewardsService.calculateRewards(user));
		});
		boolean result = forkJoinPool.awaitQuiescence(15,TimeUnit.MINUTES);
		forkJoinPool.shutdown();
		stopWatch.stop();

		// ASSERT
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		assertTrue(result);
	}

}
