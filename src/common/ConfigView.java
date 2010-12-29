package common;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

import m.M;

public class ConfigView extends Canvas implements CommandListener {
	
	private Font FONT_LARGE = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE);
	private Font FONT_SMALL = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);
	private ChartGraphics chart;
	private Dice dice;
	private RecordEnumeration enumerator;
	private RecordStore store;
	
	public ConfigView() {
		addCommand(M.COMMAND_MAIN);
		setCommandListener(this);
		try {
			store = RecordStore.openRecordStore(M.RMS_PROFILES, true);
			enumerator = store.enumerateRecords(null, null, true);
			setDice(getDefault());
		}catch (Exception e) {
			MsgBox.show(e);
		}
	}

	public Dice getDefault() {
		try {
			int defaultId = getDefaultId();
			if(defaultId == -1)
				return new Dice(2,6);
			else
				return createDice(store.getRecord(defaultId));
		}catch(Exception e){
			MsgBox.show(e);
			return new Dice(2,6);
		}
	}

	public static int getDefaultId() throws Exception {
		RecordStore store = RecordStore.openRecordStore(M.RMS_DEFAULT_PROFILE, true);
		RecordEnumeration enumeration = store.enumerateRecords(null, null, false);
		if(enumeration.hasNextElement()) 
			return enumeration.nextRecord()[0];
		else
			return -1;
	}
	
	public void setDice(Dice dice){
		if(dice == null)
			return;
		this.dice = dice;
		int textBoxHeight = FONT_LARGE.getHeight()+1;
		chart = new ChartGraphics(0, textBoxHeight, getWidth(), getHeight());
		chart.setGrid(dice.getMax() - dice.getMin());
		repaint();
	}
	
	protected void paint(Graphics g) {
		g.setColor(0x000000);
		g.fillRect(0,0,getWidth(), getHeight());

		if(dice != null) {
			g.setColor(0xffffff);
			g.setFont(FONT_LARGE);
			g.drawString(dice.toString(), 0, 0, Graphics.TOP|Graphics.LEFT);
			
			g.setFont(FONT_SMALL);
			chart.setGraphics(g);
			int max=0;
			for(int i=dice.getMin(); i<=dice.getMax(); ++i) {
				int v = dice.getProbability(i);
				max = Math.max(max, v);
			}
			for(int i=dice.getMin(); i<=dice.getMax(); ++i) {
				int v = dice.getProbability(i);
				v = 100*v/max;
				chart.drawBar(i-dice.getMin(), v, String.valueOf(i));
			}
		}
	}

	protected void keyPressed(int keyCode) {
		try {
			byte[] buf = null;
			switch(getGameAction(keyCode)){
			case LEFT:
				if (enumerator.hasPreviousElement()) 
					buf = enumerator.previousRecord();
				break;
			case RIGHT:
				if (enumerator.hasNextElement())
					buf = enumerator.nextRecord();
				break;
			}
			setDice(createDice(buf));
		} catch (Exception e) {
			MsgBox.show(e);
		}
	}

	public static Dice createDice(byte[] buf) {
		if(buf != null) {
			if(buf[2] == 0)
				return new Dice(buf[0], buf[1]);
			else
				return new DiceDropMiddle(buf[0], buf[1], buf[2]);
		}
		return null;
	}

	public void commandAction(Command cmd, Displayable d) {
		if(cmd == M.COMMAND_MAIN)
			M.showMain();
	}
}
