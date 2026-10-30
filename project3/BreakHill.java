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
        System.out.println("CRACKING REPORT");
        System.out.println("---------------");
        System.out.println("Method:");
        System.out.println("The program tests all 456,976 possible 2x2 keys modulo 26.");
        System.out.println("For each key, it calculates det = (a*d - b*c) mod 26.");
        System.out.println("If xgcd(det, 26) finds no modular inverse, that key is skipped.");
        System.out.println("This leaves 157,248 invertible keys to try.");
        System.out.println();
        System.out.println("For each valid key, findDecryptionKey() calculates its inverse,");
        System.out.println("and decrypt() recovers a candidate plaintext. The program counts");
        System.out.println("occurrences of these common English three-letter patterns:");
        System.out.println("  " + String.join(", ", common));
        System.out.println("The candidate with the highest count is selected.");
        System.out.println("Winning score: " + highestEnglishScore + " pattern occurrences.");
        System.out.println();
        System.out.println("Recovered message (spaces and punctuation restored manually):");
        System.out.println("I lived in solitude in the country and noticed how the monotony");
        System.out.println("of a quiet life stimulates the creative mind.");
        System.out.println();
        System.out.println("The final Z in the raw plaintext above appears to be padding.");
    }
}
