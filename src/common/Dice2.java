package common;


public class Dice2 {
	protected int min;
	protected int max;
	protected int[] p = null;
	
	protected Dice2() {
		
	}
	
	public Dice2(int count, int sides) {
		min = count;
		max = count*sides;
		p = new int[max-min+1];
		fill(count, sides, new int[count], 0);
		int space = calcSpace(count, sides);
		calcProb(space);			
	}

	protected void calcProb(int space) {
		for (int i = p.length-2; i >= 0; --i) 
			p[i] += p[i+1];
		for (int i = 0; i < p.length; ++i) 
			p[i] = 100 * p[i] / space;
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
	public int getMax() {
		return max;
	}
	public int getMin() {
		return min;
	}
}
