package springrabbitmq;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.spy;

@Configuration
public class ConsumerTestConfig extends ConsumerConfig {
  @Bean
  String queueName() {
    return "test-queue";
  }
  @Bean
  String exchangeName() {
    return "test-exchange";
  }

  @Bean
  RabbitConsumer rabbitConsumer() {
    return spy(super.rabbitConsumer());
  }

  /**
   * Gets a rabbitMessagingTemplate; Used to send the messages to the Rabbit queue.
   */
  @Bean
  public RabbitMessagingTemplate rabbitMessagingTemplate() {
    return new RabbitMessagingTemplate(new RabbitTemplate(connectionFactory()));
  }

  /**
   * Gets a Producer; Produces the test messages which get placed on the queue for processing.
   */
  @Bean
  ConsumerApplicationTests.Producer producer() {
    return new ConsumerApplicationTests.Producer();
  }
}