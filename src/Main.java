import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    static File file;
    static DiffieHellman dh;
    static byte[] message;

    public static void main(String[] args) throws IOException {
        if (demo()) {
            return;
        }
        JOptionPane.showMessageDialog(null, Utilities.intro());
        String username = JOptionPane.showInputDialog("Username:\n(Just to help name files)");
        String[] possibleValuesEOrD = { "Encrypt", "Decrypt" };
        String eOrD = (String) JOptionPane.showInputDialog(null, "Encrypt or Decrypt?", "Mode",
                JOptionPane.INFORMATION_MESSAGE, null, possibleValuesEOrD, possibleValuesEOrD[0]);
        file = Utilities.getFile("Pick the message\nIf you don't pick a .txt file, you'll get hieroglyphics");
        message = Utilities.readFile(file);
        dh = new DiffieHellman(username);

        if (eOrD == "Encrypt") {
            System.out.println("E");
            encrypt();
        } else {
            System.out.println("D");
            decrypt();
        }
    }

    private static boolean demo() throws IOException {
        String[] possibleValuesDemoOrNo = { "Demo", "Real" };
        String demoOrNo = (String) JOptionPane.showInputDialog(null,
                "Do you just want to see a quick demo or do you want to encrypt or decrypt your own message?", "Mode",
                JOptionPane.INFORMATION_MESSAGE, null, possibleValuesDemoOrNo, possibleValuesDemoOrNo[0]);
        if (demoOrNo) {
            URL path = Main.class.getResource("demoMessage.txt");
            file = new File(path.getFile());
            message = Utilities.readFile(file);
            path = Main.class.getResource("myFile.txt");
            File keyFile = new File(path.getFile());
            String key = new String(Utilities.readFile(keyFile));
            Cube cube = new Cube(message, dh.arrayKey(), "Demo");
            return true;
        }
        else {
            return false;
        }
    }

    public static void encrypt() throws IOException {
        Cube cube = new Cube(message, dh.arrayKey(), "Encrypting");
        message = cube.cubeToArray();
        VigenereGenerator vigenereGenerator = new VigenereGenerator(dh.getSharedKey().toString(), message);
        vigenereGenerator.encode(file.getName());
    }

    public static void decrypt() throws IOException {
        VigenereGenerator vigenereGenerator = new VigenereGenerator(dh.getSharedKey().toString(), message);
        message = vigenereGenerator.decode();
        Cube cube = new Cube(message, dh.reverseArrayKey(), "Decrypting");
        message = cube.concatenatedCubeToArray();
        String fileName = file.getName();
        if (fileName.substring(0, 18).equals("Rubik's Encrypted ")) {
            fileName = fileName.substring(18);
        }
        fileName = "Rubik's Decrypted " + fileName;
        Utilities.writeMessage(fileName, message);
    }


}
