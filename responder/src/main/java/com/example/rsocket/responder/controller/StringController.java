package com.example.rsocket.responder.controller;

import com.example.rsocket.responder.provider.BundleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bundle")
public class StringController {

    private final BundleProvider bundleProvider;

    @Autowired
    public StringController(BundleProvider bundleProvider) {
        this.bundleProvider = bundleProvider;
    }

    @GetMapping("/read/{entries}")
    public String read(@PathVariable int entries) {
        return bundleProvider.getBundle(entries);
    }

    @PostMapping("/write")
    public ResponseEntity<String> write(@RequestBody String requestBody) {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
