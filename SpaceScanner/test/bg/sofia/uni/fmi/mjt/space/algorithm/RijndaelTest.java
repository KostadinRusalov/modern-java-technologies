package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RijndaelTest {

    @Test
    public void testEncryptDecrypt() throws NoSuchAlgorithmException, CipherException {
        SecretKey secretKey = KeyGenerator.getInstance(Rijndael.ALGORITHM).generateKey();
        Rijndael rijndael = new Rijndael(secretKey);

        String message =
            "Oh, AES, you mean the digital equivalent of a high-security vault that's about as easy to crack " +
                "as teaching a goldfish to perform a Shakespearean monologue? AES, or Advanced Encryption Standard, is like a secret " +
                "language where you need a super-secret decoder ring (also known as a key) to understand it. " +
                "It's a symmetric key algorithm used to keep your digital secrets, well, secret. It scrambles your data into an " +
                "indecipherable mess unless you have the right key to unscramble it. It's like a magical puzzle box for your data, " +
                "except it's all math and no actual magic. In short, it's the digital lock that keeps your cat videos safe from prying eyes";

        InputStream inputStream = new ByteArrayInputStream(message.getBytes());
        ByteArrayOutputStream encryptedOutput = new ByteArrayOutputStream();

        rijndael.encrypt(inputStream, encryptedOutput);

        InputStream encryptedInput = new ByteArrayInputStream(encryptedOutput.toByteArray());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        rijndael.decrypt(encryptedInput, outputStream);

        assertEquals(message, outputStream.toString(), "Message should be the same after decrypting the encryption");
    }

    @Test
    public void testEncryptInvalidKey() {
        assertThrows(CipherException.class,
            () -> new Rijndael(null).encrypt(new ByteArrayInputStream("".getBytes()), new ByteArrayOutputStream()),
            "Encrypting with invalid key should throw cipher exception");
    }

    @Test
    public void testDecryptInvalidKey() {
        assertThrows(CipherException.class,
            () -> new Rijndael(null).decrypt(new ByteArrayInputStream("".getBytes()), new ByteArrayOutputStream()),
            "Decrypting with invalid key should throw cipher exception");
    }
}
