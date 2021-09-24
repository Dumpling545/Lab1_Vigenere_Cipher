package lab1.helper;

import lombok.Data;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;

@Value
@Component
public class LetterFrequencyHolder {
	LetterFrequency[] english = {
			new LetterFrequency('A', 0.08167),
			new LetterFrequency('B', 0.01492),
			new LetterFrequency('C', 0.02782),
			new LetterFrequency('D', 0.04253),
			new LetterFrequency('E', 0.12702),
			new LetterFrequency('F', 0.0228),
			new LetterFrequency('G', 0.02015),
			new LetterFrequency('H', 0.06094),
			new LetterFrequency('I', 0.06966),
			new LetterFrequency('J', 0.00153),
			new LetterFrequency('K', 0.00772),
			new LetterFrequency('L', 0.04025),
			new LetterFrequency('M', 0.02406),
			new LetterFrequency('N', 0.06749),
			new LetterFrequency('O', 0.07507),
			new LetterFrequency('P', 0.01929),
			new LetterFrequency('Q', 0.00095),
			new LetterFrequency('R', 0.05987),
			new LetterFrequency('S', 0.06327),
			new LetterFrequency('T', 0.09056),
			new LetterFrequency('U', 0.02758),
			new LetterFrequency('V', 0.00978),
			new LetterFrequency('W', 0.0236),
			new LetterFrequency('X', 0.0015),
			new LetterFrequency('Y', 0.01974),
			new LetterFrequency('Z', 0.00074)
	};

	LetterFrequency[] russian = {
			new LetterFrequency('А', 0.07821),
			new LetterFrequency('Б', 0.01732),
			new LetterFrequency('В', 0.04491),
			new LetterFrequency('Г', 0.01698),
			new LetterFrequency('Д', 0.03103),
			new LetterFrequency('Е', 0.08567),
			new LetterFrequency('Ё', 0.0007),
			new LetterFrequency('Ж', 0.01082),
			new LetterFrequency('З', 0.01647),
			new LetterFrequency('И', 0.06777),
			new LetterFrequency('Й', 0.01041),
			new LetterFrequency('К', 0.03215),
			new LetterFrequency('Л', 0.04813),
			new LetterFrequency('М', 0.03139),
			new LetterFrequency('Н', 0.0685),
			new LetterFrequency('О', 0.11394),
			new LetterFrequency('П', 0.02754),
			new LetterFrequency('Р', 0.04234),
			new LetterFrequency('С', 0.05382),
			new LetterFrequency('Т', 0.06443),
			new LetterFrequency('У', 0.02882),
			new LetterFrequency('Ф', 0.00132),
			new LetterFrequency('Х', 0.00833),
			new LetterFrequency('Ц', 0.00333),
			new LetterFrequency('Ч', 0.01645),
			new LetterFrequency('Ш', 0.00775),
			new LetterFrequency('Щ', 0.00331),
			new LetterFrequency('Ъ', 0.00023),
			new LetterFrequency('Ы', 0.01854),
			new LetterFrequency('Ь', 0.02106),
			new LetterFrequency('Э', 0.0031),
			new LetterFrequency('Ю', 0.00544),
			new LetterFrequency('Я', 0.01979)};

	public LetterFrequencyHolder(){
		Arrays.sort(russian, Comparator.comparingDouble(LetterFrequency::frequency));
		Arrays.sort(english, Comparator.comparingDouble(LetterFrequency::frequency));
	}
}
