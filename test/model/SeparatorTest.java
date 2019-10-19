package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SeparatorTest {
	private Separator sep;
	private int highActivity;
	private double highCost;
	private int lowActivity;
	private double lowCost;

	private void setupStage1() {
		sep = null;
	}

	private void setupStage2() {
		sep = new Separator();
	}

	private void setupStage3() {
		sep = new Separator();
		lowActivity = 1;
		lowCost = 1;
		highActivity = 14;
		highCost = 9;
		sep.addRecord(11, 8);
		sep.addRecord(lowActivity, lowCost);
		sep.addRecord(3, 2);
		sep.addRecord(9, 7);
		sep.addRecord(4, 4);
		sep.addRecord(6, 4);
		sep.addRecord(highActivity, highCost);
		sep.addRecord(8, 5);
	}
	
	@Test
	public void highAndLowPointTest() {
		setupStage3();
		double expectedVariablePerUnit = (highCost-lowCost)/(highActivity-lowActivity);
		double expectedFixed = lowCost - expectedVariablePerUnit*lowActivity;
		double[] halp = sep.highAndLowPoint();
		//margin of error = 0.00000000001
		assertTrue(Math.abs(expectedFixed - halp[0]) < 0.00000000001, "The fixed cost is not correct");
		assertTrue(Math.abs(expectedVariablePerUnit - halp[1]) < 0.00000000001, "The variable cost per unit is not correct");
	}

}
