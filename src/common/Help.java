package common;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;


public class Help extends Form implements CommandListener{

	private final MIDlet midlet;
	private Displayable backScreen;

	public Help(String string, MIDlet midlet) {
		super("Help");
		this.midlet = midlet;
		append(string);
		addCommand(new Command("Back",Command.BACK,0));
		setCommandListener(this);
		backScreen = Display.getDisplay(midlet).getCurrent();
	}

	public static void show(MIDlet base, String help) {
		Help form = new Help(help,base);
		Display.getDisplay(base).setCurrent(form);
	}

	public void commandAction(Command c, Displayable d) {
		Display.getDisplay(midlet).setCurrent(backScreen);
	}

}
