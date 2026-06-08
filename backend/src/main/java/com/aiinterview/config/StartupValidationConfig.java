package com.aiinterview.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;

@Component
public class StartupValidationConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupValidationConfig.class);

    @Value("${aiinterview.app.jwtSecret}")
    private String jwtSecret;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Performing Startup Security and Configuration Validation...");

        // 1. Validate JWT Secret
        if (jwtSecret == null || jwtSecret.trim().isEmpty() || jwtSecret.contains("YOUR_BASE64_JWT_SECRET_KEY")) {
            logger.error("********************************************************************************");
            logger.error("CRITICAL CONFIGURATION ERROR: JWT Secret is missing!");
            logger.error("The property 'aiinterview.app.jwtSecret' (JWT_SECRET) is mandatory.");
            logger.error("Please configure the JWT_SECRET environment variable and restart the application.");
            logger.error("********************************************************************************");
            throw new IllegalStateException("Startup failed: JWT_SECRET environment variable is mandatory and cannot be empty.");
        }

        // Test base64 decoding of the JWT secret to catch encoding issues early
        try {
            byte[] decodedKey = Decoders.BASE64.decode(jwtSecret);
            if (decodedKey.length < 32) {
                logger.warn("********************************************************************************");
                logger.warn("WARNING: JWT secret is less than 256 bits (32 bytes) after Base64 decoding.");
                logger.warn("This might lead to WeakKeyException during token generation.");
                logger.warn("********************************************************************************");
            }
        } catch (IllegalArgumentException e) {
            logger.error("********************************************************************************");
            logger.error("CRITICAL CONFIGURATION ERROR: JWT Secret is not a valid Base64 encoded string!");
            logger.error("Error: {}", e.getMessage());
            logger.error("Please provide a valid Base64 encoded string for JWT_SECRET.");
            logger.error("********************************************************************************");
            throw new IllegalStateException("Startup failed: JWT_SECRET must be a valid Base64 encoded string.", e);
        }

        // 2. Validate Gemini API Key
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty() || geminiApiKey.contains("YOUR_GEMINI_API_KEY") || geminiApiKey.equals("AIzaSyDliVAO41hLZaiv3qeZCtV1hfBrkUrRSvs")) {
            logger.warn("********************************************************************************");
            logger.warn("WARNING: Gemini API Key is missing or placeholder key is active!");
            logger.warn("The property 'gemini.api.key' (GEMINI_API_KEY) is empty or using a dummy key.");
            logger.warn("AI features will be disabled. Fallback/offline modes will be used instead.");
            logger.warn("To enable AI features, set the GEMINI_API_KEY environment variable.");
            logger.warn("********************************************************************************");
        } else {
            logger.info("Gemini API Key validation: Present.");
        }

        logger.info("Startup validation completed successfully.");
    }
}
