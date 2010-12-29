package m;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import coh.CoHCanvas;

import common.ConfigView;
import common.Help;
import common.Setup;

public abstract class M extends MIDlet implements CommandListener{
	public static final Command COMMAND_SETUP = new Command("Setup",Command.SCREEN,1);
	public static final Command COMMAND_MAIN = new Command("Main screen",Command.BACK,10);
	public static final Command COMMAND_CONFIG_VIEW = new Command("View profiles",Command.SCREEN,1);
	private static final Command COMMAND_HELP = new Command("Help",Command.SCREEN,1);
	private static final Command COMMAND_EXIT = new Command("Exit",Command.EXIT,0);

	public static M instance;
	public static final String RMS_PROFILES = "profiles";
	public static final String RMS_DEFAULT_PROFILE = "default";
	private ConfigView configView;
	private CoHCanvas mainScreen;
	
	public M() {
		instance = this;
		configView = new ConfigView();
	}
	public static MIDlet getInstance() {
		return instance;
	}
	
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
	}

	protected void pauseApp() {
	}

	protected abstract String getHelp();
	
	protected void run(CoHCanvas mainScreen) {
		this.mainScreen = mainScreen;
		mainScreen.addCommand(COMMAND_EXIT);
		mainScreen.addCommand(COMMAND_SETUP);
		mainScreen.addCommand(COMMAND_CONFIG_VIEW);
		mainScreen.setCommandListener(this);
		showMain();
	}

	public void commandAction(Command c, Displayable d) {
		if(c == COMMAND_EXIT) {
			notifyDestroyed();
		}else if(c == COMMAND_HELP) {
			Help.show(this, getHelp());
		}else if(c == COMMAND_CONFIG_VIEW) {
			show(configView);
		}else if(c == COMMAND_SETUP) {
			show(new Setup());
		}
	}
	
	public static void show(Displayable d) {
		Display.getDisplay(instance).setCurrent(d);
	}
	
	public static void showMain() {
		instance.mainScreen.setDice(instance.configView.getDefault());
		show(instance.mainScreen);
	}
}
