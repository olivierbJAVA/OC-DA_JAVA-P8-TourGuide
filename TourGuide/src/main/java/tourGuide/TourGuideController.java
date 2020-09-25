package tourGuide;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ch.qos.logback.core.CoreConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jsoniter.spi.JsoniterSpi;
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
import tourGuide.util.UserPreferencesSerializer;
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
    
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions") 
    public String getNearbyAttractions(@RequestParam String userName) throws JsonProcessingException {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(tourGuideService.getNearByAttractions(visitedLocation, getUser(userName)));
    	//return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation, getUser(userName)));
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    // TODO: Get a list of every user's most recent location as JSON
    //- Note: does not use gpsUtil to query for their current location,
    //        but rather gathers the user's current location from their stored location history.
    //
    // Return object should be the just a JSON mapping of userId to Locations similar to:
    //     {
    //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
    //        ...
    //     }
    @RequestMapping("/getAllCurrentLocations")
    public HashMap<String, Location> getAllCurrentLocations() throws JsonProcessingException {
        HashMap<String, Location> allCurrentLocations = tourGuideService.getAllCurrentLocations();
        //ObjectMapper mapper = new ObjectMapper();
        return allCurrentLocations;
        //return mapper.writeValueAsString(allCurrentLocations);
        //return JsonStream.serialize(allCurrentLocations);
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) throws JsonProcessingException {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(providers);
    	//return JsonStream.serialize(providers);
    }

    @RequestMapping("/getPreferences")
    public UserPreferences getPreferences(@RequestParam String userName) {
        UserPreferences userPreferences = tourGuideService.getUserPreferences(getUser(userName));
        return userPreferences;
        //return JsonStream.serialize(userPreferences);
    }

    @RequestMapping("/postPreferences")
    public UserPreferences postPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
        /*
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(UserPreferences.class, new UserPreferencesSerializer());
        mapper.registerModule(module);
        */
        return tourGuideService.postUserPreferences(getUser(userName), userPreferences);
    }

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}