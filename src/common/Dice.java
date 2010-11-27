package common;

import java.util.Random;

public class Dice {
	private Random random = new Random();
	private final int nr;

	private int[] probability;
	
	public Dice(int nr, int sides) {
		this.nr = nr;
		
		probability = new int[nr*sides - nr + 1];
		int sum = 0;
		for(int i=0;i<probability.length/2+1;++i){
			probability[i]=i+1;
			sum += i+1;
			if(probability.length-i-1 != i){
				probability[probability.length-i-1]=i+1;
				sum += i+1;
			}
		}
		for(int i=0;i<probability.length;++i){
			probability[i] = 100*100 * probability[i] / sum;
		}		
		for(int i=probability.length-2;i>=0;--i){
			probability[i] += probability[i+1];
			probability[i+1] += 50;
			probability[i+1] /= 100;
		}
		probability[0] += 50;
		probability[0] /= 100;
		rollStats = new int[probability.length];
	}

	public int getProbability(int need2throw) {
		int i = normalize(need2throw)-nr;
		return i>=probability.length ? 0 : probability[i];
	}

	public int normalize(int need2throw) {
		return need2throw <= nr ? nr :
			need2throw >= nr+probability.length? probability.length+nr : need2throw;
	}
	int[] rollStats;
	int rollCount =0;
	public int[] roll() {
		int[] roll = new int[nr];
		int sides = (probability.length + nr -1)/2;
		int sum = 0;
		for(int i=0;i<roll.length;++i){
			int r = random.nextInt(sides);
			roll[i] = r+1;
			sum += roll[i];
		}
		++rollCount;
		++rollStats[sum-nr];
		return roll;
	}
	
	public int getStatFor(int die) {
		int i = normalize(die) - nr;
		return i>=rollStats.length ? 0 : 100 * rollStats[i] / rollCount;
	}

	public int getNormalizedStatFor(int die) {
		int d = normalize(die) - nr;
		int max = 0;
		for(int i=0;i<rollStats.length;++i)
			max = Math.max(max, rollStats[i]);
		return d>=rollStats.length ? 0 : 100 * rollStats[d] / max;
	}

	public int getCount(int i) {
		return rollStats[normalize(i)-nr];
	}

	public int getCount() {
		return rollCount;
	}
}
