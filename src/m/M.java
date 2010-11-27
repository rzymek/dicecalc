package m;


import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import common.Help;


public abstract class M extends MIDlet implements CommandListener{

	private static final Command COMMAND_HELP = new Command("Help",Command.SCREEN,1);
	private static final Command COMMAND_EXIT = new Command("Exit",Command.EXIT,0);

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
	}

	protected void pauseApp() {
	}

	protected abstract String getHelp();
	
	protected void run(Displayable mainScreen) {
		mainScreen.addCommand(COMMAND_EXIT);
		mainScreen.addCommand(COMMAND_HELP);
		mainScreen.setCommandListener(this);
		Display.getDisplay(this).setCurrent(mainScreen);
	}

	public void commandAction(Command c, Displayable d) {
		if(c == COMMAND_EXIT) {
			notifyDestroyed();
		}else if(c == COMMAND_HELP) {
			Help.show(this, getHelp());
		}
	}
}
