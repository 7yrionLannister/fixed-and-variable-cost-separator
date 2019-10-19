package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class Separator {
	private TreeMap<Integer, Double> accountingRecords;
	
	public Separator() {
		accountingRecords = new TreeMap<>();
	}
	
	/**Overwrite existing assigned keys if they overlap
	 * */
	public void addRecord(int activityLevel, double totalCost) {
		accountingRecords.put(activityLevel, totalCost);
	}
	
	
	public double[] highAndLowPoint() {
		double lowActivity = accountingRecords.firstKey();
		double lowCost = accountingRecords.get((int)lowActivity);
		double highActivity = accountingRecords.lastKey();
		double highCost = accountingRecords.get((int)highActivity);
		
		double variablePerUnit = (highCost-lowCost)/(highActivity-lowActivity);
		double fixed = highCost - variablePerUnit*highActivity;
		
		return new double[] {fixed, variablePerUnit};
	}
	
	public double[] linearRegression() {
		Collection<Integer> X = accountingRecords.keySet();
		double pairs = X.size();
		double xSum = sum(X);
		Collection<Double> Y = accountingRecords.values();
		double ySum = sum(Y);
		double xySum = dotProduct(X, Y);
		double x2Sum = dotProduct(X, X);
		double xSum2 = xSum*xSum;
		
		double fixed = (ySum*x2Sum - xSum*xySum)/(pairs*x2Sum - xSum2);
		double variablePerUnit = (pairs*xySum - xSum*ySum)/(pairs*x2Sum - xSum2);
		
		return new double[] {fixed, variablePerUnit};
	}
	
	public <N extends Number> double sum(Collection<N> nums) {
		double sum = 0;
		for (Number n : nums) {
			sum += n.doubleValue();
		}
		return sum;
	}
	
	public <N1 extends Number, N2 extends Number> double dotProduct(Collection<N1> nums1, Collection<N2> nums2) {
		double dp = 1;
		Iterator<N1> iterator1 = nums1.iterator();
		Iterator<N2> iterator2 = nums2.iterator();
		while (iterator1.hasNext()) {
			dp += iterator1.next().doubleValue()*iterator2.next().doubleValue();
		}
		return dp;
	}
}
