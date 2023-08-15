import java.awt.*;

public class Player {
    public enum MovingDirection  {
        Up, Down, Right, Left
    }
    private Game game;
    private int size;
    private int speed;
    public MovingDirection direction;
    private Section tail;

    public Player(int x, int y, int length, Game game) {
        this.game = game;
        this.size = 10;
        this.speed = 1;
        direction = MovingDirection.Up;
        tail = null;
        for (int i=0; i<length; i++) {
            addSection(x, y);
            y += size;
        }
    }

    public void addSection(int x, int y) {
        Section newSection = new Section(x, y, size);
        newSection.next = tail;
        tail = newSection;
    }

    public void addSection(Section randomSection) {
        randomSection.next = tail;
        tail = randomSection;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        Section current = tail;
        while(current != null) {
            g.fillRect(current.x, current.y, size, size);
            current = current.next;
        }

    }

    public void update() {
        Section current = tail;
        while(current.next != null) {
            current.x = current.next.x;
            current.y = current.next.y;
            current.rectangle.x = current.next.rectangle.x;
            current.rectangle.y = current.next.rectangle.y;
            current = current.next;
        }

        switch(direction){
            case Up:
                current.y -= size;
                current.rectangle.y -= size;
                break;
            case Down:
                current.y += size;
                current.rectangle.y += size;
                break;
            case Right:
                current.x += size;
                current.rectangle.x += size;
                break;
            case Left:
                current.x -= size;
                current.rectangle.x -= size;
                break;
        }
        // Check for collision with walls
        if ((current.rectangle.getMinX() <= 0 && direction == MovingDirection.Left)||
                (current.rectangle.getMaxX() >= Game.WIDTH && direction == MovingDirection.Right)||
                (current.rectangle.getMinY() <= 0 && direction == MovingDirection.Up)||
                (current.rectangle.getMaxY() >= Game.HEIGHT && direction == MovingDirection.Down)) {
            game.finish();
        }

        // Check if the snake intersects its body
        Section checked = tail;
        while (checked.next != current) {
            if (current.rectangle.intersects(checked.rectangle)) {
                game.finish();
            }
            checked = checked.next;
        }

        // Check for collision with random section
        if (current.rectangle.intersects(game.randomSection.rectangle)) {
            addSection(game.randomSection);
            game.randomSection = null;
        }


    }
}
