package lab1.cryptoanalysis;

public class UndetectedAlphabetCryptoAnalyzerException extends CryptoAnalyzerException{
	public UndetectedAlphabetCryptoAnalyzerException() {
		super("Alphabet cannot be detected");
	}
	public UndetectedAlphabetCryptoAnalyzerException(Throwable thr){
		super("Alphabet cannot be detected", thr);
	}
}
