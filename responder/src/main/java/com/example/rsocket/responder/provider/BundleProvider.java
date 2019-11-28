package com.example.rsocket.responder.provider;

import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class BundleProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(BundleProvider.class);

    private Map<Integer, String> bundleForEntries = new HashMap<>(4);
    private Map<Integer, Bundle> rawBundleForEntries = new HashMap<>(4);

    private final IParser fhirParser;

    @Autowired
    public BundleProvider(IParser fhirParser) {
        this.fhirParser = fhirParser;
    }

    @Async
    @PostConstruct
    public void loadBundleSamples() {
        Stream.of(100, 200, 400, 800)
              .forEach(entries -> {
                  final String bundleContent = loadVitalSignsSample(entries);
                  bundleForEntries.put(entries, bundleContent);
                  rawBundleForEntries.put(entries, fhirParser.parseResource(Bundle.class, bundleContent));
              });
    }

    public String getBundle(int recordsNumber) {
        return bundleForEntries.get(recordsNumber);
    }

    public Bundle getRawBundle(int recordsNumber) {
        return Optional.ofNullable(rawBundleForEntries.get(recordsNumber))
                       .orElseThrow(() -> new IllegalArgumentException("There is no Bundle of " + recordsNumber + " entries"));
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
