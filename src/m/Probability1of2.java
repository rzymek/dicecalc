package m;

public class Probability1of2 implements ProbabilityProfile {

	private final ProbabilityProfile p;

	public Probability1of2(ProbabilityProfile p) {
		this.p = p;
	}

	public int get(int v) {
		//P(a)+P(b)-P(a)*P(b)
		return (p.get(v)*100+p.get(v)*100-p.get(v)*p.get(v))/100;
	}

}
