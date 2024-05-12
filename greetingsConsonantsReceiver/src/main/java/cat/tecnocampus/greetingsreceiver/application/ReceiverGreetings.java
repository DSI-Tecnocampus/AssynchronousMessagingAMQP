package cat.tecnocampus.greetingsreceiver.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReceiverGreetings {
    private static Logger logger = LoggerFactory.getLogger(ReceiverGreetings.class);

    @RabbitListener(queues = "${amqp.queue}")
    public void receiveConsonantGreeting(String message) {
        logger.info("Salutation received: " + message);
    }

}
