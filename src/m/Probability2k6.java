package m;

public class Probability2k6 implements ProbabilityProfile {
	private Dice dice;

	public Probability2k6() {
		dice = new Dice(2,6);
	}
		
	public int get(int v) {
		if(v < 2)
			return 100;
		if(v > 12)
			return 0;
		return dice.getAtLeastProbability(v);
	}

}
