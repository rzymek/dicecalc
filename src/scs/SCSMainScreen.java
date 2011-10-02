package scs;

import java.util.Enumeration;
import java.util.Stack;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class SCSMainScreen extends Canvas {
	private final Stack attack = new Stack();
	private final Stack defence = new Stack();
	private Stack selected = attack;
	
	private static final int SPACING = 2;

	protected void paint(Graphics g) {
		g.setColor(0x000000);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(0xffffff);
		Font big = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
		Font med = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
		Font small = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
		g.setFont(med);
		int y = 0;
		g.drawString("Attack:", 0, y, Graphics.LEFT | Graphics.TOP); 
		g.drawString(""+sum(attack), getWidth(), y, Graphics.RIGHT| Graphics.TOP);
		y += g.getFont().getHeight()+SPACING;
		g.setFont(small);
		if(selected == attack) {
			g.setColor(0x000080);
			g.fillRect(0, y, getWidth(), g.getFont().getHeight());
			g.setColor(0xffffff);
		}
		g.drawString(sumString(attack), 0, y, Graphics.LEFT | Graphics.TOP); 
		y += g.getFont().getHeight()+SPACING;
		g.setFont(med);
		g.drawString("Defence:", 0, y, Graphics.LEFT | Graphics.TOP); 
		g.drawString(""+sum(defence), getWidth(), y, Graphics.RIGHT| Graphics.TOP);
		y += g.getFont().getHeight()+SPACING;
		g.setFont(small);
		if(selected == defence) {
			g.setColor(0x000080);
			g.fillRect(0, y, getWidth(), g.getFont().getHeight());
			g.setColor(0xffffff);
		}
		g.drawString(sumString(defence), 0, y, Graphics.LEFT | Graphics.TOP); 
		y += g.getFont().getHeight()+SPACING;
		g.setFont(big);
		g.drawString(ratio(), getWidth()/2, y, Graphics.HCENTER | Graphics.TOP); 
		y += g.getFont().getHeight()+SPACING;
	}
	
	private String ratio() {
		int attack = sum(this.attack);
		int defence = sum(this.defence);
		if(attack > defence) {
			return (attack / defence) + ":1";
		}else{
			return "1:"+(defence / attack);
		}
	}

	private String sumString(Stack stack) {
		StringBuffer sum = new StringBuffer();
		Enumeration elements = stack.elements();
		if(!elements.hasMoreElements())
			return "0";
		for(;;) {
			Integer value = (Integer) elements.nextElement();
			sum.append(value);
			if(elements.hasMoreElements())
				sum.append("+");
			else
				break;
		}
		return sum.toString();
	}

	private int sum(Stack stack) {
		int res = 0;
		Enumeration elements = stack.elements();
		while (elements.hasMoreElements()) {
			Integer value = (Integer) elements.nextElement();
			res += value.intValue();			
		}
		return res;
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
			if(value == 0)
				value = 10;
			selected.push(new Integer(value));
			break;
		default:
			switch(getGameAction(keyCode)) {
			case DOWN:
				selected = defence;
				break;
			case UP:
				selected = attack;
				break;
			case FIRE:
				if(selected == attack)
					selected = defence;
				else 
					selected = attack;
			case LEFT:
				selected.pop();
				break;
			case RIGHT:
			default:
				return;
			}
		}
		repaint();
	}

}
