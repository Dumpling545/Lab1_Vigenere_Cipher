package lab1.encryption;

import lab1.helper.Alphabet;
import lab1.helper.AlphabetDetector;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Value
@Component
public class VigenereEncryptor implements Encryptor {
	AlphabetDetector alphabetDetector;

	private String toggleEncryption(String text, String key, boolean encrypt){
		Optional<Alphabet> alphabetOptional = alphabetDetector.detect(List.of(text, key));
		Alphabet alphabet = alphabetOptional.orElseThrow(UndetectedAlphabetEncryptorException::new);
		StringBuilder encrypted = new StringBuilder();
		List<Character> alphabetList = alphabet.getAlphabetList();
		Map<Character, Integer> charToAlphabetIndexMap = alphabet.getCharToAlphabetIndexMap();
		for(int i = 0; i < text.length(); i++){
			if(Alphabet.getIgnoredSymbolsSet().contains(text.charAt(i))){
				encrypted.append(text.charAt(i));
				continue;
			}
			int textCharAlphaIndex = charToAlphabetIndexMap.get(text.charAt(i));
			int keyCharAlphaIndex = charToAlphabetIndexMap.get(key.charAt(i % key.length()));
			int delta = encrypt ? keyCharAlphaIndex : alphabetList.size() - keyCharAlphaIndex;
			int encryptedCharAlphaIndex = (textCharAlphaIndex + delta) % alphabetList.size();
			encrypted.append(alphabetList.get(encryptedCharAlphaIndex));
		}
		return encrypted.toString();
	}
	@Override
	public String encrypt(String raw, String encryptionKey) {
		return toggleEncryption(raw, encryptionKey, true);
	}

	@Override
	public String decrypt(String encrypted, String decryptionKey) {
		return toggleEncryption(encrypted, decryptionKey, false);
	}
}
