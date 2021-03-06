package in.projecteka.datanotificationsubscription.common;

import in.projecteka.datanotificationsubscription.GatewayServiceProperties;
import in.projecteka.datanotificationsubscription.common.cache.CacheAdapter;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ServiceAuthentication {
    private final ServiceAuthenticationClient client;
    private final GatewayServiceProperties properties;
    private final CacheAdapter<String, String> accessTokenCache;

    public Mono<String> authenticate() {
        return accessTokenCache.getIfPresent("consentManager:gateway:accessToken")
                .switchIfEmpty(Mono.defer(this::tokenUsingSecret))
                .map(token -> String.format("%s %s", "Bearer", token));
    }

    private Mono<String> tokenUsingSecret() {
        return client.getTokenFor(properties.getClientId(), properties.getClientSecret())
                .flatMap(session ->
                        accessTokenCache.put("consentManager:gateway:accessToken", session.getAccessToken())
                                .thenReturn(session.getAccessToken()));
    }
}
