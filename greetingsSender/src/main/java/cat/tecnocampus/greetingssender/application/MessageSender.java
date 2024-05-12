package cat.tecnocampus.greetingssender.application;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    private final AmqpTemplate amqpTemplate;
    private final String exchangeTopic;
    private final String routingVowel;
    private final String routingConsonant;

    public MessageSender(final AmqpTemplate amqpTemplate, @Value("${amqp.exchange.greetings.topic}") final String exchangeTopic,
                         @Value("${amqp.exchange.greetings.routing.vowels}") final String routingVowel,
                         @Value("${amqp.exchange.greetings.routing.consonants}") final String routingConsonant) {
        this.amqpTemplate = amqpTemplate;
        this.exchangeTopic = exchangeTopic;
        this.routingConsonant = routingConsonant;
        this.routingVowel = routingVowel;
    }

    public void sendGreetings(String message) {
        // regex to check if the message begins with a vowel
        if(message.toLowerCase().matches("^[aeiou].*"))
            amqpTemplate.convertAndSend(exchangeTopic, routingVowel, message);
        else {
            System.out.println("Sending consonant message: " + message);
            amqpTemplate.convertAndSend(exchangeTopic, routingConsonant, message);
        }
    }
}
