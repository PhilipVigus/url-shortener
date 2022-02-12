package com.philvigus.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UrlShortenerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void failingTest() {
        assertEquals(true, false);
    }

}
