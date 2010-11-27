package common;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class GridGraphics {

	private Graphics g;
	private int cellwidth;
	private int cellheight;

	public GridGraphics(int cellwidth, int cellheight) {
		this.cellwidth = cellwidth;
		this.cellheight = cellheight;
	}

	public void clear(int color, int width, int height) {
		g.setColor(color);
		g.fillRect(0,0, width, height);
	}

	public void fill(int color, int x, int y) {
		g.setColor(color);
		g.fillRect(x*cellwidth, y*cellheight, cellwidth, cellheight);
	}

	public void drawString(String string, int x, int y, Font font, int anchor) {
		g.setFont(font);
		g.drawString(string, (x+1)*cellwidth, (y+1)*cellheight,anchor);
	}

	public void drawGrid(int w, int h) {		
		int wmax = w*cellwidth;
		int hmax = h*cellheight;
		for(int x=0;x<=wmax;x+=cellwidth) 
			g.drawLine(x,0,x,hmax);
		for(int y=0;y<=hmax;y+=cellheight) 
			g.drawLine(0,y,wmax,y);
	}

	public void drawDigits(int[] v, int x, int y, Font font, int anchor) {		
		drawString(String.valueOf(v[0]*10+v[1]), x, y, font, anchor);
	}

	public void drawSelectedDigit(int[] v, int selected_digit, int x, int y, Font font, int anchor) {
		String s = String.valueOf(v[selected_digit]);
		StringBuffer b = new StringBuffer();
		for(int i=selected_digit+1;i<v.length;++i) 
			b.append(v[i]);
		int off = font.stringWidth(b.toString());
		g.setFont(font);
		g.drawString(s, (x+1)*cellwidth - off, (y+1)*cellheight,anchor);
	}

	public void setGraphcs(Graphics g) {
		this.g = g;
	}

	public void fillPercent(int probability, int x, int y) {
		fill(getProbabilityColor(probability),x,y);
	}

	public static int getProbabilityColor(int probability) {
		if(probability < 50) {
			return 0xff0000 + probability * 2 * 0xff /100 * 0x100;
		} else {
			int i = (probability-50) * 2 * 0xff /100 * 0x10000;
			return 0xffff00 - i;
		}
	}

	public void drawFrame(int color, int x, int y) {
		g.setColor(color);
		for(int i=1;i<=3;++i) 
			g.drawRect(x*cellwidth+i, y*cellheight+i, cellwidth-2*i, cellheight-2*i);
		
	}
}
