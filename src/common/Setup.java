package common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

import m.M;

public class Setup extends Form implements CommandListener {
	private static final Command COMMAND_ADD = new Command("Add", Command.SCREEN, 1);
	private static final Command COMMAND_DELETE = new Command("Delete", Command.ITEM, 2);
	private static final Command COMMAND_DEFAULT = new Command("Set default", Command.ITEM, 2);

	private TextField count;
	private TextField sides;
	private TextField drop;
	private ChoiceGroup list;
	private int[] profileIds;

	public Setup() {
		super("Setup");
		append(count = new TextField("Number of dice", "2", 2, TextField.NUMERIC));
		append(sides = new TextField("Sides", "6", 2, TextField.NUMERIC));
		append(drop = new TextField("Drop middle", "0", 2, TextField.NUMERIC));
		append(list = new ChoiceGroup("Profiles", ChoiceGroup.EXCLUSIVE));
		loadList();
		addCommand(COMMAND_ADD);
		addCommand(COMMAND_DELETE);
		addCommand(COMMAND_DEFAULT);
		addCommand(M.COMMAND_MAIN);
		setCommandListener(this);
	}

	private void loadList() {
		try {
			RecordStore store = RecordStore.openRecordStore(M.RMS_PROFILES, true);
			RecordEnumeration e = store.enumerateRecords(null, null, false);
			list.deleteAll();
			profileIds = new int[e.numRecords()];
			int i=0;
			int defaultId = ConfigView.getDefaultId();			
			while(e.hasNextElement()) {
				int id = e.nextRecordId();
				profileIds[i++] = id;
				Dice dice = ConfigView.createDice(store.getRecord(id));
				String string = dice.toString();
				if(id == defaultId) {
					list.append("* "+string, null);
					list.setSelectedIndex(i-1, true);
				} else
					list.append(string, null);
			}
		}catch (Exception e) {
			MsgBox.show(e);
		}
	}

	public void commandAction(Command c, Displayable d) {
		try {
			if (c == COMMAND_ADD) {
				save();
			}else if(c==COMMAND_DELETE){
				int selected = list.getSelectedIndex();
				if(selected != -1) {
					RecordStore store = null;
					try {
						store = RecordStore.openRecordStore(M.RMS_PROFILES, true);
						int id = profileIds[selected];
						store.deleteRecord(id);
						loadList();
					}finally{
						if(store != null)
							store.closeRecordStore();
					}
				}
			}else if(c == COMMAND_DEFAULT) {
				RecordStore store = RecordStore.openRecordStore(M.RMS_DEFAULT_PROFILE, true);
				byte[] buf = { (byte) profileIds[list.getSelectedIndex()] };
				store.addRecord(buf, 0, 1);
				store.closeRecordStore();
				loadList();
			}else if(c == M.COMMAND_MAIN) {
				M.showMain();
			}
		}catch(Exception e){
			MsgBox.show(e);
		}
	}

	private void save() throws Exception {
		RecordStore store = RecordStore.openRecordStore(M.RMS_PROFILES, true);
		byte[] record = createRecord();
		store.addRecord(record, 0, record.length);
		store.closeRecordStore();
		loadList();
	}

	private byte[] createRecord() throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int count = getValue(this.count);
		int sides = getValue(this.sides);
		int drop  = getValue(this.drop);
		buf.write(count);
		buf.write(sides);
		buf.write(drop);		
		return buf.toByteArray();
	}

	private int getValue(TextField text) {
		String string = text.getString();
		if(string == null || string.trim().equals(""))
			return 0;
		return Integer.parseInt(string);
	}

}
