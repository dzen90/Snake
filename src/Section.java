import java.awt.*;

public class Section {
    public int x, y, size;
    public Rectangle rectangle;
    public Section next;

    public Section(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        rectangle = new Rectangle(x + 1, y + 1, size - 2, size - 2);
    }

    // Draw random section
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, size, size);
    }
}
