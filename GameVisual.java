import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

class GameVisual extends JComponent
{
	private final static int SCREEN_POSITION_X = 3;
	private final static int SCREEN_POSITION_Y = 3;
	
	private final static int GAME_TICK = 10000 / 60;
	private final static int DRAW_TICK = 1000 / 60;
	
	private final static int MAP_WIDTH = 50;
	private final static int MAP_HEIGHT = 50;
	
	
	private static Timer tick;
	private static Timer drawTick;
	
	
	public static ArrayList<ArrayList<Cell>> table;
	public static ArrayList<Cell> cells;
	//public static ArrayList<Cell> liveCell;
	//public static ArrayList<Cell> deadCell;
	
	public GameVisual()
	{

		table = new ArrayList<>();
		cells = new ArrayList<>();
		for (int i = 0; i < MAP_WIDTH; i++) {
			table.add(new ArrayList<>());
			for (int j = 0; j < MAP_HEIGHT; j++) {
				Cell newCell = new Cell(i, j);
				cells.add(newCell);
				table.get(i).add(newCell);
			}
		}
		tick = new Timer(GAME_TICK, (a) -> this.update());
		drawTick = new Timer(DRAW_TICK, (a) -> this.drawUpdate());
		tick.start();
		
		addKeyListener(new pauseKeyListener());
		addMouseListener(new drawClickListener());
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintComponent(g2);
	}
	private void paintComponent(Graphics2D g2)
	{
		g2.setBackground(Color.lightGray);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		g2.setBackground(Color.darkGray);
		//vertical separator line
		g2.drawLine(MAP_WIDTH * Cell.WIDTH + 2 * SCREEN_POSITION_X, 0, MAP_WIDTH * Cell.WIDTH + 2 * SCREEN_POSITION_X, this.getHeight());
		//horizontal separator line
		g2.drawLine(0, MAP_HEIGHT * Cell.HEIGHT + 2 * SCREEN_POSITION_Y,
						   MAP_WIDTH * Cell.WIDTH + 2 * SCREEN_POSITION_X, MAP_HEIGHT * Cell.HEIGHT + 2 * SCREEN_POSITION_Y);
		
		//drawout the screen, cells and markings
		g2.translate(SCREEN_POSITION_X, SCREEN_POSITION_Y);
		Stream<Cell> cellStream = cells.stream().sequential();
		cellStream.forEach(a -> a.draw(g2));
		drawScreen(g2);
		g2.translate(-SCREEN_POSITION_X, -SCREEN_POSITION_Y);
		
		if (!tick.isRunning()) {
			Graphics2D g3 = (Graphics2D) g2.create();
			g3.setPaint(Color.GREEN);
			Shape pause = new Rectangle(510, 10, 10, 50);
			g3.fill(pause);
			g3.draw(pause);
			pause = new Rectangle(530, 10, 10, 50);
			g3.fill(pause);
			g3.draw(pause);
			g3.dispose();
		}
		//TODO Buttons
		/*
		drawStart();
		drawPause();
		drawStop();
		* */
		
	}
	
	//public void update(){}
	public static void start()
	{
		tick.start();
		if (drawTick.isRunning()) drawTick.stop();
	}
	
	public void stop()
	{
		tick.stop();
		table = new ArrayList<>();
		cells = new ArrayList<>();
		for (int i = 0; i < MAP_WIDTH; i++) {
			table.add(new ArrayList<>());
			for (int j = 0; j < MAP_HEIGHT; j++) {
				Cell newCell = new Cell(i, j);
				cells.add(newCell);
				table.get(i).add(newCell);
			}
		}
	}
	
	
	public static void pause()
	{
		if(tick.isRunning()) {
			tick.stop();
			if (!drawTick.isRunning()) drawTick.start();
		}
		else start();
	}
	
