package scs;

import javax.microedition.lcdui.Displayable;

import m.MIDletAdapter;

public class M extends MIDletAdapter {

	protected Displayable createMainScreen() {
		return new SCSMainScreen(this);
	}

}
