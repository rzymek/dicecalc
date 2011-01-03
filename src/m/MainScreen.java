package m;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class MainScreen extends Canvas {
	private GridPanel gridPanel;

	public MainScreen() {
		String[][][] texts = {
			{{"",    ""    },{"DV", "00"  },{"CAP", "+1"  },{"CAP",  "+2" },{"1 of 2",""    }},
			{{"AV",  "00"  },{"12+","100%"},{"+00%","100%"},{"+00%","100%"},{"+00%",  "100%"}},
			{{"KILL","(+4)"},{"12+","100%"},{"+00%","100%"},{"+00%","100%"},{"+00%",  "100%"}},
		};
		int N = Font.STYLE_PLAIN;
		int B = Font.STYLE_BOLD;
		int I = Font.STYLE_ITALIC;
		int E = B|4;
		int[][][] styles = {
			{{N,N},{B,E},{N,N},{N,N},{I,N}},
			{{B,B},{B,N},{N,N},{N,N},{I,N}},
			{{N,N},{B,N},{N,N},{N,N},{I,N}},
		};
		gridPanel = new GridPanel(texts);		
		gridPanel.setStyles(styles);
		gridPanel.invalidate(getWidth(), getHeight());
	}
	
	protected void paint(Graphics g) {
		g.setColor(0x000000);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(0xff8080);
		gridPanel.paint(g);
	}

}
