package coh;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import m.EditableGridPanel;
import m.ProbabilityNdN;
import m.ProbabilityTable;

public class MainScreen extends Canvas {
	private static final int EDITING_AV = 0;
	private static final int EDITING_DV = 1;
	private static final int MIN_VALUE = -9;
	private static final int MAX_VALUE = 30;
	private final EditableGridPanel gridPanel;

	public MainScreen() {
		String[][][] texts = {
			{{"",    ""    },{"DV", "00"  },{"CAP", "+1"  },{"CAP",  "+2" },{"1 of 2",""    }},
			{{"AV",  "00"  },{"12 ","100%"},{"+00%","100%"},{"+00%","100%"},{"+00%",  "100%"}},
			{{"KILL","(+4)"},{"12 ","100%"},{"+00%","100%"},{"+00%","100%"},{"+00%",  "100%"}},
		};
		byte N = Font.STYLE_PLAIN;
		byte B = Font.STYLE_BOLD;
		byte I = Font.STYLE_ITALIC;
		byte[][][] styles = {
			{{N,N},{B,B},{N,N},{N,N},{I,N}},
			{{B,B},{N,B},{N,N},{N,N},{I,N}},
			{{N,N},{N,B},{N,N},{N,N},{I,N}},
		};
		int[][] editable = {
			{0,1,5},//AV
			{1,0,12},//DV
		};
		gridPanel = new ProbabilityTable(texts, editable, new ProbabilityNdN(2,6));		
		gridPanel.setStyles(styles);
		gridPanel.invalidate(getWidth(), getHeight());
	}
	
	protected void paint(Graphics g) {
		g.setColor(0x000000);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(0xffffff);
		gridPanel.paint(g);
	}
	
	protected void keyPressed(int keyCode) {
		switch(keyCode){
		case KEY_NUM0:
		case KEY_NUM1:
		case KEY_NUM2:
		case KEY_NUM3:
		case KEY_NUM4:
		case KEY_NUM5:
		case KEY_NUM6:
		case KEY_NUM7:
		case KEY_NUM8:
		case KEY_NUM9:
			int value = keyCode - KEY_NUM0;
			if(gridPanel.getEditing() == EDITING_DV)
				value += 10;
			gridPanel.setCurrentValue(value);
			gridPanel.nextEdit();
			break;
		default:
			switch(getGameAction(keyCode)) {
			case DOWN:
				if(gridPanel.getCurrentValue() <= MIN_VALUE)
					return;
				gridPanel.setCurrentValue(gridPanel.getCurrentValue()-1);
				break;
			case UP:
				if(gridPanel.getCurrentValue() >= MAX_VALUE)
					return;
				gridPanel.setCurrentValue(gridPanel.getCurrentValue()+1);
				break;
			case FIRE:
			case LEFT:
			case RIGHT:
				gridPanel.nextEdit();
				break;
			default:
				return;
			}
		}
		repaint();
	}

}
