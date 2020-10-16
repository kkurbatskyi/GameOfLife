import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Pause extends SpriteImpl implements Buttons
{
	private final static Color ON = Color.YELLOW;
	private final static Color OFF = Color.getHSBColor(80, 100, 40);
	private final static float HEIGHT = 25;
	private boolean status;
	
	public Pause(int x, int y){
		super(createShape(x, y), OFF);
		status = false;
	}
	
	private static Area createShape(int x, int y)
	{
		Area shape = new Area(new Rectangle2D.Float(x, y, HEIGHT / 3, HEIGHT));
		shape.exclusiveOr(new Area(new Rectangle2D.Float(x + 2 * (HEIGHT / 3), y, HEIGHT / 3, HEIGHT)));
		return shape;
	}
	
	public void turnOffOn(){
		setColor(this.status ? ON : OFF);
		status = !status;
	}
}