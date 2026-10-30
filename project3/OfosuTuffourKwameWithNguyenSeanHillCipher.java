/**
 * CS327 Project 3: 2x2 Hill Cipher.
 * Author: Kwame Ofosu-Tuffour & Sean Nguyen
 * Letters use A=0 through Z=25. Each pair is a column vector.
 */
public class OfosuTuffourKwameWithNguyenSeanHillCipher {
    private static final int MOD = 26;
    private static final int PADDING = 'Z' - 'A';

    private static int mod(int a, int m) {
        return Math.floorMod(a, m);
    }

    /* Extended Euclidean algorithm */
    public static int xgcd(int inE, int inZ) {
        if (inZ <= 1) {
            throw new IllegalArgumentException("Modulus must be greater than 1.");
        }
        int oldR = mod(inE, inZ);
        int r = inZ;
        long prevS = 1;
        long s = 0;
        while (r != 0) {
            int q = oldR / r;
            int nextR = oldR - q * r;
            oldR = r;
            r = nextR;
            long nextS = prevS - q * s;
            prevS = s;
            s = nextS;
        }
        return oldR == 1 ? (int) Math.floorMod(prevS, (long) inZ) : -1;
    }

    public static int[][] findDecryptionKey(int[][] encryptionKey) {
        validateKey(encryptionKey);

        int a = mod(encryptionKey[0][0], MOD);
        int b = mod(encryptionKey[0][1], MOD);
        int c = mod(encryptionKey[1][0], MOD);
        int d = mod(encryptionKey[1][1], MOD);
        int det = mod(a * d - b * c, MOD);
        int detInv = xgcd(det, MOD);
        if (detInv == -1) {
            throw new IllegalArgumentException(
                    "Key is not invertible modulo 26 (determinant = " + det + ").");
        }
        return new int[][] {
                { mod(detInv * d, MOD), mod(-detInv * b, MOD) },
                { mod(-detInv * c, MOD), mod(detInv * a, MOD) }
        };
    }

    public static int[] encrypt(int[] plaintext, int[][] encryptionKey) {
        validateMessage(plaintext);
        findDecryptionKey(encryptionKey); // Reject noninvertible keys.
        int[] padded = java.util.Arrays.copyOf(plaintext,
                plaintext.length + plaintext.length % 2);
        if (padded.length != plaintext.length) {
            padded[padded.length - 1] = PADDING;
        }
        return transform(padded, encryptionKey);
    }

    public static int[] decrypt(int[] ciphertext, int[][] decryptionKey) {
        validateMessage(ciphertext);
        findDecryptionKey(decryptionKey);
        if (ciphertext.length % 2 != 0) {
            throw new IllegalArgumentException("Ciphertext must contain complete pairs of letters.");
        }
        return transform(ciphertext, decryptionKey);
    }

    private static int[] transform(int[] message, int[][] key) {
        int a = mod(key[0][0], MOD), b = mod(key[0][1], MOD);
        int c = mod(key[1][0], MOD), d = mod(key[1][1], MOD);
        int[] result = new int[message.length];
        for (int i = 0; i < message.length; i += 2) {
            result[i] = mod(a * message[i] + b * message[i + 1], MOD);
            result[i + 1] = mod(c * message[i] + d * message[i + 1], MOD);
        }
        return result;
    }

    private static void validateKey(int[][] key) {
        if (key == null || key.length != 2 || key[0] == null || key[1] == null
                || key[0].length != 2 || key[1].length != 2) {
            throw new IllegalArgumentException("Key must be a 2x2 matrix.");
        }
    }

    private static void validateMessage(int[] message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        for (int value : message) {
            if (value < 0 || value >= MOD) {
                throw new IllegalArgumentException("Message values must be in 0..25 (A..Z).");
            }
        }
    }

    public static int[] toNumbers(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null.");
        }
        int[] result = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            if (letter >= 'a' && letter <= 'z') {
                letter = (char) (letter - 'a' + 'A');
            }
            if (letter < 'A' || letter > 'Z') {
                throw new IllegalArgumentException("Text must contain only letters A..Z.");
            }
            result[i] = letter - 'A';
        }
        return result;
    }

    public static String toLetters(int[] message) {
        validateMessage(message);
        StringBuilder result = new StringBuilder(message.length);
        for (int value : message) {
            result.append((char) ('A' + value));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        int[][] encryptionKey = { { 16, 7 }, { 9, 14 } };
        int[][] decryptionKey = findDecryptionKey(encryptionKey);
        System.out.println("Encryption key:");
        printKey(encryptionKey);
        System.out.println("Decryption key:");
        printKey(decryptionKey);
        System.out.println("Plaintext: JMUCSISCOOL");
        System.out.println("Padded plaintext: JMUCSISCOOLZ");
        System.out.println("Ciphertext: "
                + toLetters(encrypt(toNumbers("JMUCSISCOOL"), encryptionKey)));
        System.out.println("Given ciphertext: MQGVGQSMJI");
        System.out.println("Recovered cleartext: "
                + toLetters(decrypt(toNumbers("MQGVGQSMJI"), decryptionKey)));
    }

    private static void printKey(int[][] key) {
        for (int[] row : key) {
            System.out.printf("[%2d %2d]%n", row[0], row[1]);
        }
    }
}
