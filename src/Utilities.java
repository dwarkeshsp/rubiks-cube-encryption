import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utilities {

    public static void writeMessage(String fileName, byte[] output) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(output);
        fos.close();
    }

    public static File getFile(String popUp) {
        JOptionPane.showMessageDialog(null, popUp);
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("File Chosen: " + chooser.getSelectedFile().getName());
        }
        return chooser.getSelectedFile();
    }

    public static void printArr(Sticker[][] arr) {
        System.out.println("Array");
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j].getItem());
            }
            System.out.println();
        }
    }

    public static byte[] readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] message = new byte[(int) file.length()];
        fis.read(message);
        fis.close();
        return message;
    }

    public static String intro() {
        return "How this works:\nYou're either about to scramble or unscrambe a message";
    }

}
