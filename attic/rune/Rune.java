package rune;
import javax.microedition.midlet.MIDletStateChangeException;

import m.M;

public class Rune extends M {

	protected void startApp() throws MIDletStateChangeException {
		run(new RuneCalc());
	}

	protected String getHelp() {
		return "TODO";
	}

}
