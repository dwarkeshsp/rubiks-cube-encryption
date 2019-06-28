import java.io.IOException;

public class VigenereGenerator {
    byte[] key;
    byte[] message;

    public VigenereGenerator(String key, byte[] message) throws IOException {
        this.message = message;
        this.key = key.getBytes();
    }

    public void encode(String fileName) throws IOException {
        if (key.length != 0) {
            xor();
        }
        fileName = "Rubik's Encrypted " + fileName;
        Utilities.writeMessage(fileName, message);
    }

    public byte[] decode() throws IOException {
        if (key.length != 0) {
            xor();
        }
        return message;
    }

    private void xor() {
        for (int i = 0; i < message.length; i++) {
            message[i] = (byte) (message[i] ^ key[i % key.length]);
        }
    }

}
