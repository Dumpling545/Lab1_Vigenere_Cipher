package lab1.helper;

import javafx.scene.control.TextFormatter;

public class UpperCaseTextFormatter<V> extends TextFormatter<V> {
	public UpperCaseTextFormatter() {
		super((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		});
	}
}
