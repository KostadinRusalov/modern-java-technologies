package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Rijndael implements SymmetricBlockCipher {
    private static final String CIPHER = "AES";
    private final SecretKey secretKey;

    public Rijndael(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            try (var encryptedOutput = new CipherOutputStream(outputStream, cipher)) {
                encryptedOutput.write(inputStream.readAllBytes());
            } catch (IOException e) {
                throw new CipherException("An I\\O exception occurred while trying to encrypt", e);
            }
        } catch (NoSuchPaddingException e) {
            throw new CipherException("Requested padding mechanism is unavailable", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CipherException("Requested algorithm is unavailable", e);
        } catch (InvalidKeyException e) {
            throw new CipherException("Provided secret key is invalid", e);
        }
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            try (var decryptedInput = new CipherInputStream(inputStream, cipher)) {
                outputStream.write(decryptedInput.readAllBytes());
            } catch (IOException e) {
                throw new CipherException("An I\\O exception occurred while trying to decrypt", e);
            }
        } catch (NoSuchPaddingException e) {
            throw new CipherException("Requested padding mechanism is unavailable", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CipherException("Requested algorithm is unavailable", e);
        } catch (InvalidKeyException e) {
            throw new CipherException("Provided secret key is invalid", e);
        }
    }
}
