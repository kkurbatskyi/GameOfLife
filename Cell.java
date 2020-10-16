import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Cell extends SpriteImpl
{
    public boolean live;
    public int positionX;
    public int positionY;
    
    private final static Color DEAD = Color.WHITE;
    private final static Color LIVE = Color.BLACK;
    public final static int HEIGHT = 10;
    public final static int WIDTH = 10;
    
    public Cell(int x, int y){
        super(new Rectangle(x*HEIGHT, y*WIDTH, WIDTH, HEIGHT), DEAD);
        positionX = x;
        positionY = y;
        live = false;
    }
    public Cell(int x, int y, boolean live){
        super(new Rectangle(x*HEIGHT, y*WIDTH, WIDTH, HEIGHT), live ? LIVE : DEAD);
        positionX = x;
        positionY = y;
        this.live = live;
    }
    
    
    public void kill(){
        live = false;
        this.setColor(DEAD);
    }
    public void revive(){
        live = true;
        this.setColor(LIVE);
    }
    public boolean isAlive(){
        return live;
    }
    
    @Override
    public Cell clone(){
        Cell newCell = new Cell(this.positionX, this.positionY, this.live);
        newCell.live = this.live;
        if(this.live) this.setColor(LIVE);
        return newCell;
    }
    
    public boolean equals(Cell cell){
        return this.positionX == cell.positionX && this.positionY == cell.positionY;
    }
    
}

