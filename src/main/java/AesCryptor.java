import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class AesCryptor {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16; // AES block size in bytes
    private static final int KEY_LENGTH = 16; // 128-bit key

    public static String fetchLethalGameSave() throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        final String password = "lcslime14a5";
        final String inputFilePath = Paths.get(System.getenv("LocalAppData"), "\\..\\LocalLow\\ZeekerssRBLX\\Lethal Company\\LCSaveFile2").toString();

        byte[] data = readFile(inputFilePath);
        data = decrypt(data, password);
        return new String(data);
    }

    public static void saveLethalGameSave(byte[] data) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        final String password = "lcslime14a5";
        final String outputFilePath = Paths.get(System.getenv("LocalAppData"), "\\..\\LocalLow\\ZeekerssRBLX\\Lethal Company\\LCSaveFile2").toString();

        byte[] encryptedData = encrypt(data, password);
        writeFile(outputFilePath, encryptedData);
    }

    private static byte[] readFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return fis.readAllBytes();
        }
    }

    private static void writeFile(String filePath, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    private static byte[] encrypt(byte[] data, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        byte[] iv = generateRandomIv();
        SecretKeySpec key = deriveKey(password, iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] encryptedData = cipher.doFinal(data);
        return concatenate(iv, encryptedData);
    }

    private static byte[] decrypt(byte[] data, String password) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] iv = Arrays.copyOfRange(data, 0, IV_LENGTH);
        byte[] encryptedData = Arrays.copyOfRange(data, IV_LENGTH, data.length);
        SecretKeySpec key = deriveKey(password, iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        return cipher.doFinal(encryptedData);
    }

    private static SecretKeySpec deriveKey(String password, byte[] iv) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), iv, 100, KEY_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }

    private static byte[] generateRandomIv() {
        byte[] iv = new byte[IV_LENGTH];
        new java.security.SecureRandom().nextBytes(iv);
        return iv;
    }

    private static byte[] concatenate(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static byte[] gzip(byte[] data) throws IOException {
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(data);
            return baos.toByteArray();
        }
    }

    private static byte[] gunzip(byte[] data) throws IOException {
        try (java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
             GZIPInputStream gzip = new GZIPInputStream(bais);
             java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzip.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
    }

    private static boolean isGzip(byte[] data) {
        return data.length > 1 && data[0] == (byte) 0x1F && data[1] == (byte) 0x8B;
    }
}
