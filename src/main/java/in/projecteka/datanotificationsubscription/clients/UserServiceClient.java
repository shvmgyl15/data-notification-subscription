package in.projecteka.datanotificationsubscription.clients;

import in.projecteka.datanotificationsubscription.clients.model.PatientLinksResponse;
import in.projecteka.datanotificationsubscription.clients.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Properties;
import java.util.function.Supplier;

import static in.projecteka.datanotificationsubscription.common.ClientError.userNotFound;
import static in.projecteka.datanotificationsubscription.common.Constants.CORRELATION_ID;
import static reactor.core.publisher.Mono.error;

@AllArgsConstructor
public class UserServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
    private static final String INTERNAL_PATH_USER_IDENTIFICATION = "%s/internal/users/%s/";
    private static final String INTERNAL_PATH_USER_LINKS = "%s/internal/patients/%s/links";
    private final WebClient webClient;
    private final String url;
    private final Supplier<Mono<String>> tokenGenerator;
    private final String authorizationHeader;

    public Mono<User> userOf(String userId) {
        return tokenGenerator.get()
                .flatMap(token -> webClient
                        .get()
                        .uri(String.format(INTERNAL_PATH_USER_IDENTIFICATION, url, userId))
                        .header(authorizationHeader, token)
                        .header(CORRELATION_ID, MDC.get(CORRELATION_ID))
                        .retrieve()
                        .onStatus(httpStatus -> httpStatus.value() == 404,
                                clientResponse -> clientResponse.bodyToMono(String.class)
                                        .doOnNext(logger::error)
                                        .then(error(userNotFound())))
                        .bodyToMono(User.class))
                .doOnSubscribe(subscription -> logger.info("Call internal user service for user-id: {}", userId));
    }
}
