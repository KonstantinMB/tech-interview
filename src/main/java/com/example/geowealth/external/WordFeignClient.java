package com.example.geowealth.external;

import com.example.geowealth.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "scrabbleWordsClient",
        url  = "https://raw.githubusercontent.com",
        configuration = FeignConfig.class
)
public interface WordFeignClient {

    @GetMapping(
            value = "/nikiiv/JavaCodingTestOne/master/scrabble-words.txt",
            produces = "text/plain"
    )
    String getWordList();
}
