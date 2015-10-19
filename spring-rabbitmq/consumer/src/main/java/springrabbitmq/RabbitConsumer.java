package springrabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {
  @RabbitListener(queues = "#{queue}")
  public void output(@Payload("#this['first']") String first, @Payload("#this.['second']") String second) {
    System.out.println("First: " + first);
    System.out.println("Second: " + second);
  }
}
