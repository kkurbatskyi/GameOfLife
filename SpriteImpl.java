import java.awt.*;
import java.awt.geom.*;

public abstract class SpriteImpl implements Sprite
{
	
	// drawing
	private Shape shape;
	private Color fill;
	
	protected SpriteImpl(Shape shape, Color fill)
	{
		this.shape = shape;
		this.fill = fill;
	}
	
	/*protected SpriteImpl(Shape shape, Color fill)
	{
		this(shape, fill);
	}*/
	
	public Shape getShape()
	{
		return shape;
	}
	
	public void setColor(Color color)
	{
		fill = color;
	}
	
	public void draw(Graphics2D g2)
	{
		Graphics2D g3 = (Graphics2D) g2.create();
		g3.setPaint(this.fill);
		g3.fill(this.shape);
		g3.draw(this.shape);
		g3.dispose();
	}
}