package common;

import java.util.Random;

public class DiceHand {
	private Random random;
	private int rollCount = 0;
	private int[] rollStats;
	private final Dice dice;

	public DiceHand(Dice dice) {
		this.dice = dice;
		rollStats = new int[dice.max-dice.min];
		random = new Random();
	}

	public int[] roll() {
		int[] roll = new int[dice.getCount()];
		int sum = 0;
		for (int i = 0; i < roll.length; ++i) {
			int r = random.nextInt(dice.getSides());
			roll[i] = r + 1;
		}
		sum = dice.getSum(roll);
		++rollCount;
		++rollStats[sum - dice.min];
		return roll;
	}

	public int normalize(int need2throw) {
		int min = dice.min;
		int max = dice.max;
		return need2throw < min ? min : need2throw > max ? max : need2throw;
	}

	public int getStatFor(int die) {
		int i = normalize(die) - dice.min;
		return i >= rollStats.length ? 0 : 100 * rollStats[i] / rollCount;
	}

	public int getNormalizedStatFor(int die) {
		int d = normalize(die) - dice.min;
		int max = 0;
		for (int i = 0; i < rollStats.length; ++i)
			max = Math.max(max, rollStats[i]);
		return d >= rollStats.length ? 0 : 100 * rollStats[d] / max;
	}

	public int getCount(int i) {
		return rollStats[normalize(i) - dice.min];
	}

	public int getCount() {
		return rollCount;
	}

}
