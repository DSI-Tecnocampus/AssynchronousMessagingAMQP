package cat.tecnocampus.greetingssender.restApi;

import cat.tecnocampus.greetingssender.application.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private MessageSender messageSender;

    @Autowired
    public RestController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @RequestMapping("/greetings/{message}")
    ResponseEntity<String> messageSend(@PathVariable String message) {
        messageSender.sendGreetings(message);
        return ResponseEntity.ok(message);
    }

}
