package tourGuide;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.util.Locale;
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
//@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        //Added to fix NumberFormatException due to decimal number separator
        Locale.setDefault(new Locale("en", "US"));

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new MoneyModule().withDefaultFormatting());

        SpringApplication.run(Application.class, args);
    }

}
