package com.example.rsocket.requester.provider;

import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class BundleProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(BundleProvider.class);

    private final IParser fhirParser;

    public BundleProvider(IParser fhirParser) {
        this.fhirParser = fhirParser;
    }

    public Bundle getVitalSignsBundle(int recordsNumber) {
        final String bundleContent = loadVitalSignsSample(recordsNumber);

        return fhirParser.parseResource(Bundle.class, bundleContent);
    }

    private String loadVitalSignsSample(int recordsNumber) {
        try {
            final File resourceFile = buildResource(recordsNumber).getFile();
            return new String(Files.readAllBytes(resourceFile.toPath()));
        } catch (IOException ioex) {
            LOGGER.error(ioex.getMessage(), ioex);
            throw new IllegalArgumentException(ioex.getMessage());
        }
    }

    private ClassPathResource buildResource(int recordsNumber) {
        return new ClassPathResource("vital-signs-" + recordsNumber + "-entries.json");
    }
}
