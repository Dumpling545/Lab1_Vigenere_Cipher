package lab1.cryptoanalysis;

import lab1.cryptoanalysis.keywordlengthpredictor.KasiskiKeywordLengthPredictor;
import lab1.cryptoanalysis.keywordlengthpredictor.KeywordLengthPredictor;
import lab1.cryptoanalysis.keywordlengthpredictor.KeywordLengthPredictorException;
import lab1.cryptoanalysis.keywordlengthpredictor.UndetectedAlphabetKeywordLengthPredictorException;
import lombok.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Collector;
import java.util.stream.IntStream;

@Value
@Primary
@Component
public class VigenereCipherAnalyzer implements CryptoAnalyzer {
	KeywordLengthPredictor keywordLengthPredictor;
	CaesarCipherAnalyzer caesarCipherAnalyzer;

	@Override
	public String getSecretKey(String encryptedText) {
		int secretKeyLength;
		try {
			secretKeyLength = keywordLengthPredictor.predictKeywordLength(encryptedText);
		} catch (UndetectedAlphabetKeywordLengthPredictorException ex) {
			throw new UndetectedAlphabetCryptoAnalyzerException(ex);
		} catch (KeywordLengthPredictorException ex){
			throw new KeywordLengthPredictorException("Unexpected exception", ex);
		}
		StringBuilder secretKey = new StringBuilder();
		for (int r = 0; r < secretKeyLength; r++) {
			int remainder = r;
			String slice = IntStream.range(0, encryptedText.length())
					.filter(i -> i % secretKeyLength == remainder)
					.mapToObj(encryptedText::charAt)
					.collect(Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append,
							StringBuilder::toString));
			secretKey.append(caesarCipherAnalyzer.getSecretKey(slice));
		}
		return secretKey.toString();
	}
}
