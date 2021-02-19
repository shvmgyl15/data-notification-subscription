package in.projecteka.datanotificationsubscription.kafkaStream.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface IProducerStream {

    String OUTPUT = "producer-binding";

    @Output(OUTPUT)
    MessageChannel produce();
}
