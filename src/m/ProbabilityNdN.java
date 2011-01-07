package m;

public class ProbabilityNdN implements ProbabilityProfile {
	private Dice dice;

	public ProbabilityNdN(int n, int d) {
		dice = new Dice(n,d);
	}
		
	public int get(int v) {
		if(v < 2)
			return 100;
		if(v > 12)
			return 0;
		return dice.getAtLeastProbability(v);
	}

}
