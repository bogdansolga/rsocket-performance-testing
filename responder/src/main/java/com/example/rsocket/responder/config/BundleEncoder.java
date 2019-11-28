package com.example.rsocket.responder.config;

import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Encoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BundleEncoder implements Encoder<Bundle> {

    @Override
    public boolean canEncode(ResolvableType elementType, MimeType mimeType) {
        return elementType.toClass().equals(Bundle.class);
    }

    @Override
    public Flux<DataBuffer> encode(Publisher<? extends Bundle> inputStream, DataBufferFactory bufferFactory,
                                   ResolvableType elementType, MimeType mimeType, Map<String, Object> hints) {
        return Flux.from(inputStream)
                   .map(bundle -> encodeValue(bundle, bufferFactory, elementType, mimeType, hints));
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        return Collections.singletonList(MediaType.APPLICATION_CBOR);
    }

    @Override
    public DataBuffer encodeValue(Bundle bundle, DataBufferFactory bufferFactory, ResolvableType valueType,
                                  MimeType mimeType, Map<String, Object> hints) {
        return bufferFactory.wrap(serialize(bundle));
    }

    private byte[] serialize(Bundle bundle) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        SerializationUtils.serialize(bundle, baos);
        return baos.toByteArray();
    }
}
