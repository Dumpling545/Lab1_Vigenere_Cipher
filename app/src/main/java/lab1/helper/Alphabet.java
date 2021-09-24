package lab1.helper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public enum Alphabet {

	ENGLISH("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
	RUSSIAN("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");
	private final List<Character> alphabetList;


	private final Map<Character, Integer> charToAlphabetIndexMap;
	private static final Set<Character> ignored = " …,.!?:;^'-_—“”‘’\"()[]{}<>\n\r\t0123456789&$£½*\u200A".chars()
			.mapToObj(c -> (char) c)
			.collect(Collectors.toSet());

	Alphabet(String alphaString) {
		alphabetList = alphaString.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		charToAlphabetIndexMap = IntStream.range(0, alphabetList.size())
				.boxed()
				.collect(toMap(alphabetList::get, i -> i));
	}

	public List<Character> getAlphabetList() {
		return alphabetList;
	}
	public Map<Character, Integer> getCharToAlphabetIndexMap() {
		return charToAlphabetIndexMap;
	}

	public static Set<Character> getIgnoredSymbolsSet() {
		return ignored;
	}

}
