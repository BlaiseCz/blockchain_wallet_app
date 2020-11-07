package com.wallet.blockchain_wallet.client.signature;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SignatureApplier {

    @SneakyThrows
    public static String applySignature(PrivateKey privateKey, String input) {
        Signature dsa = Signature.getInstance("SHA1withRSA");
        dsa.initSign(privateKey);
        dsa.update(input.getBytes());
        byte[] output = dsa.sign();

        return Base64.getEncoder().encodeToString(output);
    }
}
