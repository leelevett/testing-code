package springrabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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

  @Bean
  RabbitConsumer rabbitConsumer() {
    return new RabbitConsumer();
  }

  @Bean()
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    connectionFactory.setHost(host);
    connectionFactory.setPort(port);
    connectionFactory.setVirtualHost(virtualHost);
    // rabbit admin - create an exchange if not present?
    return connectionFactory;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory());
    // TODO This needs configuration
    factory.setConcurrentConsumers(1);
    factory.setMaxConcurrentConsumers(1);
    return factory;
  }


/*
 * We don't *need* to define an exchange, queue and binding unless we're producing messages.
 * However, these have been defined here so as to ensure automatic creation of the required parts
 * should they not already be present on the RabbitMQ server.
 */

  @Bean
  FanoutExchange exchange() {
    return new FanoutExchange(exchangeName());
  }
  @Bean
  Queue queue() {
    return new Queue(queueName());
  }
  @Bean
  Binding binding(Queue queue, FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
  }
}