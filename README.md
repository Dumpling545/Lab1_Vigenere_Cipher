# Lab1_Vigenere_Cipher
Лабораторная работа 1, КБРС

[Отчёт по лабораторной в формате PDF](report.pdf)
Описание исходного кода:
- Определение языка текста (русский/английский) реализовано в классе [AlphabetDetector](app/src/main/java/lab1/helper/AlphabetDetector.java)
- Шифрование/Дешифрование по методу Виженера реализовано в классе [VigenereEncryptor](app/src/main/java/lab1/encryption/VigenereEncryptor.java)
- Криптоанализ шифротекста реализован в методе getSecretKey(String encryptedText) класса [VigenereCipherAnalyzer](app/src/main/java/lab1/cryptoanalysis/VigenereCipherAnalyzer.java)
  - Тест Касиски реализован в методе predictKeywordLength(String text) класса [KasiskiKeywordLengthPredictor](app/src/main/java/lab1/cryptoanalysis/keywordlengthpredictor/KasiskiKeywordLengthPredictor.java)
  - Частотный анализ шифра Цезаря реализован в методе getSecretKey(String encryptedText) класса [CaesarCipherAnalyzer](app/src/main/java/lab1/cryptoanalysis/CaesarCipherAnalyzer.java)
