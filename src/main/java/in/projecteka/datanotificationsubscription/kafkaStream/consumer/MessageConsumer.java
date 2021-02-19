package in.projecteka.datanotificationsubscription.kafkaStream.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.projecteka.consentmanager.common.TraceableMessage;
import in.projecteka.datanotificationsubscription.HIUSubscriptionManager;
import in.projecteka.datanotificationsubscription.hipLink.NewCCLinkEvent;
import in.projecteka.datanotificationsubscription.kafkaStream.stream.IConsumerStream;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static in.projecteka.datanotificationsubscription.common.Constants.CORRELATION_ID;

@Service
@AllArgsConstructor
public class MessageConsumer {
	private final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

	private final HIUSubscriptionManager subscriptionManager;

	@StreamListener(IConsumerStream.INPUT)
	public void process(String message) {
		try {

			log.info("Message from queue: {} ", message);
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			TraceableMessage traceableMessage = mapper.readValue(message, TraceableMessage.class);
			NewCCLinkEvent hipLinkEvent = mapper.convertValue(traceableMessage.getMessage(), NewCCLinkEvent.class);
			MDC.put(CORRELATION_ID, traceableMessage.getCorrelationId());
			log.debug("Received Link Event message {}", hipLinkEvent);

			subscriptionManager.notifySubscribers(hipLinkEvent)
					.subscriberContext(ctx -> {
						Optional<String> correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID));
						return correlationId.map(id -> ctx.put(CORRELATION_ID, id))
								.orElseGet(() -> ctx.put(CORRELATION_ID, UUID.randomUUID().toString()));
					})
					.subscribe();

		} catch (Exception e) {
            log.debug("Kafka consumer: error in processing message");
            e.printStackTrace();
        }
	}

}
