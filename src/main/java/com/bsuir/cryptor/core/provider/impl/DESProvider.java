package com.bsuir.cryptor.core.provider.impl;

import com.bsuir.cryptor.core.provider.CryptoProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

public class DESProvider implements CryptoProvider {

    private static final String DES_ALGORITHM = "DES";

    private Cipher cipher;
    private SecretKey secretKey;
    private SecureRandom secureRandom;

    public DESProvider(SecureRandom secureRandom) {
        try {
            this.secureRandom = secureRandom;
            this.cipher = Cipher.getInstance(DES_ALGORITHM);

            KeyGenerator generator = KeyGenerator.getInstance(DES_ALGORITHM);
            generator.init(this.secureRandom);

            this.secretKey = generator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encrypt(byte[] source) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);

            return cipher.doFinal(source);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] decrypt(byte[] source) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);

            return cipher.doFinal(source);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
