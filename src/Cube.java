import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Cube {

    private int size;
    private Sticker[][] message;
    private int[] key;
    private GridGUI gridGUI;

    public Cube(byte[] message, int[] key, String eOrD) {
        size = (int) Math.ceil(Math.sqrt((double) message.length / 6.0));
        size = size < 3 ? 3 : size;
        this.key = key;
        createCube(message);
        String[] possibleValues = {"Show the cube (Looks really cool plus it's kinda the whole point)", "Don't show (If your message is really really big)"};
        String showOrNot = (String) JOptionPane.showInputDialog(null, "Do you wanna watch the cube being encrypted?", "Show Cube?", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
        if (showOrNot == possibleValues[0]) {
            initiateShown(eOrD);
        } else {
            initiateHidden(eOrD);
        }
    }

    //delete
    public void changeKeyAndContinue(int[] key) {
        this.key = key;
        initiateShown("Decoding");
    }

    private void initiateHidden(String eOrD) {
        for (int k : key) {
            twistCube(k);
        }
    }

    private void initiateShown(String eOrD) {
        gridGUI = new GridGUI(message, eOrD);
        int speed = getSpeed();
        JOptionPane.showMessageDialog(null, "Start " + eOrD);
        for (int k : key) {
            twistCube(k);
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getSpeed() {
        String[] possibleValues = {"As Fast as Possible (So fast you won't see it)", "Fast", "Medium", "Slow", "Waiting for the end of times"};
        String speedString = (String) JOptionPane.showInputDialog(null, "How fast should the cube twist?", "Speed", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[1]);
        switch (speedString) {
            case "As Fast as Possible (So fast you won't see it)":
                return 0;
            case "Fast":
                return 50;
            case "Medium":
                return 250;
            case "Slow":
                return 1000;
            case "Waiting for the end of times":
                return 5000;
            default:
                throw new Error("No speed caught for " + speedString);
        }
    }

    private void createCube(byte[] gMes) {
        byte[] givenMes = generateFullArray(gMes);
        message = new Sticker[size * 3][size * 4];
        int gIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                message[i][j + size] = new Sticker(givenMes[gIndex], i, j + size, size);
                gIndex++;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * 4; j++) {
                message[i + size][j] = new Sticker(givenMes[gIndex], i + size, j, size);
                gIndex++;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                message[i + (size * 2)][j + size] = new Sticker(givenMes[gIndex], i + (size * 2), j + size, size);
                gIndex++;
            }
        }
        //fill null stickers
        for (int i = 0; i < message.length; i++) {
            for (int j = 0; j < message[0].length; j++) {
                if (message[i][j] == null) {
                    message[i][j] = new Sticker(' ', Color.BLACK);
                }
            }
        }
    }

    private void twistCube(int k) {
        int totalTypes = 6 * (size / 2) * 2;
        k %= totalTypes;
        //type = 0 to 5
        int type = k / ((size / 2) * 2);

        int mag = 1 + (k / 2) % (size / 2);
        int inverse = k % 2;
        //turn three times to get inverse
        int repeat = inverse == 1 ? 3 : 1;
        while (repeat != 0) {
            twistSwitch(type, mag);
            repeat--;
        }
    }

    private void twistSwitch(int type, int mag) {
        switch (type) {
            case 0:
                System.out.println("front " + mag);
                frontTwist(mag);
                break;
            case 1:
                System.out.println("right " + mag);
                rightTwist(mag);
                break;
            case 2:
                System.out.println("up " + mag);
                upTwist(mag);
                break;
            case 3:
                System.out.println("back " + mag);
                backTwist(mag);
                break;
            case 4:
                System.out.println("left " + mag);
                leftTwist(mag);
                break;
            case 5:
                System.out.println("down " + mag);
                downTwist(mag);
                break;
            default:
                throw new Error("Type not caught. Type: " + type);
        }
    }

    private void frontTwist(int mag) {
        rotate(size - mag, size - mag, size + 2 * mag);
    }

    private void rightTwist(int mag) {
        rotate(size, 2 * size, size);
        Sticker[][] whiteReverse = new Sticker[size][mag];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                whiteReverse[i][j] = message[size - 1 - i][2 * size - 1 - j];
            }
        }
        Sticker[][] blueReverse = new Sticker[size][mag];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                blueReverse[i][j] = message[2 * size - 1 - i][3 * size + mag - j - 1];
            }
        }
        for (int i = 0; i < 2 * size; i++) {
            for (int j = 2 * size - mag; j < 2 * size; j++) {
                updateMessage(i, j, message[i + size][j]);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                updateMessage(i + size, j + 3 * size, whiteReverse[i][j]);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                updateMessage(i + 2 * size, j + 2 * size - mag, blueReverse[i][j]);
            }
        }
    }

    private void upTwist(int mag) {
        rotate(0, size, size);
        Sticker[][] orange = new Sticker[mag][size];
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                orange[i][j] = message[size + i][j];
            }
        }
        for (int i = size; i < size + mag; i++) {
            for (int j = 0; j < 3 * size; j++) {
                updateMessage(i, j, message[i][j + size]);
            }
        }
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                updateMessage(size + i, j + 3 * size, orange[i][j]);
            }
        }
    }

    private void backTwist(int mag) {
        rotate(size, 3 * size, size);
        Sticker[][] white = new Sticker[mag][size];
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                white[i][j] = message[i][j + size];
            }
        }
        Sticker[][] orange = new Sticker[size][mag];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                orange[i][j] = message[i + size][j];
            }
        }
        Sticker[][] whiteRotate = rotateCounterArray(white);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                updateMessage(i + size, j, whiteRotate[i][j]);
            }
        }
        Sticker[][] yellow = new Sticker[mag][size];
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                yellow[i][j] = message[3 * size - mag + i][size + j];
            }
        }
        Sticker[][] orangeRotate = rotateCounterArray(orange);
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                updateMessage(3 * size - mag + i, size + j, orangeRotate[i][j]);
            }
        }
        Sticker[][] red = new Sticker[size][mag];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                red[i][j] = message[i + size][3 * size - mag + j];
            }
        }
        Sticker[][] yellowRotate = rotateCounterArray(yellow);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                updateMessage(i + size, 3 * size - mag + j, yellowRotate[i][j]);
            }
        }
        Sticker[][] redRotate = rotateCounterArray(red);
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                updateMessage(i, j + size, redRotate[i][j]);
            }
        }
    }

    private void leftTwist(int mag) {
        rotate(size, 0, size);
        Sticker[][] yellowReverse = new Sticker[size][mag];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                yellowReverse[i][j] = message[3 * size - 1 - i][size + mag - 1 - j];
            }
        }

        Sticker[][] blueReverse = new Sticker[size][mag];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                blueReverse[i][j] = message[2 * size - 1 - i][4 * size - j - 1];
            }
        }
        for (int i = 3 * size - 1; i >= size; i--) {
            for (int j = size; j < size + mag; j++) {
                updateMessage(i, j, message[i - size][j]);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                updateMessage(i + size, j + 4 * size - mag, yellowReverse[i][j]);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mag; j++) {
                updateMessage(i, j + size, blueReverse[i][j]);
            }
        }
    }

    private void downTwist(int mag) {
        rotate(2 * size, size, size);
        Sticker[][] blue = new Sticker[mag][size];
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                blue[i][j] = message[2 * size - mag + i][3 * size + j];
            }
        }
        for (int i = 2 * size - mag; i < 2 * size; i++) {
            for (int j = 4 * size - 1; j >= size; j--) {
                updateMessage(i, j, message[i][j - size]);
            }
        }
        for (int i = 0; i < mag; i++) {
            for (int j = 0; j < size; j++) {
                updateMessage(2 * size - mag + i, j, blue[i][j]);
            }
        }
    }

    private void rotate(int i, int j, int scope) {
        Sticker[][] rotation = new Sticker[scope][scope];
        for (int n = 0; n < scope; n++) {
            for (int m = 0; m < scope; m++) {
                rotation[n][m] = message[scope - m - 1 + i][n + j];
            }
        }
        for (int n = 0; n < scope; n++) {
            for (int m = 0; m < scope; m++) {
                updateMessage(i + n, j + m, rotation[n][m]);
            }
        }
    }

    private Sticker[][] rotateArray(Sticker[][] array) {
        Sticker[][] rotated = new Sticker[array[0].length][array.length];
        for (int i = 0; i < array[0].length; i++) {
            for (int j = 0; j < array.length; j++) {
                rotated[i][j] = array[array.length - 1 - j][i];
            }
        }
        return rotated;
    }

    private Sticker[][] rotateCounterArray(Sticker[][] array) {
        Sticker[][] rotated = new Sticker[array[0].length][array.length];
        for (int i = 0; i < array[0].length; i++) {
            for (int j = 0; j < array.length; j++) {
                rotated[i][j] = array[j][array[0].length - 1 - i];
            }
        }
        return rotated;
    }

    private void updateMessage(int i, int j, Sticker replacement) {
        message[i][j] = replacement;
        if (gridGUI != null) {
            gridGUI.updateButton(i, j);
        }
    }

    private byte[] generateFullArray(byte[] gMes) {
        int cubeSize = size * size * 6;
        if (gMes.length == cubeSize) {
            return gMes;
        }
        byte[] fullArray = new byte[cubeSize];
        for (int i = 0; i < gMes.length; i++) {
            fullArray[i] = gMes[i];
        }
        fullArray[gMes.length] = 0;
        if (fullArray.length == gMes.length + 1) {
            return fullArray;
        }
        Random random = new Random();
        for (int i = gMes.length + 1; i < fullArray.length; i++) {
            fullArray[i] = (byte) (random.nextInt(26) + 97);
        }
        return fullArray;
    }

    public byte[] cubeToArray() {
        byte[] result = new byte[size * size * 6];
        int gIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[gIndex] = (byte) message[i][j + size].getItem();
                gIndex++;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * 4; j++) {
                result[gIndex] = (byte) message[i + size][j].getItem();
                gIndex++;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[gIndex] = (byte) message[i + (size * 2)][j + size].getItem();
                gIndex++;
            }
        }
        return result;
    }

    public byte[] concatenatedCubeToArray() {
        byte[] cube = cubeToArray();
        boolean endFound = false;
        int index = 0;
        while (!endFound) {
            if (cube[index] == 0) {
                endFound = true;
            }
            index++;
        }
        index--;
        byte[] result = new byte[index];
        System.arraycopy(cube, 0, result, 0, index);
        return result;
    }

    public String byteMap() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length; i++) {
            for (int j = 0; j < message[0].length; j++) {
                sb.append(message[i][j].getItem());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        for (int i = 0; i < message.length; i++) {
//            for (int j = 0)
//        }
//        return result.toString();
//    }
}
