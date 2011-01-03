package m;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;


public class GridPanel {
	int[] sizes = {Font.SIZE_LARGE,Font.SIZE_LARGE};
	private int font = Font.FACE_SYSTEM;
	
	private String[][][] texts;
	private int[][][] styles;
	private int cellWidth=10;
	private int cellHeight=10;
	
	public GridPanel(String[][][] texts) {
		this.texts = texts;
	}
	
	public void setStyles(int[][][] styles) {
		this.styles = styles;
	}
	
	public void invalidate(int width, int height) {
		while(true){
			int wmax=0;
			int hmax=0;
			for(int y=0; y<texts.length;++y){
				//row
				for(int x=0; x<texts[y].length;++x){
					//column
					int w = 0;
					int h=0; 
					for(int i=0;i<texts[y][x].length; ++i){
						//cell
						Font f = getFont(x, y, i);
						int tw = f.stringWidth(texts[y][x][i])+2;
						w = Math.max(w, tw);
						h += f.getHeight();
					}
					wmax = Math.max(wmax, w);
					hmax = Math.max(hmax, h);
				}
			}
			cellWidth = wmax;
			cellHeight = hmax;
			if(sizes[0] == Font.SIZE_SMALL)
				break; //can't be any smaller
			if(cellWidth * getCols() > width || cellHeight * getRows() > height) {
				makeSmaller();
				continue;
			}
			break;
		}
	}

	private void makeSmaller() {
		if(sizes[0] != sizes[1])
			sizes[0] = sizes[1];
		else
			sizes[1] = getSmaller(sizes[1]);
	}

	private int getSmaller(int fontSize) {
		switch(fontSize){
		case Font.SIZE_LARGE:
			return Font.SIZE_MEDIUM;
		case Font.SIZE_MEDIUM:
			return Font.SIZE_SMALL;
		default:
			return Font.SIZE_SMALL;
		}
	}

	private Font getFont(int x, int y, int i) {
		return Font.getFont(font, styles[y][x][i], sizes[i]);
	}
	
	public int getRows() {
		return texts.length;
	}
	
	public int getCols() {
		return texts[0].length;
	}

	public void paint(Graphics g) {
		for (int y = 1; y <= texts.length; ++y)
			g.drawLine(0, y* cellHeight, getCols() * cellWidth, y * cellHeight);
		for (int x = 1; x <= texts[0].length; ++x)
			g.drawLine(x * cellWidth, 0, x * cellWidth, getRows() * cellHeight);
		for(int y=0; y<texts.length;++y){
			for(int x=0; x<texts[y].length;++x){
				String[] text = texts[y][x];
				g.setFont(getFont(x, y, 0));
				g.drawString(text[0], cellWidth * x +2, cellHeight * y, Graphics.LEFT | Graphics.TOP);
				g.setFont(getFont(x, y, 1));
				
				g.drawString(text[1], cellWidth * (x + 1) -1, cellHeight * (y + 1), Graphics.RIGHT | Graphics.BOTTOM);
			}
		}
	}
	public void setText(int x, int y, String a, String b){
		if(a != null)
			texts[y][x][0] = a;
		if(b != null)
			texts[y][x][1] = b;
		
	}
}
