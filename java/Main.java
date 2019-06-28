import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {

    static File file;
    static DiffieHellman dh;
    static byte[] message;

    public static void main(String[] args) throws IOException {
        JOptionPane.showMessageDialog(null, Utilities.intro());
        String username = JOptionPane.showInputDialog("Username:\n(Just to help name files)");

        String[] possibleValues = {"Encrypt", "Decrypt"};
//        String eOrD = "d";
        String eOrD = (String) JOptionPane.showInputDialog(null, "Encrypt or Decrypt?", "Mode", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
        file = Utilities.getFile("Pick the message\nIf you don't pick a .txt file, you'll get hieroglyphics");
//        file = new File("C:\\Users\\dwark\\IdeaProjects\\Rubik's Cube Encryption\\Rubik's Encrypted message.txt");
//        file = new File("C:\\Users\\dwark\\IdeaProjects\\Rubik's Cube Encryption\\message.txt");
        dh = new DiffieHellman(username);
        message = Utilities.readFile(file);

        if (eOrD == "Encrypt") {
            System.out.println("E");
            encrypt();
        } else {
            System.out.println("D");
            decrypt();
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
