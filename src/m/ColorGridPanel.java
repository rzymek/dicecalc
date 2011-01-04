package m;

import javax.microedition.lcdui.Graphics;

public class ColorGridPanel extends GridPanel {
	private int[][] color;

	public ColorGridPanel(String[][][] texts, int[][] color) {
		super(texts);
		this.color = color;
	}

	public void paint(Graphics g) {
		super.paint(g);
//		g.setColor(0x000000);
//		for (int y = 1; y <= texts.length; ++y)
//			g.drawLine(cellWidth, y* cellHeight, getCols() * cellWidth, y * cellHeight);
//		for (int x = 1; x <= texts[0].length; ++x)
//			g.drawLine(x * cellWidth, cellHeight, x * cellWidth, getRows() * cellHeight);
	}
	
	protected void drawCell(Graphics g, int x, int y, String[] text) {
		g.setColor(color[y][x]);
		g.fillRect(cellWidth * x+1, cellHeight * y+1, cellWidth-1, cellHeight-1);
		g.setColor(color[y][x] == 0x000000 ? 0xffffff : 0x000000);		
		super.drawCell(g, x, y, text);
	}
	
	protected void setColor(int x, int y, int color) {
		this.color[y][x] = color;
	}
	
}
