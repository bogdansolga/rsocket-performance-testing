package com.example.rsocket.responder.config;

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
import reactor.util.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class IntegerDecoder implements Decoder<Integer> {

    private static final int MAX_IN_MEMORY_SIZE = 256 * 1024;

    @Override
    public boolean canDecode(ResolvableType elementType, MimeType mimeType) {
        return elementType.toClass().equals(Integer.class);
    }

    @Override
    public Flux<Integer> decode(@NonNull Publisher<DataBuffer> inputStream, @NonNull ResolvableType elementType,
                                MimeType mimeType, Map<String, Object> hints) {
        return Flux.from(decodeToMono(inputStream, elementType, mimeType, hints));
    }

    @Override
    public Mono<Integer> decodeToMono(@NonNull Publisher<DataBuffer> inputStream, @NonNull ResolvableType elementType,
                                      MimeType mimeType, Map<String, Object> hints) {
        return DataBufferUtils.join(inputStream, MAX_IN_MEMORY_SIZE)
                              .map(dataBuffer -> Integer.parseInt(dataBuffer.toString(StandardCharsets.UTF_8)));
    }

    @Override
    public List<MimeType> getDecodableMimeTypes() {
        return Collections.singletonList(MediaType.APPLICATION_CBOR);
    }
}
