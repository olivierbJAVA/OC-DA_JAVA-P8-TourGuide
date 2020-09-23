package tourGuide.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import tourGuide.user.UserPreferences;

import java.io.IOException;

public class UserPreferencesDeserializer extends StdDeserializer<UserPreferences> {

    public UserPreferencesDeserializer() {
        this(null);
    }

    @Override
    public UserPreferences deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }

    public UserPreferencesDeserializer(Class<UserPreferences> t) {
        super(t);
    }
/*
    @Override
    public void deserialize(
            UserPreferences userPreferences, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();

        jgen.writeNumberField("attractionProximity", userPreferences.getAttractionProximity());
        jgen.writeNumberField("tripDuration", userPreferences.getTripDuration());
        jgen.writeNumberField("ticketQuantity", userPreferences.getTicketQuantity());
        jgen.writeNumberField("numberOfAdults", userPreferences.getNumberOfAdults());
        jgen.writeNumberField("numberOfChildren", userPreferences.getNumberOfChildren());

        jgen.writeObjectField("currency", userPreferences.getCurrency());
        jgen.writeObjectField("lowerPricePoint", userPreferences.getLowerPricePoint());
        jgen.writeObjectField("highPricePoint", userPreferences.getHighPricePoint());

        //jgen.writeObject(userPreferences.getCurrency());
        //jgen.writeObject(userPreferences.getLowerPricePoint());
        //jgen.writeObject(userPreferences.getHighPricePoint());

        jgen.writeEndObject();
    }

 */
}
