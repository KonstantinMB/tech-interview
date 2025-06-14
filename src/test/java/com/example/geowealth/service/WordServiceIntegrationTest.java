package com.example.geowealth.service;

import com.example.geowealth.api.dto.WordsResponse;
import com.example.geowealth.external.WordFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WordServiceIntegrationTest {

    @Autowired
    private WordService wordService;

    @Mock
    private WordFeignClient wordFeignClient;

    @Test
    void shouldInitializeService() {
        assertThat(wordService).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 9})
    void shouldProcessWordsWithinTwoSeconds(int wordLength) {

        WordsResponse response = wordService.findAllWords(wordLength);

        assertThat(response.durationMs())
                .as("Processing time should be less than 2000ms")
                .isLessThan(2000); // less than 2 seconds as per reqs
        assertThat(response.words())
                .isNotEmpty()
                .allMatch(word -> word.length() == wordLength);
        assertThat(response.foundWords()).isGreaterThan(0);
    }

}