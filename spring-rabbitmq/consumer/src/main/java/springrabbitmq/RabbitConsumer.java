package springrabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitConsumer {
  // SpEL not allowed in @Payload, boooooo... It is in integration.
  @RabbitListener(queues = "#{queue}")
  public void output(final Map<String, String> payload, @Header("header") String header) {
    System.out.println("First: " + payload.get("first"));
    System.out.println("Second: " + payload.get("second"));
  }
}
