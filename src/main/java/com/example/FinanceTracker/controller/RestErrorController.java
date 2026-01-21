package com.example.FinanceTracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RestErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) {

        Object status = request.getAttribute("jakarta.servlet.error.status_code");

        int statusCode = status != null ? (int) status : 500;

        return ResponseEntity
                .status(statusCode)
                .body(Map.of(
                        "error", "Request failed",
                        "status", statusCode
                ));
    }
}
