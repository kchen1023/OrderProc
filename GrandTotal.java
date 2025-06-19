package processor;

public class GrandTotal {
	private double value;

	public GrandTotal(double startValue) {
		value = startValue;
	}

	public void add(double value) {
		this.value += value;
	}

	public double getValue() {
		return value;
	}

	public String toString() {
		return Double.toString(value);
	}
}
