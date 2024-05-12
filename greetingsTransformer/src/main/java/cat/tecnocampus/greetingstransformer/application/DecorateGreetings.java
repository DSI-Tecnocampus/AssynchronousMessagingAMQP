package cat.tecnocampus.greetingstransformer.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class DecorateGreetings {

    @Bean
    public Function<String, String> decorateGreeting() {
        return message -> "****" + message + "****";
    }

}
