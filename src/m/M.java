package m;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

public class M extends MIDletAdapter {
	private MainScreen mainScreen = null;
	
	public void startApp() {
		show(getMainScreen());
	}
	
	public void show(Displayable screen) {
		Display.getDisplay(this).setCurrent(screen);
	}

	public MainScreen getMainScreen() {
		if(mainScreen == null)
			mainScreen = new MainScreen();
		return mainScreen;
	}
}
