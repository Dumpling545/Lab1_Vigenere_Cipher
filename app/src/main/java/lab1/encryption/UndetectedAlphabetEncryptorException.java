package lab1.encryption;

public class UndetectedAlphabetEncryptorException extends EncryptorException{

	public UndetectedAlphabetEncryptorException() {
		super("Alphabet cannot be detected");
	}
}
