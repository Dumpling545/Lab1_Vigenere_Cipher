package lab1.helper;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AlphabetDetector {
	private boolean matchesAlphabet(Set<Character> textSet, Alphabet alphabet){
		var alphaList = alphabet.getAlphabetList();
		var remainderSet = new HashSet<>(textSet);
		alphaList.forEach(remainderSet::remove);
		remainderSet.removeAll(Alphabet.getIgnoredSymbolsSet());
		return remainderSet.isEmpty();
	}
	public Optional<Alphabet> detect(String text){
		Set<Character> textSet = text.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
		return Arrays.stream(Alphabet.values()).filter((a) -> matchesAlphabet(textSet, a)).findFirst();
	}
	public Optional<Alphabet> detect(List<String> texts){
		if(texts.isEmpty()) {
			return Optional.empty();
		}
		var firstTextAlphabet = detect(texts.get(0));
		if(firstTextAlphabet.isEmpty() || texts.size() == 1){
			return firstTextAlphabet;
		}
		Alphabet alphabet = firstTextAlphabet.get();
		boolean sameAlphabets = texts.subList(1, texts.size()).stream()
				.map(t -> t.chars().mapToObj(c -> (char) c).collect(Collectors.toSet()))
				.allMatch(s -> matchesAlphabet(s, alphabet));
		if(sameAlphabets){
			return firstTextAlphabet;
		} else {
			return Optional.empty();
		}
	}
}
