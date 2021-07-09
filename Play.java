import java.awt.*;

public class Play extends SpriteImpl implements Buttons
{
	private final static Color OFF = Color.GRAY;
	private final static Color ON = Color.GREEN;
	private final static int WIDTH = 25;
	private boolean status;
	
	public Play(int x, int y){
		super(new Polygon(new int[]{x, x + WIDTH, x}, new int[]{y, y + WIDTH / 2, y + WIDTH}, 3), ON);
		status = false;
	}
	
	public void turnOffOn(){
		setColor(this.status ? ON : OFF);
		status = !status;
	}
}