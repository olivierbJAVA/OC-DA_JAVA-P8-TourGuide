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
		//VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		//tourGuideService.tracker.stopTracking();

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
		//assertTrue(allCurrentLocationsExpected.equals(allCurrentLocationsActual));
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

		//tourGuideService.tracker.stopTracking();

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

		//tourGuideService.tracker.stopTracking();
		
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

		//tourGuideService.tracker.stopTracking();

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
		
		//tourGuideService.tracker.stopTracking();
		
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
			assertEquals(nearbyAttractionsExpected.get(j).getAttractionLocation().latitude, nearbyAttractionsActual.get(j).getAttractionLocation().latitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getAttractionLocation().longitude, nearbyAttractionsActual.get(j).getAttractionLocation().longitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getUserLocation().latitude, nearbyAttractionsActual.get(j).getUserLocation().latitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getUserLocation().longitude, nearbyAttractionsActual.get(j).getUserLocation().longitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getDistanceAttractionUserLocation(), nearbyAttractionsActual.get(j).getDistanceAttractionUserLocation(), 0);
		}
	}

	@Test
	public void getNearbyAttractionsOtherVersion() {
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

		List<Attraction> allAttractions = gpsUtil.getAttractions();

		TreeMap<Double, NearbyAttraction> treeAttractionDistance = new TreeMap<>();
		allAttractions.forEach(attraction -> treeAttractionDistance.put(rewardsService.getDistance(attraction, visitedLocation.location), new NearbyAttraction( attraction.attractionName, new Location(attraction.latitude, attraction.longitude), visitedLocation.location, rewardsService.getDistance(attraction, visitedLocation.location), rewardsService.getRewardPoints(attraction, user))));

		System.out.println("User location : lat = " + visitedLocation.location.latitude + " - long = " + visitedLocation.location.longitude);

		// Print for help
		for (Map.Entry<Double, NearbyAttraction> entry : treeAttractionDistance.entrySet()) {
			System.out.println("AllAttractions - Key: " + entry.getKey() + ". Value: " + entry.getValue().getAttractionName());
		}

		List<NearbyAttraction> nearbyAttractionsExpected = treeAttractionDistance.values().stream()
				.limit(5)
				.collect(Collectors.toList());

		/*
		TreeMap<Double, NearbyAttraction> treeAttractionDistanceLimitedToFive = treeAttractionDistance.entrySet().stream()
				.limit(5)
				.collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);

		// Print for help
		for (Map.Entry<Double, NearbyAttraction> entry : treeAttractionDistanceLimitedToFive.entrySet()) {
			System.out.println("LimitedTo5 - Key: " + entry.getKey() + ". Value: " + entry.getValue().getAttractionName());
		}
		*/
		// ACT
		List<NearbyAttraction> nearbyAttractionsActual = tourGuideService.getNearByAttractions(visitedLocation, user);

		// Print for help
		nearbyAttractionsActual.forEach( (nearbyAttraction)->System.out.println(nearbyAttraction.getAttractionName()) );

		//tourGuideService.tracker.stopTracking();

		// ASSERT
		assertEquals(5, nearbyAttractionsActual.size());

		for (int j=0; j<5; j++) {
			assertEquals(nearbyAttractionsExpected.get(j).getAttractionName(), nearbyAttractionsActual.get(j).getAttractionName());
			assertEquals(nearbyAttractionsExpected.get(j).getAttractionLocation().latitude, nearbyAttractionsActual.get(j).getAttractionLocation().latitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getAttractionLocation().longitude, nearbyAttractionsActual.get(j).getAttractionLocation().longitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getUserLocation().latitude, nearbyAttractionsActual.get(j).getUserLocation().latitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getUserLocation().longitude, nearbyAttractionsActual.get(j).getUserLocation().longitude, 0);
			assertEquals(nearbyAttractionsExpected.get(j).getDistanceAttractionUserLocation(), nearbyAttractionsActual.get(j).getDistanceAttractionUserLocation(), 0);
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
		
		//tourGuideService.tracker.stopTracking();

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
		userPreferences.setAttractionProximity(1000);
		userPreferences.setCurrency("USD");
		userPreferences.setLowerPricePoint(0D);
		userPreferences.setHighPricePoint(1000000D);
		userPreferences.setTripDuration(5);
		userPreferences.setTicketQuantity(3);
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
		userPreferences.setAttractionProximity(1000);
		userPreferences.setCurrency("USD");
		userPreferences.setLowerPricePoint(0D);
		userPreferences.setHighPricePoint(1000000D);
		userPreferences.setTripDuration(5);
		userPreferences.setTicketQuantity(3);
		userPreferences.setNumberOfAdults(2);
		userPreferences.setNumberOfChildren(1);

		// ACT
		tourGuideService.postUserPreferences(user, userPreferences);

		// ASSERT
		assertEquals(userPreferences, user.getUserPreferences());
	}
}
