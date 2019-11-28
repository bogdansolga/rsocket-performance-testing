package com.example.rsocket.requester.service;

import com.example.rsocket.requester.dto.StringProcessingTimesAveragesDTO;
import com.example.rsocket.requester.dto.StringProcessingTimesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceDataGenerationService {

    private final RSocketCommunicationService rSocketCommunicationService;
    private final StringCommunicationService stringCommunicationService;

    public PerformanceDataGenerationService(RSocketCommunicationService rSocketCommunicationService,
                                            StringCommunicationService stringCommunicationService) {
        this.rSocketCommunicationService = rSocketCommunicationService;
        this.stringCommunicationService = stringCommunicationService;
    }

    public List<StringProcessingTimesAveragesDTO> generateStringAverageDataUsingHTTP() {
        return stringCommunicationService.generateAverageStringCommunicationData();
    }

    public List<StringProcessingTimesAveragesDTO> generateStringAverageDataUsingRSocket() {
        return rSocketCommunicationService.generateAverageStringCommunicationData();
    }

    public StringProcessingTimesDTO readRawBundle(int entries) {
        return rSocketCommunicationService.readRawBundle(entries);
    }
}
