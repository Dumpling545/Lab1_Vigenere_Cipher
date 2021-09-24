package lab1.cryptoanalysis.keywordlengthpredictor;

public interface KeywordLengthPredictor {
	int predictKeywordLength(String text) throws KeywordLengthPredictorException;
}
