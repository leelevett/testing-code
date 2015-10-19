package springrabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableRabbit
// Hard code properties in here for first version?
// If so; we can do a blog on PropertySource/ConfigurationProperties & Props/YAML.
// Maybe do the overriding properties file bit using custom property bean thing too?
// Look at: @TestPropertySource("/test.properties")
//@PropertySource("classpath:consumer.properties")
public class ConsumerConfig {
  //@Value("${spring.rabbitmq.username}")
  private String username;
  //@Value("${spring.rabbitmq.password}")
  private String password;
  //@Value("${spring.rabbitmq.host}")
  private String host;
  //@Value("${spring.rabbitmq.port}")
  private int port;
  //@Value("${spring.rabbitmq.virtual-host}")
  private String virtualHost;
  //@Value("${spring.rabbitmq.queue.name}")
  String queueName;
  //@Value("${spring.rabbitmq.exchange.name}")
  String exchangeName;

  // Makes beans for queue name and exchange name. For overriding with test versions.
  @Bean
  String queueName() {
    return queueName;
  }
  @Bean
  String exchangeName() {
    return exchangeName;
  }
}