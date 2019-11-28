package com.example.rsocket.requester.service;

import ca.uhn.fhir.parser.IParser;
import com.example.rsocket.requester.constants.CommunicationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StringCommunicationService extends AbstractCommunicationService {

    private final RestTemplate restTemplate;

    @Value("${responder.strings.endpoints.uri}")
    private String responderStringsEndpointsURI;

    @Autowired
    public StringCommunicationService(IParser fhirParser, RestTemplate restTemplate) {
        super(fhirParser);
        this.restTemplate = restTemplate;
    }

    @Override
    public String readBundle(int entriesNumber) {
        return restTemplate.getForEntity(responderStringsEndpointsURI + "/read/" + entriesNumber, String.class)
                           .getBody();
    }

    @Override
    public CommunicationType getCommunicationType() {
        return CommunicationType.HTTP;
    }
}
