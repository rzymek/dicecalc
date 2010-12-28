package common;

public class DiceDropMiddle extends Dice {
	private int[] tmp;
	private final int drop;
	public DiceDropMiddle(int count, int sides, int drop) {
		this.drop = drop;
		min = count - drop;
		max = (count - drop) * sides;
		p = new int[max - min + 1];
		tmp = new int[count];
		fill(count, sides, new int[count], 0);
		int space = calcSpace(count, sides);
		calcProb(space);
	}
	
	
	protected int process(int[] table) {
		copy(table, tmp);
		sort(tmp);
		int s = (table.length - drop)/2;
		int e = table.length - (table.length - drop)/2;
		int sum = 0;
		for (int i = 0; i < s; i++) {
			sum += tmp[i];
		}
		for (int i = e; i < table.length; i++) {
			sum += tmp[i];
		}
		return sum - min;
	}
	
	private void copy(int[] table, int[] tmp) {
		for (int i = 0; i < table.length; i++) {
			tmp[i] = table[i];
		}
	}


	public static void sort(int[] x) {
	    boolean doMore = true;
	    while (doMore) {
	        doMore = false;  // assume this is last pass over array
	        for (int i=0; i<x.length-1; i++) {
	            if (x[i] > x[i+1]) {
	               // exchange elements
	               int temp = x[i];  x[i] = x[i+1];  x[i+1] = temp;
	               doMore = true;  // after an exchange, must look again 
	            }
	        }
	    }
	}
}
