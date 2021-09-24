package lab1;

import lab1.cryptoanalysis.CaesarCipherAnalyzer;
import lab1.cryptoanalysis.keywordlengthpredictor.KasiskiKeywordLengthPredictor;
import lab1.cryptoanalysis.keywordlengthpredictor.KeywordLengthPredictor;
import lab1.helper.AlphabetDetector;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom")
public class MainConfiguration {
	private int kasiskiSearchLength;
	private double kasiskiDifferenceFrequencyThreshold;
	@Bean
	KeywordLengthPredictor keywordLengthPredictor(){
		return new KasiskiKeywordLengthPredictor(kasiskiSearchLength, kasiskiDifferenceFrequencyThreshold);
	}
}
