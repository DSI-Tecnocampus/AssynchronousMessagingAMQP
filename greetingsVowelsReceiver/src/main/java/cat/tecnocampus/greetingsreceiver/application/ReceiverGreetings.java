package cat.tecnocampus.greetingsreceiver.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ReceiverGreetings {
    private static Logger logger = LoggerFactory.getLogger(ReceiverGreetings.class);

    @Bean
    public Consumer<String> receiveGreeting() {
        return payload -> logger.info("Salutation received: " + payload);
    }

    @Bean
    public Consumer<String> receiveTime() {
        return payload -> logger.info("Date received: " + payload);
    }

}
