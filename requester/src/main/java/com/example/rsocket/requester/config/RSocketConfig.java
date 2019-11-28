package com.example.rsocket.requester.config;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Mono;

@Configuration
public class RSocketConfig {

    @Value("${responder.host}")
    private String responderHost;

    @Value("${responder.port}")
    private int responderPort;

    @Bean
    public Mono<RSocketRequester> requester(BundleDecoder bundleDecoder, IntegerEncoder integerEncoder) {
        final RSocketStrategies.Builder builder = RSocketStrategies.builder()
                                                                   .decoder(bundleDecoder)
                                                                   .encoder(integerEncoder);

        return RSocketRequester.builder()
                               .rsocketFactory(factory -> factory.dataMimeType(MediaType.APPLICATION_CBOR_VALUE)
                                                                 .frameDecoder(PayloadDecoder.ZERO_COPY))
                               .rsocketStrategies(builder.build())
                               .connectTcp(responderHost, responderPort)
                               .retry()
                               .cache();
    }
}
