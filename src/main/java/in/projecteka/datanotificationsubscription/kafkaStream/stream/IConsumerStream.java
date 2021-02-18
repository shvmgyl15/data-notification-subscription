package in.projecteka.datanotificationsubscription.kafkaStream.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface IConsumerStream {
	// PS: this is not the topic name
	String INPUT = "input-binding";
	
	@Input(INPUT)
	SubscribableChannel process();
}
