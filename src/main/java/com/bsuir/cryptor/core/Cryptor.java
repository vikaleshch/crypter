package com.bsuir.cryptor.core;

import com.bsuir.cryptor.core.provider.CryptoProvider;
import com.bsuir.cryptor.core.provider.impl.DESProvider;
import com.bsuir.cryptor.core.provider.impl.RC4Provider;
import com.bsuir.cryptor.core.provider.impl.BlowfishProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class Cryptor {

    private static final List<String> DECRYPT_MODES = asList("-d", "--decrypt");
    private static final List<String> SUPPORTED_ALGORITHMS = asList("RC4", "BLOWFISH", "DES");

    public static void main(String... args) throws IOException {
        final boolean decryptMode = stream(args)
                .anyMatch(DECRYPT_MODES::contains);
        final Algorithm algorithm = Algorithm.valueOf(stream(args)
                .filter(arg -> arg.startsWith("--algorithm") || arg.startsWith("-a"))
                .findFirst()
                .map(arg -> arg.split("=")[1].toUpperCase())
                .filter(SUPPORTED_ALGORITHMS::contains)
                .orElse(SUPPORTED_ALGORITHMS.get(0)));
        final String source = stream(args)
                .filter(arg -> arg.startsWith("--source") || arg.startsWith("-s"))
                .findFirst()
                .map(arg -> arg.split("=")[1].replace("/", "\\"))
                .orElseThrow(() -> new IllegalArgumentException("Исходный файл не задан"));
        if (!Files.isReadable(Paths.get(source))) {
            throw new IllegalArgumentException("Исходный файл нечитаем");
        }

        System.out.println("Начинается " + (decryptMode ? "дешифрование" : "шифрование") + " файла " + source + " с помощью алгоритма " + algorithm.name());

        final String output = source.substring(0, source.lastIndexOf(File.separator)) + "/result.crypto";
        final byte[] content = Files.readAllBytes(Paths.get(source));

        byte[] result = decryptMode ? algorithm.decrypt(content) : algorithm.encrypt(content);
        Files.write(Paths.get(output), result);

        System.out.println((decryptMode ? "Дешифрование" : "Шифрование") + " файла закончено. Результат в файле result.crypto");
    }

    private static SecureRandom secureRandom() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(securityKey().getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return random;
    }

    private static String securityKey() {
        return "Very secured key";
    }

    public enum Algorithm {
        RC4(new RC4Provider(secureRandom())),
        BLOWFISH(new BlowfishProvider(secureRandom())),
        DES(new DESProvider(secureRandom()));

        private CryptoProvider provider;

        Algorithm(CryptoProvider provider) {
            this.provider = provider;
        }

        public byte[] decrypt(byte[] source) {
            return provider.decrypt(source);
        }

        public byte[] encrypt(byte[] source) {
            return provider.encrypt(source);
        }
    }
}
