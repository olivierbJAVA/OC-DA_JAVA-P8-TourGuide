package tourGuide.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.javamoney.moneta.Money;
import tourGuide.user.UserPreferences;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;

public class UserPreferencesSerializer extends StdSerializer<UserPreferences> {

    public UserPreferencesSerializer() {
        this(null);
    }

    public UserPreferencesSerializer(Class<UserPreferences> t) {
        super(t);
    }

    @Override
    public void serialize(
            UserPreferences userPreferences, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
/*
        private int attractionProximity = Integer.MAX_VALUE;
        private CurrencyUnit currency = Monetary.getCurrency("USD");
        private Money lowerPricePoint = Money.of(0, currency);
        private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);
        private int tripDuration = 1;
        private int ticketQuantity = 1;
        private int numberOfAdults = 1;
        private int numberOfChildren = 0;
*/

        jgen.writeNumberField("attractionProximity", userPreferences.getAttractionProximity());
        jgen.writeNumberField("tripDuration", userPreferences.getTripDuration());
        jgen.writeNumberField("ticketQuantity", userPreferences.getTicketQuantity());
        jgen.writeNumberField("numberOfAdults", userPreferences.getNumberOfAdults());
        jgen.writeNumberField("numberOfChildren", userPreferences.getNumberOfChildren());

        jgen.writeObjectField("currency", userPreferences.getCurrency());
        jgen.writeObjectField("lowerPricePoint", userPreferences.getLowerPricePoint());
        jgen.writeObjectField("highPricePoint", userPreferences.getHighPricePoint());
/*
        jgen.writeObject(userPreferences.getCurrency());
        jgen.writeObject(userPreferences.getLowerPricePoint());
        jgen.writeObject(userPreferences.getHighPricePoint());
*/
        jgen.writeEndObject();
    }
}
