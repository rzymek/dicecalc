package common;

import java.util.Random;

public class DiceHand {
	private final int count;
	private final int sides;
	private Random random;
	private int rollCount = 0;
	private int[] rollStats;

	public DiceHand(int count, int sides) {
		this.count = count;
		this.sides = sides;
		rollStats = new int[count * sides - count];
		random = new Random();
	}

	public int[] roll() {
		int[] roll = new int[count];
		int sum = 0;
		for (int i = 0; i < roll.length; ++i) {
			int r = random.nextInt(sides);
			roll[i] = r + 1;
			sum += roll[i];
		}
		++rollCount;
		++rollStats[sum - count];
		return roll;
	}

	public int normalize(int need2throw) {
		int min = count;
		int max = count * sides;
		return need2throw < min ? min : need2throw > max ? max : need2throw;
	}

	public int getStatFor(int die) {
		int i = normalize(die) - count;
		return i >= rollStats.length ? 0 : 100 * rollStats[i] / rollCount;
	}

	public int getNormalizedStatFor(int die) {
		int d = normalize(die) - count;
		int max = 0;
		for (int i = 0; i < rollStats.length; ++i)
			max = Math.max(max, rollStats[i]);
		return d >= rollStats.length ? 0 : 100 * rollStats[d] / max;
	}

	public int getCount(int i) {
		return rollStats[normalize(i) - count];
	}

	public int getCount() {
		return rollCount;
	}

}
