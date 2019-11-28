package com.example.rsocket.responder.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {

    @Bean
    public FhirContext fhirContext(@Value("${fhir.client.socket-timeout:10000}") int fhirClientSocketTimeout) {
        FhirContext fhirContext = FhirContext.forR4();
        fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        fhirContext.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        fhirContext.getRestfulClientFactory().setSocketTimeout(fhirClientSocketTimeout);

        return fhirContext;
    }

    @Bean
    public IParser fhirParser() {
        return FhirContext.forR4().newJsonParser();
    }
}
