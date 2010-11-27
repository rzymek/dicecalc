package rune;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class RuneCalc extends Canvas {
	int[] heroValue = {3,3,3};
	int[] heroDamage = {1,1,1};
	int[] enemyValue = {10,10,10};
	int[] enemyDamage = {1,1,1};
	
	int[] heroProbability = new int[3];
	int[] enemyProbability = new int[3];
	
	int[] heroExpects = new int[3];
	int[] enemyExpects = new int[3];
	
	int[] probability;
	
	public RuneCalc() {
		int[] k10 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
		float sum = 100;
		float[] probability = new float[k10.length];
		for(int i=0;i<k10.length;++i) 
			probability[i] = k10[i] / sum;		
		for(int i=probability.length-2;i>=0;--i)  
			probability[i] += probability[i+1];
		this.probability = new int[probability.length];
		for(int i=0;i<probability.length;++i) {
			this.probability[i] = (int)(probability[i]*100f + 0.5);
		}
		calcStats();
	}
	
	Hashtable damageProbs = new Hashtable(6);
	
	public void calcStats() {
		for(int i=0;i<3;++i) {
			heroProbability[i] = getProbability(enemyValue[i] - heroValue[i]);
			enemyProbability[i] = 100 - heroProbability[i];
			heroExpects[i] = heroProbability[i] * heroDamage[i];
			enemyExpects[i] = enemyProbability[i] * enemyDamage[i] + (i>0 ? enemyExpects[i-1] : 0);
		}

		damageProbs.clear();
		int ds = 0;
		int dp = 1;
		for(int i=0;i<3;++i) {
			if(enemyDamage[i] == 0) {
				setDamageProb(0, 100);
			} else {
				setDamageProb(0, 100-enemyProbability[i]);
				setDamageProb(enemyDamage[i], enemyProbability[i]);				
				int next = enemyDamage[(i+1)%3];
				if(next != 0)
					setDamageProb(enemyDamage[i]+next, enemyProbability[i]*enemyProbability[(i+1)%3]/100);
				ds += enemyDamage[i];
				dp *= enemyProbability[i];
			}
		}
		setDamageProb(ds, dp/100/100);
	}

	private void setDamageProb(int damage, int p) {
		Integer key = new Integer(damage);
		Integer value = (Integer) damageProbs.get(key);
		if(value == null)
			value = new Integer(p);
		else
			value = new Integer(value.intValue() + p);
		damageProbs.put(key, value);
	}
	
	private int getProbability(int dice) {
		return dice <= 2 ? 100 : dice > 20 ? 0 : probability[dice-2];
	}

	
	protected void paint(Graphics g) {
		g.setColor(0x000000);
		g.fillRect(0,0,getWidth(), getHeight());
		g.setColor(0xffffff);
		
		Font large = getFont(Font.SIZE_LARGE);
		Font medium= getFont(Font.SIZE_MEDIUM);
		
		int lw = large.stringWidth(" 00");
		int lh = large.getHeight();
		int mw = medium.stringWidth("/0");
		int mh = medium.getHeight();
		int cw = lw+mw;
	
		g.setColor(0x00aa00);
		g.fillRect(0,0,getWidth(),lh);
		g.setColor(0x0);
		drawStats(g, large, medium, lw, mw, heroValue, heroDamage, lh);
		g.setColor(0xdd0000);
		g.fillRect(0,lh+1,getWidth(),lh-1);
		g.setColor(0x0);
		drawStats(g, large, medium, lw, mw, enemyValue, enemyDamage, lh*2);
		g.setColor(0xffffff);
		
		g.setFont(medium);
		for(int x=0;x<3;++x){
			int dice = getDice(enemyValue[x]-heroValue[x]);
			g.setColor(dice > 20 ? 0xff0000 : 0xffffff);
			g.drawString(dice+"+ ", cw*(x+1), lh*3, Graphics.RIGHT|Graphics.BOTTOM);
		}
		g.setFont(large);
		for(int x=0;x<3;++x){
			g.setColor(getColor(heroProbability[x]));
			g.fillRect(cw*x,lh*3,lw+mw,lh);
			g.setColor(0x0);
			g.drawString(heroProbability[x]+"%", cw*(x+1), lh*4, Graphics.RIGHT|Graphics.BOTTOM);
			
			g.setColor(getColor(enemyProbability[x]));
			g.fillRect(cw*x,lh*4,lw+mw,lh);
			g.setColor(0x0);
			g.drawString(enemyProbability[x]+"%", cw*(x+1), lh*5, Graphics.RIGHT|Graphics.BOTTOM);
		}
		drawDamageProb(g, lh*5+3, cw);

		g.setColor(0xffffff);
		for(int x=0;x<3;++x)  
			g.drawLine((x+1)*(lw+mw),0,(x+1)*(lw+mw),getHeight());
		
		//-------------------		
		g.setColor(0xffff00);
		if(damageEdit) {
			g.setFont(medium);
			String s = String.valueOf(cursor_y == 0 ? heroDamage[cursor_x] : enemyDamage[cursor_x]); 
			g.drawString(s, (cursor_x+1)*(lw+mw), lh+lh*cursor_y, Graphics.RIGHT|Graphics.BOTTOM);
		}else{
			g.setFont(large);
			int cell = cursor_x / 2;
			int v = cursor_y == 0 ? heroValue[cell] : enemyValue[cell];
			int x = (cell+1)*cw;
			int d;
			if(cursor_x % 2 == 0) {//tens
				d = (v / 10);
				x -= large.stringWidth(String.valueOf(d));
			}else{
				d = v - (v / 10) * 10;
			}
			String s = String.valueOf(d);
			g.drawString(s, x-mw, lh+lh*cursor_y, Graphics.RIGHT|Graphics.BOTTOM);
		}
	}

	private void drawDamageProb(Graphics g, int y,int cw) {
		g.setColor(0xffffff);
		Font medium = getFont(Font.SIZE_MEDIUM);
		Font small = getFont(Font.SIZE_SMALL);
		Font font = (y+damageProbs.size()*medium.getHeight() < getHeight()) ? medium : small;
		g.setFont(font);
		int ch = font.getHeight();
		Enumeration keys = damageProbs.keys();
		int i=1;
		while (keys.hasMoreElements()) {
			Integer key = (Integer) keys.nextElement();
			Integer p = (Integer) damageProbs.get(key);
			g.setColor(getColor(100-p.intValue()));
			g.fillRect(cw,y+ch*(i-1),cw*2,ch);
			g.setColor(0x0);
			g.drawString(key.toString(), 2*cw-3, y+ch*i, Graphics.RIGHT|Graphics.BOTTOM);
			g.drawString(p+"%", 3*cw-3, y+ch*i, Graphics.RIGHT|Graphics.BOTTOM);
			++i;
		}
	}

	private int getDice(int value) {
		return value <= 2 ? 2 : value;
	}

	private void drawExpects(Graphics g, int x, int y, int expects) {
		String prefix = "";
		if(expects < 10)
			prefix += "0,0";
		else if(expects < 100)
			prefix += "0,";
		if(expects > 99) {
			int d = (expects / 100);
			expects -= d*100;
			prefix = d+",";
		}
		g.drawString(prefix+expects, x, y, Graphics.RIGHT|Graphics.TOP);
	}
	
	private void drawStats(Graphics g, Font large, Font medium, int lw, int mw, int[] value, int[] damage, int y) {
		g.setFont(large);
		for(int x=0;x<value.length;++x) 
			g.drawString(""+value[x], lw+x*(lw+mw), y, Graphics.RIGHT|Graphics.BOTTOM);
		g.setFont(medium);
		for(int x=0;x<damage.length;++x) { 
			g.drawString("/"+damage[x], lw+x*(lw+mw), y, Graphics.LEFT|Graphics.BOTTOM);
		}
		g.setColor(0xffffff);
		g.drawLine(0,y,getWidth(),y);
	}
	
	private int getColor(int probability) {
		if(probability < 50) {
			return 0xff0000 + probability * 2 * 0xff /100 * 0x100;
		} else {
			int i = (probability-50) * 2 * 0xff /100 * 0x10000;
			return 0xffff00 - i;
		}
	}

	private Font getFont(int size) {
		return Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, size);
	}

	int cursor_x = 0;
	int cursor_y = 1;
	private boolean damageEdit;
	
	protected void keyPressed(int keyCode) {
		switch(keyCode) {
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
			if(damageEdit) {
				int v = keyCode - KEY_NUM0;
				if(cursor_y == 0)
					heroDamage[cursor_x] = v;
				else
					enemyDamage[cursor_x] = v;
			} else {
				int cell = cursor_x / 2;
				int v = cursor_y == 0 ? heroValue[cell] : enemyValue[cell];
				int key = keyCode - KEY_NUM0;
				if(cursor_x % 2 == 0) {//tens
					v = v % 10 + key * 10;
				}else{
					v = (v / 10) * 10 + key;
				}
				if(cursor_y == 0)
					heroValue[cell] = v;
				else
					enemyValue[cell] = v;
			}
			cursor_x = ++cursor_x % (damageEdit ? 3 : 6);
			calcStats();
			repaint();
			break;
		case KEY_STAR:
		case KEY_POUND:
			damageEdit = !damageEdit;
			cursor_x = 0;
			repaint();
			break;
		default:
			switch(getGameAction(keyCode)){
			case LEFT:
				cursor_x = (--cursor_x < 0 ? (damageEdit ? 2 : 5): cursor_x) % (damageEdit ? 3 : 6);
				repaint();
				break;
			case RIGHT:
				cursor_x = ++cursor_x % (damageEdit ? 3 : 6);
				repaint();
				break;
			case UP:
				if(cursor_y == 0)
					++heroValue[cursor_x / 2];
				else
					++enemyValue[cursor_x / 2];
				calcStats();
				repaint();
				break;
			case DOWN:
				if(cursor_y == 0)
					--heroValue[cursor_x / 2];
				else
					--enemyValue[cursor_x / 2];
				calcStats();
				repaint();
				break;
			case FIRE:
				cursor_x = 0;
				cursor_y = ++cursor_y % 2;
				repaint();
				break;
			}
		}
	}

}