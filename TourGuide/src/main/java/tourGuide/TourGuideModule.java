package tourGuide;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import org.zalando.jackson.datatype.money.CurrencyUnitDeserializer;
import org.zalando.jackson.datatype.money.CurrencyUnitSerializer;
import org.zalando.jackson.datatype.money.MoneyModule;
import rewardCentral.RewardCentral;
import tourGuide.service.RewardsService;
import tourGuide.util.UserPreferencesSerializer;

@Configuration
public class TourGuideModule {
	
	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
/*
	@Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
		return new ObjectMapper().registerModule(new MoneyModule().withDefaultFormatting());
	}
*/
/*
    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new MonetaryAmountSerializer();
    }
*/

	@Bean
	public UserPreferencesSerializer userPreferencesSerializer() {
		return new UserPreferencesSerializer();
	}
}
