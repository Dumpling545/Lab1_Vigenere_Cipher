package lab1.encryption;

public interface Encryptor {
	String encrypt(String raw, String encryptionKey) throws EncryptorException;
	String decrypt(String encrypted, String decryptionKey) throws EncryptorException;
}
