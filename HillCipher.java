/**
 * CS327 Project: 2x2 Hill Cipher
 * Author: Kwame Ofosu-Tuffour
 *
 *
 *
 */
public class HillCipher {

    private static final int MOD = 26;

    /** Returns a mod m as a value from 0 to m-1. */
    private static int mod(int a, int m) {
        return ((a % m) + m) % m;
    }

    /** Returns the inverse of inE mod inZ, or -1 if there is none. */
    public static int xgcd(int inE, int inZ) {
        // oldR, r - the running remainder pair, same values gcd() would work
        // once r hits 0, oldR holds gcd(inE, inZ)
        // prevS, s - the running s-coefficient pair from the slides' table
        // (s_i * a + t_i * b = d_i); we only need s here, since s
        // ends up being the inverse of inE mod inZ, so t is never tracked
        int oldR = inE;
        int r = inZ;
        int prevS = 1;
        int s = 0;

        while (r != 0) {
            int q = oldR / r;

            int tempR = r;
            r = oldR - q * r;
            oldR = tempR;

            int tempS = s;
            s = prevS - q * s;
            prevS = tempS;
        }

        // oldR now holds gcd(inE, inZ)
        if (oldR != 1) {
            return -1;
        }

        int inverse = prevS % inZ;
        if (inverse < 0) {
            inverse += inZ;
        }

        return inverse;
    }

    /** Finds the decryption key from the encryption key. */
    public static int[][] findDecryptionKey(int encryptionKey[][]) {
        int a = encryptionKey[0][0], b = encryptionKey[0][1];
        int c = encryptionKey[1][0], d = encryptionKey[1][1];

        int det = mod(a * d - b * c, MOD);
        int detInv = xgcd(det, MOD);
        if (detInv == -1) {
            throw new IllegalArgumentException("Key is not invertible mod 26 (det = " + det + ")");
        }

        int[][] dec = new int[2][2];
        dec[0][0] = mod(detInv * d, MOD);
        dec[0][1] = mod(detInv * -b, MOD);
        dec[1][0] = mod(detInv * -c, MOD);
        dec[1][1] = mod(detInv * a, MOD);
        return dec;
    }

    /** Multiplies the key by each pair of letters. */
    private static int[] applyKey(int[] message, int[][] key) {
        int[] out = new int[message.length];
        for (int i = 0; i < message.length; i += 2) {
            int x = message[i], y = message[i + 1];
            out[i]     = mod(key[0][0] * x + key[0][1] * y, MOD);
            out[i + 1] = mod(key[1][0] * x + key[1][1] * y, MOD);
        }
        return out;
    }

    /** Encrypts a message, padding with Z if the length is odd. */
    public static int[] encrypt(int plaintext[], int encryptionKey[][]) {
        int[] p = plaintext;
        if (p.length % 2 != 0) {
            p = new int[plaintext.length + 1];
            System.arraycopy(plaintext, 0, p, 0, plaintext.length);
            p[p.length - 1] = 'Z' - 'A';
        }
        return applyKey(p, encryptionKey);
    }

    /** Decrypts a message with the decryption key. */
    public static int[] decrypt(int ciphertext[], int decryptionKey[][]) {
        if (ciphertext.length % 2 != 0) {
            throw new IllegalArgumentException("Hill ciphertext length must be even.");
        }
        return applyKey(ciphertext, decryptionKey);
    }

    /** Converts letters to numbers */
    public static int[] toInts(String s) {
        String clean = s.toUpperCase().replaceAll("[^A-Z]", "");
        int[] out = new int[clean.length()];
        for (int i = 0; i < clean.length(); i++) {
            out[i] = clean.charAt(i) - 'A';
        }
        return out;
    }

    /** Converts numbers back to letters. */
    public static String toLetters(int[] nums) {
        StringBuilder sb = new StringBuilder();
        for (int n : nums) {
            sb.append((char) ('A' + n));
        }
        return sb.toString();
    }

    /** Formats a 2x2 matrix for printing. */
    private static String matrixToString(int[][] m) {
        return "[ " + m[0][0] + "  " + m[0][1] + " ]\n[ " + m[1][0] + "  " + m[1][1] + " ]";
    }

    /** Runs the required tests and prints the results. */
    public static void main(String[] args) {
        int[][] encryptionKey = { { 16, 7 }, { 9, 14 } };

        //Decryption key
        int[][] decryptionKey = findDecryptionKey(encryptionKey);
        System.out.println("Encryption key:");
        System.out.println(matrixToString(encryptionKey));
        System.out.println("Decryption key:");
        System.out.println(matrixToString(decryptionKey));
        System.out.println();

        // Encrypt JMUCSISCOOL
        String plaintext = "JMUCSISCOOL";
        String cipher = toLetters(encrypt(toInts(plaintext), encryptionKey));
        System.out.println("Plaintext:  " + plaintext);
        System.out.println("Ciphertext: " + cipher);
        System.out.println();

        //Decrypt MQGVGQSMJI
        String ciphertext = "MQGVGQSMJI";
        String cleartext = toLetters(decrypt(toInts(ciphertext), decryptionKey));
        System.out.println("Cipher text: " + ciphertext);
        System.out.println("Clear text:  " + cleartext);
        System.out.println();




    }
}
