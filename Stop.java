import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Stop extends SpriteImpl implements Buttons
{
	private final static Color ON = Color.RED;
	private final static Color OFF = Color.getHSBColor(60, 100, 40);
	private final static float RADIUS = 25;
	private boolean status;
	
	public Stop(int x, int y){
		super(new Ellipse2D.Float(x, y, RADIUS, RADIUS), OFF);
		status = false;
	}
	
	public void turnOffOn(){
		setColor(this.status ? ON : OFF);
		status = !status;
	}
}