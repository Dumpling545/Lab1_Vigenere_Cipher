package lab1.cryptoanalysis;

import lab1.helper.Alphabet;
import lab1.helper.AlphabetDetector;
import lab1.helper.LetterFrequency;
import lab1.helper.LetterFrequencyHolder;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@Component
public class CaesarCipherAnalyzer implements CryptoAnalyzer {

	AlphabetDetector alphabetDetector;
	LetterFrequencyHolder letterFrequencyHolder;

	private LetterFrequency[] computeTextFrequencies(String encryptedText) {
		double letterAmount = encryptedText.chars()
				.boxed()
				.map(c -> (char) c.intValue())
				.filter(c -> !Alphabet.getIgnoredSymbolsSet().contains(c)).count();
		var frequencyMap = encryptedText.chars()
				.boxed()
				.map(c -> (char) c.intValue())
				.filter(c -> !Alphabet.getIgnoredSymbolsSet().contains(c))
				.collect(Collectors.groupingBy(c -> c, Collectors.collectingAndThen(Collectors.counting(), n -> n / letterAmount)));
		return frequencyMap.entrySet().stream()
				.map(e -> new LetterFrequency(e.getKey(), e.getValue()))
				.toArray(LetterFrequency[]::new);
	}

	private int shiftBetweenChars(char from, char to, Alphabet alphabet) {
		int indexFrom = alphabet.getCharToAlphabetIndexMap().get(from);
		int indexTo = alphabet.getCharToAlphabetIndexMap().get(to);
		int alphabetLength = alphabet.getAlphabetList().size();
		if (indexTo >= indexFrom) {
			return indexTo - indexFrom;
		} else {
			return alphabetLength - indexFrom + indexTo;
		}
	}

	private int findShiftByFrequency(LetterFrequency letterFrequency, LetterFrequency[] alphabetFrequencies, Alphabet alphabet){
		double delta = Math.abs(letterFrequency.frequency() - alphabetFrequencies[0].frequency());
		int j = 0;
		while (j < alphabetFrequencies.length - 1) {
			double temporaryDelta = Math.abs(letterFrequency.frequency() - alphabetFrequencies[++j].frequency());
			if (delta < temporaryDelta) {
				break;
			} else {
				delta = temporaryDelta;
			}
		}
		return shiftBetweenChars(alphabetFrequencies[j].character(), letterFrequency.character(), alphabet) ;
	}

	private int getCaesarShift(String encryptedText, LetterFrequency[] alphabetFrequencies, Alphabet alphabet) {
		LetterFrequency[] textFrequencies = computeTextFrequencies(encryptedText);
		Map<Integer, Long> shifts = Arrays.stream(textFrequencies)
				.map(lf -> findShiftByFrequency(lf, alphabetFrequencies, alphabet))
				.collect(Collectors.groupingBy(i -> i, Collectors.counting()));
		var maxEntryByValue = shifts.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue));
		return maxEntryByValue.map(Map.Entry::getKey).orElse(0);
	}

	@Override
	public String getSecretKey(String encryptedText) {
		Optional<Alphabet> alphabetOptional = alphabetDetector.detect(encryptedText);
		Alphabet alphabet = alphabetOptional.orElseThrow(UndetectedAlphabetCryptoAnalyzerException::new);
		int shift = switch (alphabet) {
			case ENGLISH -> getCaesarShift(encryptedText, letterFrequencyHolder.getEnglish(), alphabet);
			case RUSSIAN -> getCaesarShift(encryptedText, letterFrequencyHolder.getRussian(), alphabet);
		};
		return alphabet.getAlphabetList().get(shift).toString();
	}
}