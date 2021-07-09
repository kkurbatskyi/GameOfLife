import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Stream;

class GameVisual extends JComponent
{
	private final static int SCREEN_POSITION_X = 3;
	private final static int SCREEN_POSITION_Y = 3;
	
	private final static int GAME_TICK = 10000 / 60;
	private final static int DRAW_TICK = 1000 / 60;
	
	private final static int MAP_WIDTH = 51;
	private final static int MAP_HEIGHT = 51;
	
	
	private static Timer tick;
	private static Timer drawTick;
	
	
	//public static ArrayList<ArrayList<Cell>> map.table();
	//public static ArrayList<Cell> map.cells();
	public static Map map; //draw-screen,
	public static ArrayList<Map> saves;// 0-3 - Saves
	public final Point saveDim = new Point(6, 8);
	
	//public final ArrayList<Point> savesPos = new ArrayList<Point>();
	
	private final static int BUTTON_Y = 50;
	private final static int BUTTON_X = MAP_WIDTH * Cell.WIDTH + 50;

	public static ArrayList<Buttons> button;
	
	public GameVisual()
	{
		setFocusable(true);
		
		button = new ArrayList<>();
		button.add(new Play(BUTTON_X, BUTTON_Y));
		button.add(new Pause(BUTTON_X + 100, BUTTON_Y));
		button.get(1).turnOffOn();
		button.add(new Stop(BUTTON_X + 200, BUTTON_Y));
		
		map = new Map(MAP_WIDTH, MAP_HEIGHT);
		
		//TODO: add ability to load save from and into file
		saves = new ArrayList<>();
		for(int  i = 0; i < 4; i++){
			saves.add(new Map(saveDim));
		}
		
		tick = new Timer(GAME_TICK, (a) -> this.update());
		drawTick = new Timer(DRAW_TICK, (a) -> this.drawUpdate());
		tick.start();
		pause();
		
		this.addKeyListener(new pauseKeyListener());
		this.addMouseListener(new drawClickListener());
		this.addMouseMotionListener(new drawClickListener());
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
		
		// 6x8 save-rectangles
		g2.drawRect(MAP_WIDTH * Cell.WIDTH + 2 * SCREEN_POSITION_X, MAP_HEIGHT * Cell.HEIGHT + 2 * SCREEN_POSITION_Y,6 * Cell.HEIGHT, 8 * Cell.WIDTH);
		
		
		//drawout the screen, cells and markings
		g2.translate(SCREEN_POSITION_X, SCREEN_POSITION_Y);
		Stream<Cell> cellStream = map.cells().stream().sequential();
		cellStream.forEach(a -> a.draw(g2));
		drawScreen(g2);
		g2.translate(-SCREEN_POSITION_X, -SCREEN_POSITION_Y);
		
		//TODO: Make buttons different frames to add Listeners directly to them
		Stream<Buttons> controls = button.stream().sequential();
		controls.forEach(a -> a.draw(g2));
		
		
		//Little useless "pause" sign like '||'
		/*
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
		}*/
		//TODO Buttons
		/*
		drawStart();
		drawPause();
		drawStop();
		* */
		
	}
	
	//public void update(){}
	public void start()
	{
		tick.start();
		if (drawTick.isRunning()) drawTick.stop();
		button.get(0).turnOffOn();
		button.get(1).turnOffOn();
	}
	
	public void stop()
	{
		button.get(2).turnOffOn();
		tick.stop();
		
		map = new Map(map.getDimensions());
		repaint();
	}
	
	
	public void pause()
	{
		if(tick.isRunning()) {
			tick.stop();
			button.get(0).turnOffOn();
			button.get(1).turnOffOn();
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
	
	//TODO: Save drawing: the save-boxes, save maps
	/*
	public static void drawSave(Graphics2D g2){
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
				Cell cell = map.table().get(i).get(j);
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
		
		map.setTable(tableMod);
		map.setCells(cellsMod);
		
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
					if (!(i == x && j == y) && map.table().get(i).get(j).isAlive()) counter++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
		return counter;
	}
	
	private class drawClickListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			//pause if clicked off map. start if clicked again
			int x = e.getX() - SCREEN_POSITION_X;
			int y = e.getY() - SCREEN_POSITION_Y;
			if (x >= MAP_WIDTH * Cell.HEIGHT || y >= MAP_HEIGHT * Cell.HEIGHT) {
				pause();
			}
			else if(x > 0 && y > 0){
				Cell clicked = map.table().get(x / Cell.HEIGHT).get(y / Cell.HEIGHT);
				//if alive - kill, if dead - revive. Removed unwanted cells from the drawing
				if(!clicked.isAlive())clicked.revive();
				else
					clicked.kill();
			}
		}

		//click-and-hold drawing
		@Override
		public void mouseDragged(MouseEvent e)
		{
				int x = e.getX() - SCREEN_POSITION_X;
				int y = e.getY() - SCREEN_POSITION_Y;
				if (!(x >= MAP_WIDTH * Cell.HEIGHT || y >= MAP_HEIGHT * Cell.HEIGHT) && x > 0 && y > 0) {
					Cell clicked = map.table().get(x / Cell.HEIGHT).get(y / Cell.HEIGHT);
					if (!clicked.isAlive()) clicked.revive();
				}
		}
	}
	
	private class pauseKeyListener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				pause();
			}
			else if(e.getKeyCode() == KeyEvent.VK_R){
				stop();
			}
		}
		
	}
	
}