package com.hitsme.locker.app.core.data.crypto.AES;

/**
 * Created by 10093 on 2017/5/27.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CoreCrypto {
    private static final int KEY_SIZE_BITS = 256;
    private static final int KEY_SIZE_BYTES = 32;
    private static final int SALT_SIZE_BYTE = 32;

    public CoreCrypto() {
    }

    public static CoreCrypto.AES getCipher() throws Exception {
        CoreCrypto aes = new CoreCrypto();
        return aes.new AES();
    }

    public class AES {
        private static final String SECRET_KEY_ALGORITHM = "p【、";
        private static final String ALGORITHM = "AES";
        private static final String MODE = "CBC";
        private static final String PADDING = "PKCS5Padding";
        private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
        private SecretKey key;
        private byte[] keyInByte;
        public int IV_LENGTH_BYTE;

        public AES() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidParameterSpecException, InvalidKeyException, InvalidAlgorithmParameterException {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.IV_LENGTH_BYTE = cipher.getBlockSize();
        }

        public void makeKey() throws Exception {
            this.keyInByte = CoreCrypto.PBKDF2.genertateKey().getEncoded();
            this.key = new SecretKeySpec(this.keyInByte, "AES");
        }

        public OutputStream saveKey(String password, OutputStream out) throws IOException, GeneralSecurityException {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] salt = CoreCrypto.Utils.getRandomBytes(32);
            out.write(salt, 0, 32);
            byte[] ivBytes = CoreCrypto.Utils.getRandomBytes(this.IV_LENGTH_BYTE);
            out.write(ivBytes, 0, this.IV_LENGTH_BYTE);
            SecretKey keyFromUserPassword = CoreCrypto.PBKDF2.pbkdf2(password, salt, 1000);
            cipher.init(1, keyFromUserPassword, new IvParameterSpec(ivBytes));
            CipherOutputStream out1 = new CipherOutputStream(out, cipher);
            out1.write(this.keyInByte);
            return out1;
        }

        public InputStream loadKey(InputStream in, String password) throws GeneralSecurityException, IOException {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] salt = new byte[32];
            if(in.read(salt) < 32) {
                throw new IllegalArgumentException("Invalid file length (needs a full block for salt)");
            } else {
                byte[] ivBytes = new byte[this.IV_LENGTH_BYTE];
                if(in.read(ivBytes) < this.IV_LENGTH_BYTE) {
                    throw new IllegalArgumentException("Invalid file length (needs a full block for iv)");
                } else {
                    SecretKey keyFromUserPassword = CoreCrypto.PBKDF2.pbkdf2(password, salt, 1000);
                    cipher.init(2, keyFromUserPassword, new IvParameterSpec(ivBytes));
                    CipherInputStream cipherInputStream = new CipherInputStream(in, cipher);
                    this.keyInByte = new byte[32];
                    cipherInputStream.read(this.keyInByte);
                    this.key = new SecretKeySpec(this.keyInByte, "AES");
                    return cipherInputStream;
                }
            }
        }

        public Cipher getCiphertoEnc(OutputStream out) throws Exception {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivBytes = CoreCrypto.Utils.getRandomBytes(this.IV_LENGTH_BYTE);
            out.write(ivBytes, 0, this.IV_LENGTH_BYTE);
            cipher.init(1, this.key, new IvParameterSpec(ivBytes));
            return cipher;
        }

        public Cipher getCiphertoDec(InputStream in) throws Exception {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivBytes = new byte[this.IV_LENGTH_BYTE];
            if(in.read(ivBytes) < this.IV_LENGTH_BYTE) {
                throw new IllegalArgumentException("Invalid file length (needs a full block for iv)");
            } else {
                cipher.init(2, this.key, new IvParameterSpec(ivBytes));
                return cipher;
            }
        }
    }

    private static class Base64Coder {
        private static final char[] map1 = new char[64];
        private static final byte[] map2;

        static {
            int i = 0;

            char c;
            for(c = 65; c <= 90; map1[i++] = c++) {
                ;
            }

            for(c = 97; c <= 122; map1[i++] = c++) {
                ;
            }

            for(c = 48; c <= 57; map1[i++] = c++) {
                ;
            }

            map1[i++] = 43;
            map1[i++] = 47;
            map2 = new byte[128];

            for(i = 0; i < map2.length; ++i) {
                map2[i] = -1;
            }

            for(i = 0; i < 64; ++i) {
                map2[map1[i]] = (byte)i;
            }

        }

        public static char[] encode(byte[] in) {
            return encode(in, 0, in.length);
        }

        public static char[] encode(byte[] in, int iOff, int iLen) {
            int oDataLen = (iLen * 4 + 2) / 3;
            int oLen = (iLen + 2) / 3 * 4;
            char[] out = new char[oLen];
            int ip = iOff;
            int iEnd = iOff + iLen;

            for(int op = 0; ip < iEnd; ++op) {
                int i0 = in[ip++] & 255;
                int i1 = ip < iEnd?in[ip++] & 255:0;
                int i2 = ip < iEnd?in[ip++] & 255:0;
                int o0 = i0 >>> 2;
                int o1 = (i0 & 3) << 4 | i1 >>> 4;
                int o2 = (i1 & 15) << 2 | i2 >>> 6;
                int o3 = i2 & 63;
                out[op++] = map1[o0];
                out[op++] = map1[o1];
                out[op] = op < oDataLen?map1[o2]:61;
                ++op;
                out[op] = op < oDataLen?map1[o3]:61;
            }

            return out;
        }

        private Base64Coder() {
        }
    }

    private static class PBKDF2 {
        private PBKDF2() {
        }

        public static SecretKey genertateKey() throws Exception {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256);
            return kgen.generateKey();
        }

        public static SecretKey pbkdf2(String password, byte[] salt, int pbkdf2Iterations) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, pbkdf2Iterations, 256);
            byte[] keyBytes = keyFactory.generateSecret(pbeKeySpec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        }
    }

    public static class Utils {
        public Utils() {
        }

        public static byte[] getRandomBytes(int len) {
            SecureRandom ranGen = new SecureRandom();
            byte[] aesKey = new byte[len];
            ranGen.nextBytes(aesKey);
            return aesKey;
        }

        public static String byteArrayToBase64String(byte[] raw) {
            return new String(CoreCrypto.Base64Coder.encode(raw));
        }
    }
}

