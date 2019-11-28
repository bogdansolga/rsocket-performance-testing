package com.example.rsocket.requester.service;

import ca.uhn.fhir.parser.IParser;
import com.example.rsocket.requester.constants.CommunicationType;
import com.example.rsocket.requester.constants.OperationType;
import com.example.rsocket.requester.dto.StringProcessingTimesAveragesDTO;
import com.example.rsocket.requester.dto.StringProcessingTimesDTO;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public abstract class AbstractCommunicationService {

    private static final int NUMBER_OF_FHIR_REQUESTS = 20;

    private final List<Integer> entriesValues = Arrays.asList(100, 200, 400, 800);

    final IParser fhirParser;

    @Autowired
    public AbstractCommunicationService(IParser fhirParser) {
        this.fhirParser = fhirParser;
    }

    public abstract String readBundle(int entriesNumber);

    public abstract CommunicationType getCommunicationType();

    List<StringProcessingTimesAveragesDTO> generateAverageStringCommunicationData() {
        final List<StringProcessingTimesDTO> processingTimes = Stream.of(NUMBER_OF_FHIR_REQUESTS)
                                                                     .flatMap(item -> generateStringData().stream())
                                                                     .collect(Collectors.toList());

        return entriesValues.stream()
                            .map(entries -> getAverageProcessingTimes(processingTimes, entries))
                            .collect(Collectors.toList());
    }

    private List<StringProcessingTimesDTO> generateStringData() {
        return entriesValues.stream()
                            .map(this::read)
                            .collect(Collectors.toList());
    }

    private StringProcessingTimesDTO read(int entriesNumber) {
        long now = System.currentTimeMillis();
        final String bundleString = readBundle(entriesNumber);
        final int requestTime = (int) (System.currentTimeMillis() - now);

        long deSerializingTimeStart = System.currentTimeMillis();
        fhirParser.parseResource(Bundle.class, bundleString);
        final int deSerializingTime = (int) (System.currentTimeMillis() - deSerializingTimeStart);

        long totalTime = System.currentTimeMillis() - now;

        return new StringProcessingTimesDTO(OperationType.READ, getCommunicationType(), entriesNumber,
                bundleString.length(), totalTime, requestTime, null, deSerializingTime);
    }

    private StringProcessingTimesAveragesDTO getAverageProcessingTimes(List<StringProcessingTimesDTO> processingTimes,
                                                                       int entriesNumber) {
        int averageBodySize = processingTimes.stream()
                                             .filter(it -> it.getBundleEntries() == entriesNumber)
                                             .collect(Collectors.averagingInt(StringProcessingTimesDTO::getStringSizeInBytes))
                                             .intValue();

        int averageTotalTime = processingTimes.stream()
                                              .filter(it -> it.getBundleEntries() == entriesNumber)
                                              .collect(Collectors.averagingLong(StringProcessingTimesDTO::getTotalTime))
                                              .intValue();

        double averageCommTime = processingTimes.stream()
                                                .filter(it -> it.getBundleEntries() == entriesNumber)
                                                .collect(Collectors.averagingDouble(StringProcessingTimesDTO::getCommTime));

        double averageDeSerializingTime = processingTimes.stream()
                                                         .filter(it -> it.getBundleEntries() == entriesNumber &&
                                                                 it.getDeSerializingTime() != null)
                                                         .collect(Collectors.averagingDouble(
                                                                 StringProcessingTimesDTO::getDeSerializingTime));

        return new StringProcessingTimesAveragesDTO(getCommunicationType(), OperationType.READ, entriesNumber,
                averageBodySize, averageTotalTime, averageCommTime, null, averageDeSerializingTime);
    }
}
