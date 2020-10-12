import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

public interface Sprite {
	public Shape getShape();
	public void draw(Graphics2D g2);
}
