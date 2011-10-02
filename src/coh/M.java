package coh;

import javax.microedition.lcdui.Displayable;

import m.MIDletAdapter;

public class M extends MIDletAdapter {
	public Displayable createMainScreen() {
		return new MainScreen();
	}
}
