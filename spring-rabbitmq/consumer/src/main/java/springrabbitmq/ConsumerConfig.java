package springrabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableRabbit
@PropertySource("classpath:*.properties")
public class ConsumerConfig {
  // Needs to read in properties
}