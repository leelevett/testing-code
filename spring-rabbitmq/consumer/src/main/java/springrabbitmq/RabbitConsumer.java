package springrabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitConsumer {
  @RabbitListener(queues = "#{queue}")
  public void output(@Payload() Map<String, String> mappy, @Header("header") String header) {//@Payload("#this['first']") String first, @Payload("#this['second']") String second) {
    System.out.println("First: " + mappy.get("first"));
    System.out.println("Second: " + mappy.get("second"));
  }
}
