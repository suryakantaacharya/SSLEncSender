package com.surya.configs;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class CryptoUtil {
	
    private static final String ALGORITHM = "RSA";
    private static final String PUBLIC_KEY_BASE64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAspQwzP1rYrgDQ0G6+TgtzA6JqO+44ZlhhevtrZcxfRKVPEd+cu88QpMw7sCI1evMoTNuhDjYDE0xqy6AIDdCUFWOnVx12QqeFWM+xQUNyggvId/yHkSt4GTkGttu7dOkyytYmwvzIJi5o/gFmTi9FX7jqV60mw+KOkgCdwhtvPMBBz8xCHCIWgbG9LTbXUczz0wm+RDBnjIgBxvs9/76ZgE4kr/426m5m6imPLPWF3lw/IS0ThlGQwNZzdgBTtkDSguGe0frIpp+ZT1PTkj8pZ1TS/u0ak1PnPeckSROPeJ3X2aIDhrE7ufqkYy4peQCzO8ZmfhxJTbXJIhBHaeWNwIDAQAB"; 
	
	public static byte[] encrypt(String data) throws Exception {

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		PublicKey publicKey = getPublicKey();

		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes());

		return encryptedBytes;

	}
	
    private static PublicKey getPublicKey() throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY_BASE64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

}
