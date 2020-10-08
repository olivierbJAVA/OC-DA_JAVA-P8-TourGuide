package tourGuide;

import java.util.HashMap;
import java.util.List;

import gpsUtil.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import gpsUtil.location.VisitedLocation;
import tourGuide.attraction.NearbyAttraction;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	
    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @GetMapping("/getLocation")
    public Location getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return visitedLocation.location;
    }
    
    @GetMapping("/getNearbyAttractions")
    public List<NearbyAttraction> getNearbyAttractions(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return tourGuideService.getNearByAttractions(visitedLocation, getUser(userName));
    }
    
    @GetMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) {
    	return tourGuideService.getUserRewards(getUser(userName));
    }

    @GetMapping("/getAllCurrentLocations")
    public HashMap<String, Location> getAllCurrentLocations() {
        HashMap<String, Location> allCurrentLocations = tourGuideService.getAllCurrentLocations();
        return allCurrentLocations;
    }
    
    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return providers;
    }

    @GetMapping("/getPreferences")
    public UserPreferences getPreferences(@RequestParam String userName) {
        UserPreferences userPreferences = tourGuideService.getUserPreferences(getUser(userName));
        return userPreferences;
    }

    @GetMapping("/postPreferences")
    public UserPreferences postPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
        return tourGuideService.postUserPreferences(getUser(userName), userPreferences);
    }

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   
}