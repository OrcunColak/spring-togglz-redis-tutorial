package com.colak.springtutorial.controller;

import com.colak.springtutorial.config.ProductCheckFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {


    // http://localhost:8080/check
    // http://localhost:8080/togglz-console
    @GetMapping("/check")
    public ResponseEntity<String> checkProduct() {
        if (ProductCheckFeature.isEnhancedLoggingActive()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("ENHANCED_LOGGING is enabled");
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("OLD_LOGGING is enabled");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        // Return 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
