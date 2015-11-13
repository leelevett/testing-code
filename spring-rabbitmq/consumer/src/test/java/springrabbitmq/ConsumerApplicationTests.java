package springrabbitmq;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsumerTestConfig.class)
public class ConsumerApplicationTests {
  @Autowired
  private ConsumerApplicationTests.Producer producer;
  @Autowired
  private RabbitConsumer rabbitConsumer;

	@Test
	public void contextLoads() throws InterruptedException {
    // Producer needed to output stuff.
    Map<String, String> expectedMap =
        ImmutableMap.<String, String>builder()
            .put("first", "expected1")
            .put("second", "expected2")
            .build();
    producer.sendToRabbitMq(expectedMap);
    // Wait for message.
    TimeUnit.SECONDS.sleep(5); // Change in next post for aspectJ latch.
    // Verify data received.
    verify(rabbitConsumer, times(1)).output(expectedMap, "test");// "expected1", "expected2");
	}

  @ContextConfiguration(classes = ConsumerTestConfig.class)
  public static class Producer {
    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;
    @Autowired
    private Exchange exchange;
    @Autowired
    private Queue queue;

    public void sendToRabbitMq(final Map<String, String> message) {
      this.rabbitMessagingTemplate.convertAndSend(
          exchange.getName(), queue.getName(), message, ImmutableMap.<String, Object>builder().put("header", "test").build());
    }
  }
}