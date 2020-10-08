package tourGuide;

import static org.junit.Assert.*;

import java.util.*;

import gpsUtil.location.Location;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import utils.TourGuideTestUtil;

public class TestRewardsService {

	@Test
	public void calculateRewards() {
		//Added to fix NumberFormatException due to decimal number separator
		Locale.setDefault(new Locale("en", "US"));

		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		tourGuideService.tracker.stopTracking();

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

		// ACT
		rewardsService.calculateRewards(user);

		// ASSERT
		List<UserReward> userRewards = user.getUserRewards();
		assertEquals(1, userRewards.size());
	}
	
	@Test
	public void isWithinAttractionProximity() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		Attraction attraction = gpsUtil.getAttractions().get(0);

		// ACT & ASSERT
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAttraction() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		Attraction attraction = gpsUtil.getAttractions().get(0);

		VisitedLocation visitedLocationRandom = new VisitedLocation(UUID.randomUUID(), new Location(TourGuideTestUtil.generateRandomLatitude(), TourGuideTestUtil.generateRandomLongitude()), TourGuideTestUtil.getRandomTime());

		// ACT & ASSERT
		assertTrue(rewardsService.nearAttraction(visitedLocationRandom, attraction));
	}

	@Test
	public void nearAllAttractions() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		tourGuideService.tracker.stopTracking();

		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}

}
