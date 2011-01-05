package m;

public class ProbabilityTable extends EditableGridPanel {

	private ProbabilityProfile p;
	private ProbabilityProfile p1of2;

	public ProbabilityTable(String[][][] texts, int[][] editable, ProbabilityProfile p) {
		super(texts, editable, createColor(texts));
		this.p = p;
		p1of2 = new Probability1of2(p);
		recalculate();
	}
	
	private static int[][] createColor(String[][][] texts) {
		return new int[texts.length][texts[0].length];
	}
	
	public static int getProbabilityColor(int probability) {
		if(probability < 50) {
			return 0xff0000 + probability * 2 * 0xff /100 * 0x100;
		} else {
			int i = (probability-50) * 2 * 0xff /100 * 0x10000;
			return 0xffff00 - i;
		}
	}

	protected void recalculate() {
		int av = editable[0][2];
		int dv = editable[1][2];
		int v = dv-av;
		int k = dv-av+4;
		
		int p0 = p.get(v);
		int pk = p.get(k);
		int p1 = p.get(v-1);
		int p1k = p.get(k-1);
		int p2 = p.get(v-2);
		int p2k = p.get(k-2);
		int p1o2 = p1of2.get(v);
		int p1o2k = p1of2.get(k);
		
		setText(1, 1, normalize(v)+" ", p0);
		setText(1, 2, normalize(k)+" ", pk);
		
		setText(2, 1, "+"+(p1-p0)+"%", p1);
		setText(2, 2, "+"+(p1k-pk)+"%", p1k);
		
		setText(3, 1, "+"+(p2-p1)+"%", p2);
		setText(3, 2, "+"+(p2k-p1k)+"%", p2k);
		
		setText(4, 1, "+"+(p1o2-p0)+"%", p1o2);
		setText(4, 2, "+"+(p1o2k-pk)+"%", p1o2k);
	}

	private void setText(int x, int y, String a, int p) {
		setColor(x,y, getProbabilityColor(p));
		setText(x, y, p+"%", a);
	}

	private int normalize(int v) {
		if(v < 2)
			v = 2;
		if(v > 12)
			v = 13;
		return v;
	}

	public void setCurrentValue(int value) {
		super.setCurrentValue(value);
		recalculate();
	}

}
