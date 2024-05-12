package cat.tecnocampus.timesender.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

@Configuration
public class TimeSender {
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Supplier<String> sendTime() {
        return () -> LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

}
