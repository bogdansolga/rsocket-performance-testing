package com.example.rsocket.responder.controller;

import com.example.rsocket.responder.provider.BundleProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class RSocketController {

    private final BundleProvider bundleProvider;

    @Autowired
    public RSocketController(BundleProvider bundleProvider) {
        this.bundleProvider = bundleProvider;
    }

    @MessageMapping("bundle.read")
    public Mono<String> readBundle(Integer entries) {
        return Mono.just(bundleProvider.getBundle(entries));
    }

    @MessageMapping("bundle.read.raw")
    public Mono<Bundle> readRawBundle(Integer entries) {
        return Mono.just(bundleProvider.getRawBundle(entries));
    }

    @MessageMapping("bundle.write")
    public Mono<String> writeBundle(String bundle) {
        return Mono.just("OK");
    }
}
