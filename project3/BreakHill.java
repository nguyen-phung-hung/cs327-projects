/**
 *
 * Bonus Question - Kwame, Sean
 *
 *
 *
 */



public class BreakHill {
    public static void main(String[] args) {
        String ct = "HDHNXZHFGWVKPOBOHFCBKYWGATXUNNOXJFUUXZLUDXLKECNOPINYJRAQYOXPVKTMTBUELRNTKOCBKYXANTHNKIHFOJ";
        int[] cipher = OfosuTuffourKwameWithNguyenSeanHillCipher.toNumbers(ct);
        String[] common = { "THE", "AND", "ING", "ENT", "ION", "HER", "FOR", "THA" };

        int highestEnglishScore = -1;
        int[][] crackedEncKey = null;
        String crackedPlaintext = "";

        for (int a = 0; a < 26; a++)
            for (int b = 0; b < 26; b++)
                for (int c = 0; c < 26; c++)
                    for (int d = 0; d < 26; d++) {
                        int det = ((a * d - b * c) % 26 + 26) % 26;
                        if (OfosuTuffourKwameWithNguyenSeanHillCipher.xgcd(det, 26) == -1) {
                            continue;
                        }
                        int[][] enc = { { a, b }, { c, d } };
                        int[][] dec = OfosuTuffourKwameWithNguyenSeanHillCipher.findDecryptionKey(enc);
                        String candidateText = OfosuTuffourKwameWithNguyenSeanHillCipher.toLetters(OfosuTuffourKwameWithNguyenSeanHillCipher.decrypt(cipher, dec));
                        int englishScore = 0;
                        for (String t : common) {
                            for (int i = candidateText.indexOf(t); i >= 0; i = candidateText.indexOf(t, i + 1)) {
                                englishScore++;
                            }
                        }
                        if (englishScore > highestEnglishScore) {
                            highestEnglishScore = englishScore;
                            crackedEncKey = enc;
                            crackedPlaintext = candidateText;
                        }
                    }

        int[][] crackedDecKey = OfosuTuffourKwameWithNguyenSeanHillCipher.findDecryptionKey(crackedEncKey);

        System.out.println("Encryption key:");
        System.out.println("[ " + crackedEncKey[0][0] + "  " + crackedEncKey[0][1] + " ]");
        System.out.println("[ " + crackedEncKey[1][0] + "  " + crackedEncKey[1][1] + " ]");
        System.out.println("Decryption key:");
        System.out.println("[ " + crackedDecKey[0][0] + "  " + crackedDecKey[0][1] + " ]");
        System.out.println("[ " + crackedDecKey[1][0] + "  " + crackedDecKey[1][1] + " ]");
        System.out.println("Plaintext: " + crackedPlaintext);
        System.out.println();
        System.err.println(" I wrote a Java program that tests every possible key integers mod 26. A key is usable only if its determinant is coprime to 26, so for each candidate the program computes det = (ad − bc) mod 26 and skips the key when my xgcd() method finds no modular inverse. That leaves around 157,000? invertible keys out of 26⁴. For each remaining key, the program builds the decryption key with findDecryptionKey(), decrypts the whole ciphertext, and scores the result by counting common English letters in 3 words. The key found is [[9, 13], [12, 1]], with decryption key [[3, 13], [16, 1]]. It recovers the plaintext “I lived in solitude in the country and noticed how the monotony of a quiet life stimulates the creative mind”, followed by a Z pad letter");
    }
}
