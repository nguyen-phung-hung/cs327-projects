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



}
