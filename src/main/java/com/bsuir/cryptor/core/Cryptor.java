package com.bsuir.cryptor.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class Cryptor {

    private static final List<String> MODES = asList("-e", "-d", "--encrypt", "--decrypt");
    private static final List<String> ALGORITHMS = asList("RC4", "SHA", "DES");

    public static void main(String... args) throws IOException {
        final String режим = stream(args)
                .filter(MODES::contains)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Режим работы программы не задан"));
        final String алгоритм = stream(args)
                .filter(arg -> arg.startsWith("--algorithm") || arg.startsWith("-a"))
                .findFirst()
                .map(arg -> arg.split("=")[1].toUpperCase())
                .filter(ALGORITHMS::contains)
                .orElse(ALGORITHMS.get(0));
        final String исходныйФайл = stream(args)
                .filter(arg -> arg.startsWith("--source") || arg.startsWith("-s"))
                .findFirst()
                .map(arg -> arg.split("=")[1])
                .orElseThrow(() -> new IllegalArgumentException("Исходный файл не задан"));
        final String папкаРезультата = stream(args)
                .filter(arg -> arg.startsWith("--output") || arg.startsWith("-o"))
                .findFirst()
                .map(arg -> arg.split("=")[1])
                .orElse(исходныйФайл.substring(0, исходныйФайл.lastIndexOf("/")));
        byte[] содержимое = Files.readAllBytes(Paths.get(исходныйФайл));

        // do crypto magic
    }
}
