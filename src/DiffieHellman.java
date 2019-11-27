import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman {
    private static final BigInteger p = new BigInteger(Utilities.diffieHellmanP());
    private static final BigInteger g = new BigInteger("2");
    private static String dhExplanation = Utilities.diffieHellmanExplanation();
    private BigInteger privateKey;
    private BigInteger sharedKey;
    private String username;

    public DiffieHellman(String username) throws IOException {
        this.username = username;
        JOptionPane.showMessageDialog(null, dhExplanation);
        privateKey = makePrivateKey();
        BigInteger publicKey = g.modPow(privateKey, p);
        Utilities.writeMessage(username + " Public Key.txt", publicKey.toString().getBytes());
        JOptionPane.showMessageDialog(null, "Your new public key has been saved as publicKey.txt" + "\nShare freely.");
        sharedKey = makeSharedKey();
        System.out.println(sharedKey.toString());
    }

    private static BigInteger readBigInteger(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes);
        fis.close();
        return new BigInteger(new String(bytes));
    }

    private BigInteger makePrivateKey() throws IOException {
        int hasPrivateKey = JOptionPane.showConfirmDialog(null, "Do you have a private key?", "",
                JOptionPane.YES_NO_OPTION);
        if (hasPrivateKey > 0) {
            privateKey = new BigInteger(8192, new Random());
            JOptionPane.showMessageDialog(null,
                    "Your new private key has been saved as privateKey.txt" + "\nDon't let anyone see!!! ");
            Utilities.writeMessage(username + " Private Key.txt", privateKey.toString().getBytes());
            return privateKey;
        } else {
            File privateKeyFile = Utilities.getFile("Please open the txt file that holds your private key.");
            return readBigInteger(privateKeyFile);
        }
    }

    private BigInteger makeSharedKey() throws IOException {
        int hasPublicKey = JOptionPane.showConfirmDialog(null, "Do you have the other guy's public key?", "",
                JOptionPane.YES_NO_OPTION);
        if (hasPublicKey > 0) {
            JOptionPane.showMessageDialog(null, "Come back once you have it, bucko.");
            System.exit(0);
        }
        File publicKeyFile = Utilities.getFile("Please open the text file that holds the other guy's public key.");
        BigInteger otherPublicKey = readBigInteger(publicKeyFile);
        return otherPublicKey.modPow(privateKey, p);
    }

    public BigInteger getSharedKey() {
        return sharedKey;
    }

    public static int[] arrayKey(String sKey) {
        int remainder = 2466 - sKey.length();
        if (remainder == -1) {
            sKey = sKey.substring(1);
        }
        while (remainder > 0) {
            sKey = '0' + sKey;
            remainder--;
        }
        if (sKey.length() != 2466) {
            throw new Error("Key is " + sKey.length() + " long instead of 2466.");
        }
        int[] key = new int[411];
        String currentInt = "";
        int keyIndex = 0;
        for (int i = 0; i < sKey.length(); i++) {
            if (i % 6 == 0 && i != 0) {
                key[keyIndex] = Integer.parseInt(currentInt);
                currentInt = "";
                keyIndex++;
            }
            currentInt += sKey.charAt(i);
        }
        key[keyIndex] = Integer.parseInt(currentInt);
        return key;
    }

    public int[] arrayKey() {
        String sKey = sharedKey.toString();
        return arrayKey(sKey);
    }

    public int[] reverseArrayKey() {
        return reverseKey(arrayKey());
    }

    public static int[] reverseKey(int[] key) {
        int[] reverse = new int[key.length];
        for (int i = 0; i < key.length; i++) {
            int keyI = key[key.length - i - 1];
            keyI = keyI % 2 == 0 ? keyI + 1 : keyI - 1;
            reverse[i] = keyI;
        }
        return reverse;
    }
}
