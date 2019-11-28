package com.example.rsocket.requester.config;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Encoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class IntegerEncoder implements Encoder<Integer> {

    @Override
    public boolean canEncode(ResolvableType elementType, MimeType mimeType) {
        return elementType.toClass().equals(Integer.class);
    }

    @Override
    public Flux<DataBuffer> encode(Publisher<? extends Integer> inputStream, DataBufferFactory bufferFactory,
                                   ResolvableType elementType, MimeType mimeType, Map<String, Object> hints) {
        return Flux.from(inputStream)
                   .map(value -> bufferFactory.wrap(value.toString().getBytes()));
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        return Collections.singletonList(MediaType.APPLICATION_CBOR);
    }

    @Override
    public DataBuffer encodeValue(Integer value, DataBufferFactory bufferFactory, ResolvableType valueType,
                                  MimeType mimeType, Map<String, Object> hints) {
        return bufferFactory.wrap(value.toString().getBytes());
    }
}
