import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Cracker {
    private final static byte BASE = 36;
    private final static char[] digitToChar = new char[BASE];

    public Cracker() {
        for (int i = 0; i < 26; ++i) {
            digitToChar[i] = (char) (i + 'a');
        }
        for (int i = 0; i < 10; ++i) {
            digitToChar[i + 26] = (char) (i + '0');
        }
    }

    private static String mask_to_string(long mask, int size) {
        StringBuilder result = new StringBuilder();
        while (size-- > 0) {
            int digit = (int) (mask % BASE);
            result.append(digitToChar[digit]);
            mask /= (long) BASE;
        }
        return result.toString();
    }

    private static String get_hash(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hash = bigInt.toString(16);
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < 32 - hash.length(); ++i) {
            ret.append("0");
        }
        ret.append(hash);
        return ret.toString();
    }

    public String brute_force(long start, long end, int size, String key) throws NoSuchAlgorithmException {
        for (long i = start; i <= end; ++i) {
            String password = mask_to_string(i, size);
            String hashText = get_hash(password);
            if (key.equals(hashText))  {
                return password;
            }
        }
        return null;
    }
}