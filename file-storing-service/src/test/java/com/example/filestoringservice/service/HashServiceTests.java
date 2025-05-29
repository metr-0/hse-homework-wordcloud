package com.example.filestoringservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class HashServiceTests {

    private HashService hashService;

    @BeforeEach
    void setUp() {
        hashService = new HashService();
    }

    @Test
    void computeHash_shouldReturnCorrectSHA256() throws IOException, NoSuchAlgorithmException {
        // SHA-256("test string") = d5579c46dfcc7f18207013e65b44e4cb4e2c2298f4ac457ba8f82743f31e930b
        String input = "test string";
        String expectedHash = "d5579c46dfcc7f18207013e65b44e4cb4e2c2298f4ac457ba8f82743f31e930b";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        String result = hashService.computeHash(inputStream);

        assertEquals(expectedHash, result);
    }

    @Test
    void computeHash_emptyStream_shouldReturnSHA256OfEmptyString() throws IOException, NoSuchAlgorithmException {
        // SHA-256("") = e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        String hash = hashService.computeHash(emptyStream);

        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);
    }

}
