import java.awt.*;

public class Sticker {
    private char item;
    private Color color;

    public Sticker(byte item, Color color) {
        this.item = (char) item;
        this.color = color;
    }

    public Sticker(char item, Color color) {
        this.item = item;
        this.color = color;
    }

    public Sticker(byte item, int i, int j, int size) {
        this.item = (char) item;
        if (i < size && j >= size && j < size * 2) color = Color.WHITE;
        if (i >= size * 2 && j >= size && j < size * 2) color = Color.YELLOW;
        if (i < size * 2 && i >= size && j >= 0 && j < size) color = Color.ORANGE;
        if (i < size * 2 && i >= size && j >= size && j < size * 2) color = Color.GREEN;
        if (i < size * 2 && i >= size && j >= size * 2 && j < size * 3) color = Color.RED;
        if (i < size * 2 && i >= size && j >= size * 3) color = Color.BLUE;
        if (color == null) color = Color.BLACK;
    }

    public char getItem() {
        return item;
    }

    public void setItem(char item) {
        this.item = item;
    }

    public void setItem(byte item) {
        this.item = (char) item;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
