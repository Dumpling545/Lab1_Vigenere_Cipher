package lab1.report;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import lab1.cryptoanalysis.CryptoAnalyzer;
import lab1.encryption.Encryptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Component
@ConfigurationProperties(prefix = "custom.report")
public class ReportRunner {
	private String textFileName;
	private List<Integer> textLengths;
	private List<Integer> secretKeyLengths;
	private String textFile;
	private int numberOfTexts;
	private List<Integer> defaultSecretKeyLengths;
	private List<Integer> defaultTextLengths;

	private final Encryptor encryptor;
	private final CryptoAnalyzer analyzer;

	private String generateSecretKey(int length) {
		return (new Random()).ints('A', 'Z')
				.limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	private String[] generateTextsFromOrigin(int length, String origin) {
		int index = (new Random()).nextInt(origin.length() - numberOfTexts * length);
		return IntStream.range(0, numberOfTexts)
				.mapToObj(i -> origin.substring(index + i * length, index + (i + 1) * length))
				.toArray(String[]::new);
	}

	private double calculateSuccessRate(String derivedKey, String originalKey) {
		int maxLength = Math.max(derivedKey.length(), originalKey.length());
		int minLength = Math.min(derivedKey.length(), originalKey.length());
		double rate = 0;
		for (int i = 0; i < minLength; i++) {
			if (derivedKey.charAt(i) == originalKey.charAt(i)) {
				rate++;
			}
		}
		return rate / maxLength;
	}

	private List<XYChart.Series<Number, Number>> generateSuccessRateData(List<Integer> xAxisPoints,
	                                                                     List<String> seriesNames,
	                                                                     BiFunction<Integer, Integer, Integer> textLengthGenerator,
	                                                                     BiFunction<Integer, Integer, Integer> keyLengthGenerator) {
		List<XYChart.Series<Number, Number>> seriesList = new ArrayList<>();
		for (int si = 0; si < seriesNames.size(); si++) {
			var series = new XYChart.Series<Number, Number>();
			series.setName(seriesNames.get(si));
			for (int xi = 0; xi < xAxisPoints.size(); xi++) {
				var texts = generateTextsFromOrigin(textLengthGenerator.apply(si, xi), textFile);
				double averageSuccessRate = 0;
				for (int k = 0; k < numberOfTexts; k++) {
					String key = generateSecretKey(keyLengthGenerator.apply(si, xi));
					String rawText = texts[k];
					String encryptedText = encryptor.encrypt(rawText, key);
					String derivedKey = analyzer.getSecretKey(encryptedText);
					averageSuccessRate += calculateSuccessRate(derivedKey, key);
				}
				averageSuccessRate /= numberOfTexts;
				series.getData().add(new XYChart.Data<>(xAxisPoints.get(xi), averageSuccessRate));
			}
			seriesList.add(series);
		}
		return seriesList;
	}


	private LineChart<Number, Number> generateSuccessRateChart(String title,
	                                                           List<Integer> xAxisData,
	                                                           String xAxisName,
	                                                           List<String> seriesNames,
	                                                           BiFunction<Integer, Integer, Integer> textLengthGenerator,
	                                                           BiFunction<Integer, Integer, Integer> keyLengthGenerator) {
		int minX = xAxisData.stream().min(Integer::compareTo).orElse(0);
		int maxX = xAxisData.stream().max(Integer::compareTo).orElse(0);
		int xTick = (maxX - minX) / (xAxisData.size() - 1);
		var xAxis = new NumberAxis(minX, maxX, xTick);
		xAxis.setLabel(xAxisName);
		var yAxis = new NumberAxis(0, 1, 0.1);
		yAxis.setLabel("Success Rate");
		var successRateChart = new LineChart<>(xAxis, yAxis);
		successRateChart.setTitle(title);
		successRateChart.getData()
				.addAll(generateSuccessRateData(xAxisData, seriesNames, textLengthGenerator, keyLengthGenerator));
		return successRateChart;
	}

	public List<LineChart<Number, Number>> run() throws IOException, URISyntaxException {
		if (textFile == null) {
			Path path = Paths.get(getClass().getClassLoader()
					.getResource(textFileName).toURI());
			textFile = Files.lines(path)
					.map(String::toUpperCase)
					.collect(Collectors.joining("\n"));
		}
		var defaultKeyLengthsNames =
				defaultSecretKeyLengths.stream().map(i -> String.format("key length %d", i)).toList();
		var successRateByTextLengthChart = generateSuccessRateChart(
				"Success rate by text length",
				textLengths,
				"Text length, chars",
				defaultKeyLengthsNames,
				(si, xi) -> textLengths.get(xi),
				(si, xi) -> defaultSecretKeyLengths.get(si));
		var defaultTextLengthsNames = defaultTextLengths.stream().map(i -> String.format("text length %d", i)).toList();
		var successRateBySecretKeyLengthChart = generateSuccessRateChart(
				"Success rate by secret key length",
				secretKeyLengths,
				"Key length, chars",
				defaultTextLengthsNames,
				(si, xi) -> defaultTextLengths.get(si),
				(si, xi) -> secretKeyLengths.get(xi));

		return List.of(successRateByTextLengthChart, successRateBySecretKeyLengthChart);
	}
}
