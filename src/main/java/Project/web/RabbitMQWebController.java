package Project.web;


import Project.domain.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/")
public class RabbitMQWebController {

    @GetMapping(value = "/MQ")
    public void producer(@RequestParam("email") String email, @RequestParam("content") String content, @RequestParam("date") String date) {
        Message msg = new Message();
        msg.setEmail(email);
        msg.setContent(content);
        msg.setDate(date);
    }

}