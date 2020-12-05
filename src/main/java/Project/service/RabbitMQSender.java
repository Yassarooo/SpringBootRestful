package Project.service;


import Project.domain.Message;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${yassar.rabbitmq.exchange}")
    private String exchange;

    @Value("${yassar.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Message msg) {
        rabbitTemplate.convertAndSend(exchange, routingkey, msg);
        System.out.println("Send msg = " + msg);

    }
}