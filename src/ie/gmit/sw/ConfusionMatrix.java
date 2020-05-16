package ie.gmit.sw;

public class ConfusionMatrix {

	private float truePositive;
	private float falseNegative;
	private float falsePositive;
	private float trueNegative;
	
	public ConfusionMatrix() {
		//throw new UnsupportedOperationException();
	}
	
	public float getTruePositive() {
		return truePositive;
	}
	public void setTruePositive() {
		this.truePositive++;
	}
	public float getFalseNegative() {
		return falseNegative;
	}
	public void setFalseNegative() {
		this.falseNegative++;
	}
	public float getFalsePositive() {
		return falsePositive;
	}
	public void setFalsePositive() {
		this.falsePositive++;
	}
	public float getTrueNegative() {
		return trueNegative;
	}
	public void setTrueNegative() {
		this.trueNegative++;
	}
	
	public float getAccuracy() {
		return (this.truePositive + this.trueNegative) / 
				(this.truePositive + this.trueNegative + this.falsePositive + this.falseNegative);
	}
	
	public float getSensitvity() {
		return this.truePositive / (this.truePositive + this.falseNegative);
	}
	public float getSpecificty() {
		return this.trueNegative / (this.trueNegative + this.falsePositive);
	}
	public float getPrecision() {
		return this.truePositive / (this.truePositive + this.falsePositive);
	}

	@Override
	public String toString() {
		return "ConfusionMatrix [truePositive=" + truePositive + ", falseNegative=" + falseNegative + ", falsePositive="
				+ falsePositive + ", trueNegative=" + trueNegative + ", getAccuracy()=" + getAccuracy()
				+ ", getSensitvity()=" + getSensitvity() + ", getSpecificty()=" + getSpecificty() + ", getPrecision()="
				+ getPrecision() + "]";
	}
	
}
