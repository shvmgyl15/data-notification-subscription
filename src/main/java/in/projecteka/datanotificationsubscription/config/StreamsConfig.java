package in.projecteka.datanotificationsubscription.config;

import in.projecteka.datanotificationsubscription.kafkaStream.stream.IConsumerStream;
import in.projecteka.datanotificationsubscription.kafkaStream.stream.IProducerStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(value = {Source.class, IProducerStream.class, IConsumerStream.class})
public class StreamsConfig {
}

