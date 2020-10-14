package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.stream.Collectors;

import gpsUtil.location.Location;
import org.junit.Ignore;
import org.junit.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.attraction.NearbyAttraction;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

public class TestTourGuideService {

	@Test
	public void getUserLocation() {
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

		// ACT
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);

		// ASSERT
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

	@Test
	public void getUser() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		tourGuideService.tracker.stopTracking();

		User user1 = new User(UUID.randomUUID(), "jon1", "000", "jon1@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);

		// ACT
		User retrievedUser1 = tourGuideService.getUser(user1.getUserName());
		User retrievedUser2 = tourGuideService.getUser(user2.getUserName());

		// ASSERT
		assertEquals(user1, retrievedUser1);
		assertEquals(user2, retrievedUser2);
	}

	@Test
	public void addUser() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		tourGuideService.tracker.stopTracking();

		User user1 = new User(UUID.randomUUID(), "jon1", "000", "jon1@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		// ACT
		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);

		// ASSERT
		User retrievedUser1 = tourGuideService.getUser(user1.getUserName());
		User retrievedUser2 = tourGuideService.getUser(user2.getUserName());

		assertEquals(user1, retrievedUser1);
		assertEquals(user2, retrievedUser2);
	}

	@Test
	public void getAllUsers() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		tourGuideService.tracker.stopTracking();

		User user1 = new User(UUID.randomUUID(), "jon1", "000", "jon1@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);

		// ACT
		List<User> allUsers = tourGuideService.getAllUsers();

		// ASSERT
		assertEquals(2, allUsers.size());
		assertTrue(allUsers.contains(user1));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUserLocation() {
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
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearbyAttractions() {
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

		VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(47.305969D, 71.710449D), new Date());

		List<NearbyAttraction> nearbyAttractionsExpected = new ArrayList<>();

		NearbyAttraction nearbyAttraction1 = new NearbyAttraction("McKinley Tower", new Location(61.218887D, -149.877502D), new Location(47.305969D, 71.710449D), 4586.179236787266D, rewardsService.getRewardPoints(new Attraction("McKinley Tower", "Anchorage", "AK", 61.218887D, -149.877502D), user));
		NearbyAttraction nearbyAttraction2 = new NearbyAttraction("Franklin Park Zoo", new Location(42.302601D, -71.086731D), new Location(47.305969D, 71.710449D), 5836.916287407119D, rewardsService.getRewardPoints(new Attraction("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D), user));
		NearbyAttraction nearbyAttraction3 = new NearbyAttraction("Bronx Zoo", new Location(40.852905D, -73.872971D), new Location(47.305969D, 71.710449D), 5985.996919050997D, rewardsService.getRewardPoints(new Attraction("Bronx Zoo", "Bronx", "NY", 40.852905D, -73.872971D), user));
		NearbyAttraction nearbyAttraction4 = new NearbyAttraction("Flatiron Building", new Location(40.741112D, -73.989723D), new Location(47.305969D, 71.710449D), 5995.465468529265, rewardsService.getRewardPoints(new Attraction("Flatiron Building", "New York City", "NY", 40.741112D, -73.989723D), user));
		NearbyAttraction nearbyAttraction5 = new NearbyAttraction("Jackson Hole", new Location(43.582767D, -110.821999D), new Location(47.305969D, 71.710449D), 6150.946648899067D, rewardsService.getRewardPoints(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D), user));
		nearbyAttractionsExpected.add(nearbyAttraction1);
		nearbyAttractionsExpected.add(nearbyAttraction2);
		nearbyAttractionsExpected.add(nearbyAttraction3);
		nearbyAttractionsExpected.add(nearbyAttraction4);
		nearbyAttractionsExpected.add(nearbyAttraction5);

		// ACT
		List<NearbyAttraction> nearbyAttractionsActual = tourGuideService.getNearByAttractions(visitedLocation, user);

		//tourGuideService.tracker.stopTracking();

		// ASSERT
		assertEquals(5, nearbyAttractionsActual.size());

		for (int j=0; j<5; j++) {
			assertEquals(nearbyAttractionsExpected.get(j).getAttractionName(), nearbyAttractionsActual.get(j).getAttractionName());
		}
	}

	@Test
	public void getTripDeals() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		tourGuideService.tracker.stopTracking();

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		// ACT
		List<Provider> providers = tourGuideService.getTripDeals(user);

		// ASSERT
		assertEquals(5, providers.size());
	}

}
