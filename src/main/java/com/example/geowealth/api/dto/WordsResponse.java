package com.example.geowealth.api.dto;

import java.util.List;

public record WordsResponse(List<String> words, long durationMs, long foundWords) { }
