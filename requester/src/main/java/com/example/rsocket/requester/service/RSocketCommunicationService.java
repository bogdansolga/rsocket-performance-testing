package com.example.rsocket.requester.service;

import ca.uhn.fhir.parser.IParser;
import com.example.rsocket.requester.constants.CommunicationType;
import com.example.rsocket.requester.constants.OperationType;
import com.example.rsocket.requester.dto.StringProcessingTimesDTO;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RSocketCommunicationService extends AbstractCommunicationService {

    private final Mono<RSocketRequester> requester;

    @Autowired
    public RSocketCommunicationService(IParser fhirParser, Mono<RSocketRequester> requester) {
        super(fhirParser);
        this.requester = requester;
    }

    @Override
    public String readBundle(int entries) {
        return requester.flatMap(rSocketRequester -> retrieveBundle(rSocketRequester, entries))
                        .block(); //TODO is there a better way to retrieve the wrapped value?
    }

    public StringProcessingTimesDTO readRawBundle(int entriesNumber) {
        long now = System.currentTimeMillis();
        final Bundle bundle = requester.flatMap(rSocketRequester -> retrieveRawBundle(rSocketRequester, entriesNumber))
                                       .block();
        final int requestTime = (int) (System.currentTimeMillis() - now);

        long serializingTimeStart = System.currentTimeMillis();
        final String resourceToString = fhirParser.encodeResourceToString(bundle);
        final int serializingTime = (int) (System.currentTimeMillis() - serializingTimeStart);

        long totalTime = System.currentTimeMillis() - now;

        return new StringProcessingTimesDTO(OperationType.READ, getCommunicationType(), entriesNumber,
                resourceToString.length(), totalTime, requestTime, serializingTime, null);
    }

    @Override
    public CommunicationType getCommunicationType() {
        return CommunicationType.RSocket;
    }

    private Mono<String> retrieveBundle(final RSocketRequester rSocketRequester, int entries) {
        return rSocketRequester.route("bundle.read")
                               .data(entries)
                               .retrieveMono(String.class);
    }

    private Mono<Bundle> retrieveRawBundle(final RSocketRequester rSocketRequester, int entries) {
        return rSocketRequester.route("bundle.read.raw")
                               .data(entries)
                               .retrieveMono(Bundle.class);
    }
}
