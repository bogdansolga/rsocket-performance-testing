package com.example.rsocket.responder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;

@Configuration
public class RSocketConfig {

    @Bean
    public RSocketStrategies rSocketStrategies(BundleEncoder bundleEncoder, IntegerDecoder integerDecoder) {
        return RSocketStrategies.builder()
                                .decoder(integerDecoder)
                                .encoder(bundleEncoder)
                                .build();
    }
}
