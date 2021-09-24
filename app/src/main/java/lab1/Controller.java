package lab1;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lab1.cryptoanalysis.CryptoAnalyzer;
import lab1.cryptoanalysis.UndetectedAlphabetCryptoAnalyzerException;
import lab1.encryption.Encryptor;
import lab1.encryption.UndetectedAlphabetEncryptorException;
import lab1.helper.UpperCaseTextFormatter;
import lab1.report.ReportRunner;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class Controller {
	@FXML
	private Button encryptText;
	@FXML
	private Button decryptText;
	@FXML
	private TextField derivedKey;
	@FXML
	private Button analyze;
	@FXML
	private TextArea analyzedText;
	@FXML
	private TextArea encryptedText;
	@FXML
	private TextArea originalText;
	@FXML
	private TextField secretKey;
	@FXML
	private Button runTests;
	@FXML
	private HBox reportHBox;

	private final Encryptor encryptor;
	private final CryptoAnalyzer analyzer;
	private final ReportRunner reportRunner;

	@FXML
	public void initialize() {
		encryptedText.setTextFormatter(new UpperCaseTextFormatter<>());
		originalText.setTextFormatter(new UpperCaseTextFormatter<>());
		secretKey.setTextFormatter(new UpperCaseTextFormatter<>());
		analyzedText.setTextFormatter(new UpperCaseTextFormatter<>());
		derivedKey.setTextFormatter(new UpperCaseTextFormatter<>());
		encryptText.setOnAction(event -> {
			try {
				encryptedText.setText(encryptor.encrypt(originalText.getText(), secretKey.getText()));
			} catch (UndetectedAlphabetEncryptorException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Mixed or undetected alphabet");
				alert.show();
			}
		});
		decryptText.setOnAction(event -> {
			try {
				originalText.setText(encryptor.decrypt(encryptedText.getText(), secretKey.getText()));
			} catch (UndetectedAlphabetEncryptorException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Mixed or undetected alphabet");
				alert.show();
			}
		});
		analyze.setOnAction(event -> {
			try {
				String key = analyzer.getSecretKey(analyzedText.getText());
				derivedKey.setText(key);
			} catch (UndetectedAlphabetCryptoAnalyzerException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Mixed or undetected alphabet");
				alert.show();
			}
		});
		runTests.setOnAction(event -> {
			try {
				List<LineChart<Number, Number>> lineCharts = reportRunner.run();
				reportHBox.getChildren().clear();
				lineCharts.stream()
						.peek(lc -> lc.setMinWidth(reportHBox.getPrefWidth() / 2))
						.forEach(lc -> reportHBox.getChildren().add(lc));
			} catch (Exception ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid File: " + ex.getMessage());
				alert.show();
			}
		});
	}
}
