package lab1.cryptoanalysis.keywordlengthpredictor;

import com.google.common.math.IntMath;
import lab1.helper.Alphabet;
import lab1.helper.AlphabetDetector;
import lab1.helper.PositionedWord;
import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Value
public class KasiskiKeywordLengthPredictor implements KeywordLengthPredictor {
	int searchLength;
	double differenceFrequencyThreshold;
	AlphabetDetector alphabetDetector = new AlphabetDetector();

	private boolean hasOnlyAlphabetCharacters(String word, Alphabet alphabet) {
		return word.chars().allMatch(c -> alphabet.getCharToAlphabetIndexMap().containsKey((char) c));
	}

	private Map<String, List<Integer>> getRepeatedWordOccurrences(String text, Alphabet alphabet) {
		Map<String, List<Integer>> wordOccurrences =  IntStream.range(0, text.length() - searchLength)
				.mapToObj(index -> new PositionedWord(index, text.substring(index, index + searchLength)))
				.filter(pw -> hasOnlyAlphabetCharacters(pw.word(), alphabet))
				.collect(groupingBy(PositionedWord::word, Collectors.mapping(PositionedWord::position, toList())));
		return wordOccurrences.entrySet().stream()
				.filter(e -> e.getValue().size() > 1)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map<Integer, Integer> computeDifferences(Map<String, List<Integer>> repeatedWordOccurrences) {
		Map<Integer, Integer> differences = new HashMap<>();
		for (var entry : repeatedWordOccurrences.entrySet()) {
			var occurrencesList = entry.getValue();
			for (int i = 1; i < occurrencesList.size(); i++) {
				int difference = occurrencesList.get(i) - occurrencesList.get(i - 1);
				differences.merge(difference, 1, (ov, nv) -> ov + 1);
			}
		}
		return differences;
	}

	private List<Integer> retainFrequentDifferences(Map<Integer, Integer> differences) {
		int maxFrequency = differences.entrySet().stream()
				.max(Map.Entry.comparingByValue()).map(Map.Entry::getValue).orElse(1);
		int minFrequency = (int) Math.ceil(differenceFrequencyThreshold * maxFrequency);
		return differences.entrySet().stream()
				.filter(e -> e.getValue() >= minFrequency)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	private int gcd(List<Integer> integers) {
		return integers.stream().reduce(IntMath::gcd).orElse(1);
	}

	@Override
	public int predictKeywordLength(String text) {
		Optional<Alphabet> alphabetOptional = alphabetDetector.detect(text);
		Alphabet alphabet = alphabetOptional.orElseThrow(UndetectedAlphabetKeywordLengthPredictorException::new);
		Map<String, List<Integer>> repeatedWordOccurrences = getRepeatedWordOccurrences(text, alphabet);
		Map<Integer, Integer> differences = computeDifferences(repeatedWordOccurrences);
		List<Integer> frequentDifferences = retainFrequentDifferences(differences);
		return gcd(frequentDifferences);
	}
}
