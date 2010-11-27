package coh;
import javax.microedition.midlet.MIDletStateChangeException;

import m.M;

public class CoH extends M {

	protected void startApp() throws MIDletStateChangeException {
		run(new CoHCanvas());
	}

	protected String getHelp() {
		return "SELECT key switches between AV and DV input (white background). " +
				"UP/DOWN keys increase/decrease currently selected value. " +
				"LEFT/RIGHT lets you select individual digits (in blue). Numeric keys set the " +
				"selected digit. " +
				"\n\n" +
				"Pound (#) or star (*) key rolls the dice. Matching cells in the grid are marked with blue border. \n" +
				"The bar chart on the bottom shows distribution of all dice roll in this session. Statistically the bars should create " +
				"a triangle as marked by the oblique line.";				
	}
}
