package common;

import javax.microedition.lcdui.Graphics;

public class DiceGraphics {

	private int x1;
	private int y1;
	private int x2;
	private int y2;
	Graphics g;

	public void setGraphics(Graphics g) {
		this.g = g;
	}
	
	public DiceGraphics(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public int getWidth(){
		return x2-x1;
	}
	public int getHeight(){
		return y2-y1;
	}

	public void drawDie(int val, int x, int y, int size, int bg, int fg) {
		x+=x1;
		y+=y1;
		g.setColor(bg);
		g.fillRect(x, y, size,size);
		g.setColor(fg);
		g.drawRect(x, y, size,size);
		int margin = size/10;
		int dot = size/4;
		if (val > 1) // upper left dot
			fillOval(x + margin,y + margin, dot);
		if (val > 3) // upper right dot
			fillOval(x + size - dot - margin,y + margin, dot);
		if (val == 6) // middle left dot
			fillOval(x + margin,y + (size-dot)/2, dot);
		if (val % 2 == 1) // middle dot (for odd-numbered val's)
			fillOval(x + (size-dot)/2,y + (size-dot)/2, dot);
		if (val == 6) // middle right dot
			fillOval(x  + size - dot - margin,y + (size-dot)/2, dot);
		if (val > 3) // bottom left dot
			fillOval(x + margin,y  + size - dot - margin, dot);
		if (val > 1) // bottom right dot
			fillOval(x + size - dot - margin,y +  size - dot - margin, dot);
	}

	private void fillOval(int x, int y, int dot) {
		g.fillRoundRect(x, y, dot, dot, dot,dot);
	}

	public void draw(String s, int x, int y, int anchor) {
		g.drawString(s, x1+x, y1+y, anchor);
	}

}
