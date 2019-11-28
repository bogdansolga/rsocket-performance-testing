package com.example.rsocket.requester.controller;

import com.example.rsocket.requester.dto.StringProcessingTimesAveragesDTO;
import com.example.rsocket.requester.dto.StringProcessingTimesDTO;
import com.example.rsocket.requester.service.PerformanceDataGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perf-data")
public class PerformanceAnalysisController {

    private final PerformanceDataGenerationService performanceDataGenerationService;

    @Autowired
    public PerformanceAnalysisController(PerformanceDataGenerationService performanceDataGenerationService) {
        this.performanceDataGenerationService = performanceDataGenerationService;
    }

    @GetMapping("/http")
    public List<StringProcessingTimesAveragesDTO> getHTTPCommunicationDetails() {
        return performanceDataGenerationService.generateStringAverageDataUsingHTTP();
    }

    @GetMapping("/rsocket")
    public List<StringProcessingTimesAveragesDTO> getRSocketCommunicationDetails() {
        return performanceDataGenerationService.generateStringAverageDataUsingRSocket();
    }

    @GetMapping("/rsocket/raw/{entries}")
    public StringProcessingTimesDTO getRawBundle(@PathVariable int entries) {
        return performanceDataGenerationService.readRawBundle(entries);
    }
}
