package lab1.cryptoanalysis;

public interface CryptoAnalyzer {
	String getSecretKey(String encryptedText) throws CryptoAnalyzerException;
}
