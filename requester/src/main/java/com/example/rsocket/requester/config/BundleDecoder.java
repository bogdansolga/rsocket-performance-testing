package com.example.rsocket.requester.config;

import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Decoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BundleDecoder implements Decoder<Bundle> {

    private static final int MAX_IN_MEMORY_SIZE = 256 * 1024;

    @Override
    public boolean canDecode(ResolvableType resolvableType, MimeType mimeType) {
        return resolvableType.toClass().equals(Bundle.class);
    }

    @Override
    public Flux<Bundle> decode(Publisher<DataBuffer> publisher, ResolvableType resolvableType, MimeType mimeType,
                               Map<String, Object> map) {
        return Flux.from(decodeToMono(publisher, resolvableType, mimeType, map));
    }

    @Override
    public Mono<Bundle> decodeToMono(Publisher<DataBuffer> publisher, ResolvableType resolvableType, MimeType mimeType,
                                     Map<String, Object> map) {
        return DataBufferUtils.join(publisher, MAX_IN_MEMORY_SIZE)
                              .map(dataBuffer -> SerializationUtils.deserialize(dataBuffer.asInputStream()));
    }

    @Override
    public List<MimeType> getDecodableMimeTypes() {
        return Collections.singletonList(MediaType.APPLICATION_CBOR);
    }
}
