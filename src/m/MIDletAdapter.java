package m;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public abstract class MIDletAdapter extends MIDlet {
	protected Displayable mainScreen = null;
	public void startApp() {
		show(getMainScreen());
	}
	
	public Displayable getMainScreen() {
		if(mainScreen == null)
			mainScreen = createMainScreen();
		return mainScreen;
	}
	
	protected abstract Displayable createMainScreen();

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
	}

	public void show(Displayable screen) {
		Display.getDisplay(this).setCurrent(screen);
	}

	protected void pauseApp() {
	}
	
}
