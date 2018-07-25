package ml.echelon133.security.secret;

import java.security.SecureRandom;
import java.util.Random;


public class SecretGenerator implements ISecretGenerator {
    public static final String alphaUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String alphaLower = alphaUpper.toLowerCase();
    public static final String numeric = "0123456789";
    public static final String alphaNumeric = alphaUpper + alphaLower + numeric;
    private final Random random;
    private final char[] allSymbols = alphaNumeric.toCharArray();
    private final char[] buf;

    public SecretGenerator(int secretLength) {
        random = new SecureRandom();
        this.buf = new char[secretLength];
    }

    public String generateSecret() {
        for (int i = 0; i < buf.length; ++i) {
            buf[i] = allSymbols[random.nextInt(allSymbols.length)];
        }
        return new String(buf);
    }
}