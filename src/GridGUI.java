import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GridGUI {
    JFrame frame;
    JPanel panel;
    JButton[][] grid;
    Sticker[][] message;
    int width;
    int length;
    int size;

    public GridGUI(Sticker[][] message, String name) {
        frame = new JFrame(name);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // frame.setUndecorated(true);
        // frame.setVisible(true);
        // frame.setSize(width, length);
        this.message = message;
        width = message.length;
        length = message[0].length;
        size = width / 3;
        grid = new JButton[width][length];
        panel = new JPanel();
        panel.setLayout(new GridLayout(width, length));
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < length; ++j) {
                Sticker currentSticker = message[i][j];
                JButton button = new JButton(String.valueOf(currentSticker.getItem()));
                if (currentSticker.getColor() == Color.BLACK) {
                    button.setBorder(null);
                } else {
                    button.setBorder(new LineBorder(Color.BLACK));
                }
                button.setBackground(currentSticker.getColor());
                grid[i][j] = button;
                panel.add(this.grid[i][j]);
            }
        }
        frame.revalidate();
        frame.setContentPane(panel);
        frame.pack();
    }

    public void updateGridGui() {
        for (int i = 0; i < size; i++) {
            for (int j = size; j < 2 * size; j++) {
                updateButton(i, j);
            }
        }
        for (int i = size; i < 2 * size; i++) {
            for (int j = 0; j < 4 * size; j++) {
                updateButton(i, j);
            }
        }
        for (int i = 2 * size; i < 3 * size; i++) {
            for (int j = size; j < 2 * size; j++) {
                updateButton(i, j);
            }
        }
    }

    public void updateButton(int i, int j) {
        Sticker currentSticker = message[i][j];
        JButton currentButton = (JButton) frame.getContentPane().getComponent(i * length + j);
        if (!currentButton.getText().equals(String.valueOf(currentSticker.getItem()))) {
            currentButton.setText(String.valueOf(currentSticker.getItem()));
        }
        if (!currentButton.getBackground().equals(currentSticker.getColor())) {
            currentButton.setBackground(currentSticker.getColor());
        }
    }
}