package coh;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

import common.ChartGraphics;
import common.ConfigView;
import common.Dice;
import common.DiceGraphics;
import common.DiceHand;
import common.GridGraphics;

public class CoHCanvas extends Canvas {
	
	private static final int MIN_VALUE = -9;
	private static final int MAX_VALUE = 99;
	private static final int DIE_MARGIN = 2;
	private static final int DIE_SIZE = 22;
	private static final int BIG_DICE_DELAY = 4;//sec
	private Font larger;
	private Font smaller;
	private int cellwidth;
	private int cellheight;
	
	GameData data = new GameData();
	boolean bigDice=false;

	
	private GridGraphics gx;
	private Dice dice;
	private DiceHand hand;
	private DiceGraphics gd;
	private ChartGraphics gp;
	private int bigDiceSize;
	private Player diceSound;
	private ChartGraphics gdp;

	public CoHCanvas() {
		larger = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE);
		smaller = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_MEDIUM);
		cellwidth = Math.max(larger.stringWidth("00"), smaller.stringWidth("100%"));
		cellheight = Math.max(larger.getHeight(), smaller.getHeight()*2);
		gx = new GridGraphics(cellwidth,cellheight);
		gp = new ChartGraphics(3+(1+data.dmods.length)*cellwidth,0,getWidth(),(1+data.amods.length)*cellheight);
		gp.setGrid(data.amods.length * data.dmods.length);
		gdp = new ChartGraphics(2*DIE_MARGIN+DIE_SIZE, (1+data.amods.length)*cellheight + 2*DIE_MARGIN,getWidth(),getHeight());
		gdp.setGrid(11);
		gd = new DiceGraphics(0,(1+data.amods.length)*cellheight,getWidth(),getHeight());
		bigDiceSize = Math.min(gd.getWidth()/2-DIE_MARGIN*3, gd.getHeight()-DIE_MARGIN*2);
		try{
			diceSound = Manager.createPlayer(getClass().getResourceAsStream("/dice.amr"),"audio/amr");
			diceSound.prefetch();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void setDice(Dice dice) {
		this.dice = dice;
		hand = new DiceHand(2, 6);		
	}
	
	public void paint(Graphics g) {
		paintGrid(g);
		paintProbChart(g);
		if(data.roll != null) {
			if(bigDice)
				paintBigDice(g);
			else {
				paintDiceDistributionChart(g);		
				paintDice(g);
			}
			g.setFont(larger);g.setColor(0xffffff);
			g.drawString(String.valueOf(data.roll[0]+data.roll[1]), 1, 1, Graphics.TOP|Graphics.LEFT);
			g.setFont(smaller);g.setColor(0x808080);
			g.drawString(String.valueOf(hand.getCount()), cellwidth, cellheight, Graphics.BOTTOM|Graphics.RIGHT);
		}
	}

	private void paintBigDice(Graphics g) {
		gd.setGraphics(g);
		gd.drawDie(data.roll[0], DIE_MARGIN, DIE_MARGIN, bigDiceSize,0x000000,0xffffff);
		gd.drawDie(data.roll[1], DIE_MARGIN+bigDiceSize+DIE_MARGIN, DIE_MARGIN, bigDiceSize,0x000000,0xffffff);
	}

	private void paintProbChart(Graphics g) {
		gp.setGraphics(g);
		int rollProbability = 100-dice.getProbability(getCurrentRoll());
		if(data.roll!=null && data.markRoll)
			gp.fillValue(rollProbability,0x8080ff);
		for(int j=0;j<data.amods.length;++j){
			for(int i=0;i<data.dmods.length;++i){
				int need2throw = hand.normalize(getValue(data.dv) - getValue(data.av)-data.amods[j]+data.dmods[i]);
				int probability = dice.getProbability(need2throw);
				int p = i*3+j;
				gp.drawBar(p, probability, String.valueOf(need2throw));
			}
		}
		gp.drawLines();
		if(data.roll!=null && data.markRoll) 
			gp.drawLine(rollProbability,0x0000ff);
	}

	private void paintDice(Graphics g) {
		gd.setGraphics(g);
		g.setColor(0xffffff);
		int fg = 0xffffff;
		int bg = 0x000000;
		gd.drawDie(data.roll[0], DIE_MARGIN, DIE_MARGIN, DIE_SIZE,bg,fg);
		gd.drawDie(data.roll[1], DIE_MARGIN, 2*DIE_MARGIN+DIE_SIZE, DIE_SIZE,bg,fg);
		String s = String.valueOf(data.roll[0]+data.roll[1]);
		int x = DIE_MARGIN + (DIE_SIZE - larger.stringWidth(s))/2;
		g.setColor(fg);
		int y = 3*DIE_MARGIN+2*DIE_SIZE;
		g.setFont(larger);
		gd.draw(s, x,y, Graphics.LEFT|Graphics.TOP);
		g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	}

	private void paintDiceDistributionChart(Graphics g) {
		gdp.setGraphics(g);
		gdp.drawLines();
		for(int i=2;i<=12;++i){
			gdp.drawBar(i-2, hand.getNormalizedStatFor(i), String.valueOf(i));
		}
		g.setColor(0x606060);
		int xhalf = gdp.x1+gdp.getWidth()/2;
		g.drawLine(gdp.x1, gdp.y2, 	xhalf, gdp.y1);
		g.drawLine(xhalf, gdp.y1, gdp.x1+gdp.getWidth(), gdp.y2);
	}

	private void paintGrid(Graphics g) {
		gx.setGraphcs(g);
		int roll = getCurrentRoll();
		gx.clear(0x000000, getWidth(), getHeight());
		gx.fill(0xffffff, data.selected_edit, 1-data.selected_edit);
		g.setColor(0x808080);
		Font smallest = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		gx.drawString("AV",-1,0, smallest, Graphics.LEFT | Graphics.TOP);
		gx.drawDigits(data.av,0,1, larger, Graphics.RIGHT | Graphics.BOTTOM);
		gx.drawString("DV",0,-1, smallest, Graphics.LEFT | Graphics.TOP);
		gx.drawDigits(data.dv,1,0, larger, Graphics.RIGHT | Graphics.BOTTOM);
		g.setColor(0xaaaaaa);
		for(int i=1;i<data.amods.length;++i) {
//			gx.fill(0x0000ff, 0, i+1);
//			g.setColor(0x000000);
			gx.drawString("CAP", 0, i, smaller, Graphics.RIGHT | Graphics.TOP);
			gx.drawString(" +"+data.amods[i], 0, i+1, smaller, Graphics.RIGHT | Graphics.BOTTOM);
		}
		for(int i=1;i<data.dmods.length;++i) {
//			gx.fill(0xff0000, i+1, 0);
//			g.setColor(0x000000);
			gx.drawString("+"+data.dmods[i],i+1,0, smaller, Graphics.RIGHT | Graphics.BOTTOM);
		}
		
		g.setColor(0x0000ff);
		gx.drawSelectedDigit(data.editable[data.selected_edit], data.selected_digit, 
				data.selected_edit, 1 - data.selected_edit, 
				larger, Graphics.RIGHT | Graphics.BOTTOM);
		for(int i=0;i<data.dmods.length;++i){
			for(int j=0;j<data.amods.length;++j){
				int x = 1+i;
				int y = 1+j;
				int need2throw = hand.normalize(getValue(data.dv) - getValue(data.av)-data.amods[j]+data.dmods[i]);
				int probability = dice.getProbability(need2throw);
				gx.fillPercent(probability, x,y);
				if(roll >= need2throw && data.markRoll)
					gx.drawFrame(0x0000ff, x,y);
				g.setColor(0x000000);
				gx.drawString(need2throw+"+",x,y-1,smaller,Graphics.RIGHT|Graphics.TOP);
				gx.drawString(probability+"%",x,y,smaller,Graphics.RIGHT|Graphics.BOTTOM);				
			}
		}
		g.setColor(0x606060);
		gx.drawGrid(1+data.dmods.length,1+data.amods.length);
	}
	
	private int getCurrentRoll() {
		return data.roll == null ? -1 : data.roll[0]+data.roll[1];
	}

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
			data.editable[data.selected_edit][data.selected_digit] =  (keyCode - KEY_NUM0);
			nextDigit(+1);
			break;	
		case KEY_STAR:
		case KEY_POUND:
			rollDice();
			repaint();
			return;
		default:
			switch(getGameAction(keyCode)){
			case LEFT:
				nextDigit(-1);
				break;
			case RIGHT:
				nextDigit(+1);
				break;
			case UP:
				add(+1);
				break;
			case DOWN:
				add(-1);
				break;
			case FIRE:
				data.selected_edit =  ((data.selected_edit+1)%data.editable.length);
				data.selected_digit = data.editable[data.selected_edit][0] > 0 ? 0 : 1;
				break;
			default:
				return;
			}
		}
		data.markRoll = false;
		repaint();		
	}

	Timer timer = new Timer();
	private void rollDice() {
		if(!bigDice){
			bigDice = true;
			try {
				if(diceSound != null)
					diceSound.start();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			data.roll = hand.roll();
			timer.schedule(new TimerTask(){
				public void run() {
					bigDice = false;
					repaint();
				}
			}, BIG_DICE_DELAY*1000);
			data.markRoll = true;
		}
	}

	private void add(int i) {
		int v = getValue(data.editable[data.selected_edit]) + i;
		if(MIN_VALUE <= v && v <= MAX_VALUE) {
			data.editable[data.selected_edit][0] = v /10;
			data.editable[data.selected_edit][1] = v - data.editable[data.selected_edit][0] * 10;
		}
	}

	private int getValue(int[] digits) {
		return digits[0] * 10 + digits[1];
	}

	private void nextDigit(int direction) {
		data.selected_digit += direction;
		if(data.selected_digit >= data.editable[data.selected_edit].length) { 
//			selected_edit =  ((selected_edit+direction) % editable.length);
//			selected_digit =  (editable[selected_edit].length - 1);
			if(data.editable[data.selected_edit][0] > 0)
				data.selected_digit = 0;
			else
				data.selected_digit = 1;
		}else if(data.selected_digit < 0) {
//			selected_edit =  ((selected_edit-direction) % editable.length);
			data.selected_digit = (data.editable[data.selected_edit].length - 1);
		}
	}

}