# Word Reduction API

This service finds words that can be reduced to "I" or "A" by removing one letter at a time, where each intermediate result must also be a valid word.

## Quick Start

1. Start the application locally
2. Open Swagger UI at: http://localhost:8080/swagger-ui/index.html#/word-controller/findAllWords

## API Usage

### Find Reducible Words
Send a GET request specifying the word length:
`GET /api/v1/words?wordLength=9`

#### Request Parameters
- `wordLength` - Length of words to search for (e.g. 9)

#### Response Format
```json 
{ "words": ["STARTLING", "SPIRALING", ...], // List of found words 
  "durationMs": 1234, // Processing time in milliseconds 
  "foundWords": 42 // Number of words found 
}
```
#### Example Using cURL
```bash
curl -X GET "[http://localhost:8080/api/v1/words?wordLength=9](http://localhost:8080/api/v1/words?wordLength=9)"
```
## Example Word Reduction
The word "STARTLING" can be reduced to "I" through this sequence:
```txt
STARTLING → STARTING → STATING → SATING → STING → SING → SIN → IN → I
```

## Performance
- Processing time is guaranteed to be under 2 seconds
- Verified by integration tests that use actual dictionary service

## Testing
Integration tests cover:
- Real dictionary service integration
- Performance requirements
- Word reduction logic
