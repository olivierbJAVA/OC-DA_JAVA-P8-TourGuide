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
import tourGuide.user.UserPreferences;
import tripPricer.Provider;

/**
 * Class including unit tests for the services of the TourGuide application.
 */
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
	public void getAllCurrentLocations() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		tourGuideService.tracker.stopTracking();

        User user1 = new User(UUID.fromString("123e4567-e89b-42d3-a456-556642440001"), "jon1", "001", "jon1@tourGuide.com");
        User user2 = new User(UUID.fromString("123e4567-e89b-42d3-a456-556642440002"), "jon2", "002", "jon2@tourGuide.com");
        User user3 = new User(UUID.fromString("123e4567-e89b-42d3-a456-556642440003"), "jon3", "003", "jon3@tourGuide.com");
		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);
		tourGuideService.addUser(user3);
        Location location1 = new Location(61.218887D, -149.877502D);
        Location location2 = new Location(62.218887D, -148.877502D);
        Location location3 = new Location(63.218887D, -147.877502D);
		user1.addToVisitedLocations(new VisitedLocation(user1.getUserId(), location1, new Date()));
		user2.addToVisitedLocations(new VisitedLocation(user2.getUserId(), location2, new Date()));
		user3.addToVisitedLocations(new VisitedLocation(user3.getUserId(), location3, new Date()));

		HashMap<String, Location> allCurrentLocationsExpected = new HashMap<>();
		allCurrentLocationsExpected.put("123e4567-e89b-42d3-a456-556642440001", location1);
		allCurrentLocationsExpected.put("123e4567-e89b-42d3-a456-556642440002", location2);
		allCurrentLocationsExpected.put("123e4567-e89b-42d3-a456-556642440003", location3);

      	// ACT
		HashMap<String, Location> allCurrentLocationsActual = tourGuideService.getAllCurrentLocations();

		// ASSERT
		assertEquals(allCurrentLocationsExpected, allCurrentLocationsActual);
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

		User user = new User(UUID.randomUUID(), "jon1", "000", "jon1@tourGuide.com");
		tourGuideService.addUser(user);

		// ACT
		User retrievedUser = tourGuideService.getUser(user.getUserName());

		// ASSERT
		assertEquals(user, retrievedUser);
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

		User user = new User(UUID.randomUUID(), "jon1", "000", "jon1@tourGuide.com");

		// ACT
		tourGuideService.addUser(user);

		// ASSERT
		User retrievedUser = tourGuideService.getUser(user.getUserName());
		assertEquals(user, retrievedUser);
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
		User user3 = new User(UUID.randomUUID(), "jon3", "000", "jon3@tourGuide.com");

		tourGuideService.addUser(user1);
		tourGuideService.addUser(user2);
		tourGuideService.addUser(user3);

		// ACT
		List<User> allUsers = tourGuideService.getAllUsers();

		// ASSERT
		assertEquals(3, allUsers.size());
		assertTrue(allUsers.contains(user1));
		assertTrue(allUsers.contains(user2));
		assertTrue(allUsers.contains(user3));
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

		// ASSERT
		assertEquals(5, nearbyAttractionsActual.size());
		for (int i=0; i<5; i++) {
			assertEquals(nearbyAttractionsExpected.get(i).getAttractionName(), nearbyAttractionsActual.get(i).getAttractionName());
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
		assertEquals(5, providers.size());// initial wrong = 10
	}

	@Test
	public void getUserPreferences() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		tourGuideService.tracker.stopTracking();

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		UserPreferences userPreferences = new UserPreferences();
		userPreferences.setTripDuration(5);
		userPreferences.setNumberOfAdults(2);
		userPreferences.setNumberOfChildren(1);
		user.setUserPreferences(userPreferences);

		// ACT
		UserPreferences userPreferencesRetrieved = tourGuideService.getUserPreferences(user);

		// ASSERT
		assertEquals(userPreferences, userPreferencesRetrieved);
	}

	@Test
	public void postUserPreferences() {
		// ARRANGE
		GpsUtil gpsUtil = new GpsUtil();
		RewardCentral rewardCentral = new RewardCentral();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		tourGuideService.tracker.stopTracking();

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		UserPreferences userPreferences = new UserPreferences();
		userPreferences.setTripDuration(5);
		userPreferences.setNumberOfAdults(2);
		userPreferences.setNumberOfChildren(1);

		// ACT
		tourGuideService.postUserPreferences(user, userPreferences);

		// ASSERT
		assertEquals(userPreferences, user.getUserPreferences());
	}
}
