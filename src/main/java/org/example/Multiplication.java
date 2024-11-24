package org.example;

public class Multiplication
{
    public static String encryptWithMultiplication(String plainText, int key)
    {
        StringBuilder encryptedText = new StringBuilder();
        for (char c: plainText.toCharArray()){
            int n=256;
            char encryptedChar = (char) ((c * key) % n);
            encryptedText.append(encryptedChar);
        }
        return encryptedText.toString();
    }

    public static String decryptWithMultiplication(String encryptedText, int key) {
        StringBuilder decryptedText = new StringBuilder();

        for (char c : encryptedText.toCharArray()) {
            int n = 256;
            char decryptedChar = (char) ((c * modInverse(key, n)) % n);
            decryptedText.append(decryptedChar);
        }
        return decryptedText.toString();
    }

    private static int modInverse(int a, int m)
    {
        a = a % m;
        for (int x = 1; x < m; x++)
        {
            if ((a * x) % m == 1)
                return x;
        }
        return 1;
    }
}
