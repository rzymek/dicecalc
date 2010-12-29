package common;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import m.M;

public class MsgBox implements CommandListener {

	private static final Command CMD_OK = new Command("OK", Command.OK, 0);
	private final Displayable current;

	public MsgBox(Displayable current) {
		this.current = current;
	}

	public static void show(String title, String text, AlertType type) {
		Alert alert = new Alert(title, text, null, type);
		alert.addCommand(CMD_OK);
		Displayable current = Display.getDisplay(M.getInstance()).getCurrent();
		alert.setCommandListener(new MsgBox(current));
		show(alert);
	}

	private static void show(Displayable d) {
		Display.getDisplay(M.getInstance()).setCurrent(d);
	}

	public void commandAction(Command cmd, Displayable d) {
		if(cmd == CMD_OK)
			show(current);
	}

	public static void show(Exception e) {
		show("Exception", e.toString(), AlertType.ERROR);
		e.printStackTrace();
	}
}