	public static void drawScreen(Graphics2D g2)
	{
		g2.drawRect(0, 0, MAP_WIDTH * Cell.HEIGHT, MAP_HEIGHT * Cell.HEIGHT);
		g2.setColor(Color.GRAY);
		for (int i = 1; i < MAP_WIDTH; i++) {
			if (i % 3 == 0) g2.setColor(Color.DARK_GRAY);
			else g2.setColor(Color.GRAY);
			g2.drawLine(i * Cell.HEIGHT, 0, i * Cell.HEIGHT, MAP_HEIGHT * Cell.HEIGHT);
		}
		for (int i = 1; i < MAP_HEIGHT; i++) {
			if (i % 3 == 0) g2.setColor(Color.DARK_GRAY);
			else g2.setColor(Color.GRAY);
			g2.drawLine(0, i * Cell.HEIGHT, MAP_WIDTH * Cell.HEIGHT, i * Cell.HEIGHT);
		}
	}
	
	/*public static void drawStop(Graphics2D g2){
		g2.setColor(Color.GRAY);
		for(int i = 1; i < MAP_WIDTH; i++) {
			if(i % 3 == 0)g2.setColor(Color.DARK_GRAY);
			else g2.setColor(Color.GRAY);
			g2.drawLine(i * Cell.HEIGHT, 0, i * Cell.HEIGHT, MAP_HEIGHT * Cell.HEIGHT);
		}
		for(int i = 1; i < MAP_HEIGHT; i++) {
			if(i % 3 == 0)g2.setColor(Color.DARK_GRAY);
			else g2.setColor(Color.GRAY);
			g2.drawLine(0, i * Cell.HEIGHT, MAP_WIDTH * Cell.HEIGHT, i * Cell.HEIGHT);
		}
	}*/
	
	public void drawUpdate()
	{
		repaint();
	}
	
	public void update()
	{

		ArrayList<ArrayList<Cell>> tableMod = new ArrayList<>();
		ArrayList<Cell> cellsMod = new ArrayList<>();
		
		for (int i = 0; i < MAP_WIDTH; i++) {
			tableMod.add(new ArrayList<>());
			for (int j = 0; j < MAP_HEIGHT; j++) {
				Cell cell = table.get(i).get(j);
				Cell newCell = new Cell(cell.positionX, cell.positionY, cell.live);
				int neighbours = this.countNeighbours(cell.positionX, cell.positionY);
				if (cell.isAlive()) {
					if (neighbours < 2 || neighbours > 3) {
						newCell = new Cell(cell.positionX, cell.positionY);
					}
				} else if (neighbours == 3) newCell = new Cell(cell.positionX, cell.positionY, true);
				tableMod.get(i).add(newCell);
				cellsMod.add(newCell);
			}
		}
		
		table = tableMod;
		cells = cellsMod;
		
		repaint();
	}

//	public void decideStage(Cell cell){
//		int neighbours = this.countNeighbours(cell.positionX, cell.positionY);
//		if(cell.isAlive()){
//			if(neighbours < 2 || neighbours > 3)cell.kill();
//		}
//		else
//		if(neighbours == 3)cell.revive();
//	}
	
	public int countNeighbours(int x, int y)
	{
		int counter = 0;
		try {
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					if (!(i == x && j == y) && table.get(i).get(j).isAlive()) counter++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		return counter;
	}
	
	private static class drawClickListener extends MouseInputAdapter
	{
		private boolean isPressed;
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			int x = e.getX() - SCREEN_POSITION_X;
			int y = e.getY() - SCREEN_POSITION_Y;
			if (x >= 500 || y >= 500) {
				if (tick.isRunning()) pause();
				else tick.start();
				return;
			}
			table.get(x / Cell.HEIGHT).get(y / Cell.HEIGHT).revive();
		}
	}
	
	private static class pauseKeyListener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			System.out.println("Click!");
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				if (tick.isRunning()) pause();
				else tick.start();
			}
		}
	}
	
}