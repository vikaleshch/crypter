package com.bsuir.cryptor.core.provider;

public interface CryptoProvider {

    public byte[] encrypt(byte[] source);

    public byte[] decrypt(byte[] cipher);
}
