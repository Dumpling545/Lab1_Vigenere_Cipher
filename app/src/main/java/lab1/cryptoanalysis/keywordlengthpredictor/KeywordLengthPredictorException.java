package lab1.cryptoanalysis.keywordlengthpredictor;

public class KeywordLengthPredictorException extends RuntimeException {
	public KeywordLengthPredictorException(String message) {
		super(message);
	}
	public KeywordLengthPredictorException(String message, Throwable thr) {
		super(message, thr);
	}
}
