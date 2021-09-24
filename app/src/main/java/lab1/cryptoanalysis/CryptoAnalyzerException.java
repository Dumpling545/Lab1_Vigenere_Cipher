package lab1.cryptoanalysis;

public class CryptoAnalyzerException extends RuntimeException {
	public CryptoAnalyzerException(String message) {
		super(message);
	}
	public CryptoAnalyzerException(String message, Throwable thr){
		super(message, thr);
	}
}
