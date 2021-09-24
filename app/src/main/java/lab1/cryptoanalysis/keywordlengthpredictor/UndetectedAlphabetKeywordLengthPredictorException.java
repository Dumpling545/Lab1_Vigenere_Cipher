package lab1.cryptoanalysis.keywordlengthpredictor;

public class UndetectedAlphabetKeywordLengthPredictorException extends KeywordLengthPredictorException {
	public UndetectedAlphabetKeywordLengthPredictorException() {
		super("Alphabet cannot be detected");
	}
	public UndetectedAlphabetKeywordLengthPredictorException(Throwable thr) {
		super("Alphabet cannot be detected", thr);
	}
}
