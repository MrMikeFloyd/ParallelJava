package de.dhbw.parprog;

/**
 * Calculation class definition for messages that result in a calculation.
 * Contains the number to be multiplied with 42.
 * 
 * @author maik
 */
public class Calculation {

	private double factor;

	public Calculation(double factor) {
		this.factor = factor;
	}

	public double getFactor() {
		return factor;
	}

}
