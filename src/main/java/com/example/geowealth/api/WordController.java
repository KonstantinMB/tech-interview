package com.example.geowealth.api;

import com.example.geowealth.api.dto.WordsResponse;
import com.example.geowealth.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @Operation(summary = "Find all words that match pre-defined wordLength")
    @GetMapping("/words/{wordLength}")
    public ResponseEntity<WordsResponse> findAllWords(@PathVariable Integer wordLength) {

        WordsResponse response = wordService.findAllWords(wordLength);

        if (response == null || response.words().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new WordsResponse(List.of(), 0, 0));
        }

        return ResponseEntity.ok(response);
    }
}
