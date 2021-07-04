import java.awt.Point;
import java.util.ArrayList;

public class Map{
	private ArrayList<ArrayList<Cell>> table;
	private ArrayList<Cell> cells;
	private Point dimensions; //cell count vertical and horizontal
	
	public Map(int x, int y){
		dimensions = new Point(x, y);
		table = new ArrayList<>();
		cells = new ArrayList<>();
		for (int i = 0; i < dimensions.x; i++) {
			table.add(new ArrayList<>());
			for (int j = 0; j < dimensions.y; j++) {
				Cell newCell = new Cell(i, j);
				cells.add(newCell);
				table.get(i).add(newCell);
			}
		}
	}
	
	public Map(Point dim){
		dimensions = dim;
		table = new ArrayList<>();
		cells = new ArrayList<>();
		for (int i = 0; i < dimensions.x; i++) {
			table.add(new ArrayList<>());
			for (int j = 0; j < dimensions.y; j++) {
				Cell newCell = new Cell(i, j);
				cells.add(newCell);
				table.get(i).add(newCell);
			}
		}
	}
	
	public void setTable(ArrayList<ArrayList<Cell>> table)
	{
		this.table = table;
	}
	
	public void setCells(ArrayList<Cell> cells)
	{
		this.cells = cells;
	}
	
	public ArrayList<ArrayList<Cell>> table()
	{
		return table;
	}
	
	public ArrayList<Cell> cells()
	{
		return cells;
	}
	
	public Point getDimensions()
	{
		return dimensions;
	}
	
	public void setDimensions(Point dimensions)
	{
		this.dimensions = dimensions;
	}
}