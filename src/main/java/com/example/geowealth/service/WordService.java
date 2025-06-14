package com.example.geowealth.service;

import com.example.geowealth.api.dto.WordsResponse;
import com.example.geowealth.external.WordFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordFeignClient wordFeignClient;
    private final AtomicReference<Set<String>> cache = new AtomicReference<>();

    public WordsResponse findAllWords(Integer wordLength) {
        long start = System.nanoTime();

        Set<String> words = cache.updateAndGet(current -> {
            if (current != null) return current;

            return Set.of(Arrays.stream(
                            wordFeignClient.getWordList().split("\\R"))
                    .map(String::trim)
                    .toArray(String[]::new)
            );
        });

        System.out.println("after remote fetch, time in ms: " + (System.nanoTime() - start) / 1_000_000);
        System.out.println("size of cache: " + words.size());

        List<String> filtered = new java.util.ArrayList<>(words.stream()
                .filter(w -> w.length() == wordLength)
                // since the resulted one-letter word can be only I or A, we filter out words which don't have any of these letters present in them
                .filter(w -> w.contains("I") || w.contains("A"))
                .filter(this::isValidWord)
                .toList());

        long duration = (System.nanoTime() - start) / 1_000_000;

        return new WordsResponse(filtered, duration, filtered.size());
    }

    private boolean isValidWord(String word) {

        String tempWord = word;
        System.out.println("word: " + tempWord);

        while (tempWord.length() > 1) {

            int len = tempWord.length();

            for (int i = 0; i < len; i++) {

                String resultedWord = new StringBuilder(tempWord.substring(0, i))
                        .append(tempWord.substring(i + 1))
                        .toString();

                if (resultedWord.equals("I") || resultedWord.equals("A")) return true;

                if (cache.get().contains(resultedWord) &&
                        (resultedWord.indexOf("I") > 0 || resultedWord.indexOf("A") > 0)
                ) {
                    System.out.println("new word: " + resultedWord);
                    tempWord = resultedWord;
                    break; // startling
                }
            }
            if (tempWord.length() == len) return false;
        }

        return true;
    }
}


// Examples:

//  word:     ODORISERS
//  new word: ODORISES
//  new word: DORISES
//  new word: DORIES
//  new word: DRIES
//  new word: DIES
//  new word: DIS
//  new word: DI

//  word:     DROPPINGS
//  new word: DOPPINGS
//  new word: DOPINGS
//  new word: DOINGS
//  new word: DINGS
//  new word: DIGS
//  new word: DIS
//  new word: DI