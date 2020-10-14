package tourGuide;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gpsUtil.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }
    
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) throws JsonProcessingException {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(tourGuideService.getNearByAttractions(visitedLocation, getUser(userName)));
   }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getAllCurrentLocations")
    public HashMap<String, Location> getAllCurrentLocations() throws JsonProcessingException {
        HashMap<String, Location> allCurrentLocations = tourGuideService.getAllCurrentLocations();
        return allCurrentLocations;
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) throws JsonProcessingException {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(providers);
    }

    @RequestMapping("/getPreferences")
    public UserPreferences getPreferences(@RequestParam String userName) {
        UserPreferences userPreferences = tourGuideService.getUserPreferences(getUser(userName));
        return userPreferences;
    }

    @RequestMapping("/postPreferences")
    public UserPreferences postPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
       return tourGuideService.postUserPreferences(getUser(userName), userPreferences);
    }

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
}