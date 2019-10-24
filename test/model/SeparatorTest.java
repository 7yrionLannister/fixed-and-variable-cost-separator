package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;

public class SeparatorTest {
	private Separator sep;
	private int highActivity;
	private double highCost;
	private int lowActivity;
	private double lowCost;

	private void setupStage1() {
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
		setupStage1();
		double expectedVariablePerUnit = (highCost-lowCost)/(highActivity-lowActivity);
		double expectedFixed = lowCost - expectedVariablePerUnit*lowActivity;
		double[] halp = sep.highAndLowPoint();
		//margin of error = 0.00000000001
		assertTrue(Math.abs(expectedFixed - halp[0]) < 0.00000000001, "The fixed cost is not correct");
		assertTrue(Math.abs(expectedVariablePerUnit - halp[1]) < 0.00000000001, "The variable cost per unit is not correct");
	}

	@Test
	public void linearRegression() {
		setupStage1();
		//values found using linear regression in Desmos.com/calculator, a reliable online graphing calculator
		double expectedFixed = 0.545454545455;
		double expectedVariablePerUnit = 0.636363636364;
		
		double[] lr = sep.linearRegression();
		assertTrue(Math.abs(expectedFixed-lr[0]) < 0.00000000001);
		assertTrue(Math.abs(expectedVariablePerUnit-lr[1]) < 0.00000000001);
	}
}
