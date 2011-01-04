package m;


public class Dice {
	protected int min;
	protected int max;
	protected int[] p = null;
	private int[] atLeastP = null;
	
	protected Dice() {
		
	}
	
	public Dice(int count, int sides) {
		min = count;
		max = count*sides;
		p = new int[max-min+1];
		fill(count, sides, new int[count], 0);
		int space = calcSpace(count, sides);
		calcProb(space);			
	}

	protected void calcProb(int space) {
		atLeastP = new int[max-min+1];
		for (int i = 0; i < p.length; ++i) 
			atLeastP[i] = p[i];
		for (int i = atLeastP.length-2; i >= 0; --i) 
			atLeastP[i] += atLeastP[i+1];
		for (int i = 0; i < p.length; ++i) { 
			p[i] = 100 * p[i] / space;
			atLeastP[i] = 100 * atLeastP[i] / space;
		}
	}

	protected int calcSpace(int count, int sides) {
		int space = 1;
		for(int i=0;i<count;++i) 
			space *=sides;
		return space;
	}
	
	protected void fill(int count, int sides, int[] table, int i) {
		if(i==count) {
			int value = process(table);
			p[value]++;
			return;
		}
		
		for (int die = 1; die <= sides; ++die) {
			table[i]=die;
			fill(count,sides, table, i+1);
		}
	}

	protected int process(int[] table) {
		int sum=0;
		for (int i = 0; i < table.length; i++) 
			sum+=table[i];
		return sum - min;
	}
	
	public int getProbability(int value) {
		return p[value-min];
	}
	public int getAtLeastProbability(int value) {
		return atLeastP[value-min];
	}
	public int getMax() {
		return max;
	}
	public int getMin() {
		return min;
	}
	public int getSides(){
		return max/min;
	}
	public int getCount() {
		return min;
	}
	public int[] resetUncountedDice(int[] table) {
		return table;
	}
	public String toString() {
		return getCount()+"d"+getSides();
	}

	public int getSum(int[] roll) {
		int sum=0;
		for (int i = 0; i < roll.length; i++) {
			sum += roll[i];
		}
		return sum;
	}
}
