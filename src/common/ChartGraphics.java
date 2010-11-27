package common;

import javax.microedition.lcdui.Graphics;


public class ChartGraphics {
	private static final int MARGIN = 2;
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	Graphics g;
	private int bars;
	private int barWidth;

	public void setGraphics(Graphics g) {
		this.g = g;
	}
	
	public ChartGraphics(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void setGrid(int bars) {
		this.bars = bars;
		barWidth = (x2 - x1)/bars-MARGIN;
	}

	public void drawBar(int i, int value, String label) {
		int height = getLevel(value);
		g.setColor(GridGraphics.getProbabilityColor(value));
		int x = x1+MARGIN+barWidth*i;
		int y = y2-height;
		g.fillRect(x, y, barWidth, height);
		int anchor = Graphics.BOTTOM;
		if(value > 50){
			anchor = Graphics.TOP;
			g.setColor(0x0);
		}
		g.drawString(label, x + (barWidth - g.getFont().stringWidth(label))/2, y, Graphics.LEFT|anchor);
	}

	private int getLevel(int value) {
		return value*(y2-y1)/100;
	}
	
	public void drawLines() {
		int w = getWidth();
		int h = y2-y1;
		int y;
		g.setColor(0x808080);
		g.drawRect(x1+MARGIN, y1, w, h);
		y = y1+h/4;	g.drawLine(x1+MARGIN, y, x1+w, y);
		y = y1+h/2;	g.drawLine(x1+MARGIN, y, x1+w, y);
		y = y2-h/4; g.drawLine(x1+MARGIN, y, x1+w, y);
	}

	public int getWidth() {
		return bars*barWidth;
	}

	public void drawLine(int value, int color) {
		int h = getLevel(value);
		g.setColor(color);
		int w = getWidth();
		g.drawLine(x1+MARGIN, h, x1+w, h);
		g.drawLine(x1+MARGIN, h+1, x1+w, h+1);
//		g.drawLine(x1+MARGIN, h-1, x1+w, h-1);
	}

	public void fillValue(int to, int color) {
		int t = getLevel(to);
		g.setColor(color);
		int w = getWidth();
		g.fillRect(x1+MARGIN, 0, w, t);
	}
	

}
