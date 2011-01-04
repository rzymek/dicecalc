package m;

import javax.microedition.lcdui.Graphics;

public class EditableGridPanel extends GridPanel{
	private int cursor = 0;
	private int[][] editable;

	public EditableGridPanel(String[][][] texts, int[][] editable) {
		super(texts);
		this.editable = editable;
		for (int i = 0; i < editable.length; i++) 
			updateEdited(i);
	}

	private void updateEdited(int i) {
		int x = this.editable[i][0];
		int y = this.editable[i][1];
		int value = this.editable[i][2];
		this.texts[y][x][1] = String.valueOf(value);
	}

	protected void drawCellLower(Graphics g, int x, int y, String text) {
//		super.drawCellLower(g, x, y, text);
		if (editable[cursor][0] == x && editable[cursor][1] == y) {
			g.setColor(0xffffff);
			g.fillRect(cellWidth * x, cellHeight * y + cellHeight/2, cellWidth, cellHeight/2);
			g.setFont(getFont(x, y, 1));
			g.setColor(0x0000ff);
			g.drawString(text, cellWidth * (x + 1) -1, cellHeight * (y + 1), Graphics.RIGHT | Graphics.BOTTOM);
			g.setColor(0xffffff);
		}else{
			super.drawCellLower(g, x, y, text);
		}
	}

	private boolean isEditable(int x, int y) {
		for (int i = 0; i < editable.length; i++) {
			if(editable[i][0] == x && editable[i][1] == y)
				return true;
		}
		return false;
	}
	public void setCurrentValue(int value) {
		editable[cursor][2] = value;
		updateEdited(cursor);		
	}

	public int getCurrentValue() {
		return editable[cursor][2];
	}

	public int getEditing() {
		return cursor;
	}

	public void setEditing(int i) {
		cursor = i;
	}

	public void nextEdit() {
		setEditing((getEditing() + 1) % editable.length);
	}
}
