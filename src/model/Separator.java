package model;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

public class Separator {
	private ArrayList<Record> accountingRecords;

	public Separator() {
		accountingRecords = new ArrayList<>();
	}

	/**Overwrite existing assigned keys if they overlap
	 * */
	public void addRecord(int activityLevel, double totalCost) {
		accountingRecords.add(new Record(activityLevel, totalCost));
		Collections.sort(accountingRecords);
	}

	public double[] highAndLowPoint() {
		double lowActivity = accountingRecords.get(0).getActivityLevel();
		double lowCost = accountingRecords.get(0).getTotalCost();
		double highActivity = accountingRecords.get(accountingRecords.size()-1).getActivityLevel();
		double highCost = accountingRecords.get(accountingRecords.size()-1).getTotalCost();

		double variablePerUnit = (highCost-lowCost)/(highActivity-lowActivity);
		double fixed = highCost - variablePerUnit*highActivity;

		return new double[] {fixed, variablePerUnit};
	}

	public double[] linearRegression() {
		Collection<Double> X = new ArrayList<>();
		Collection<Double> Y = new ArrayList<>();
		for(Record r : accountingRecords) {
			X.add(r.getActivityLevel()*1.0);
			Y.add(r.getTotalCost());
		}
		double pairs = X.size();
		double xSum = sum(X);
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
		double dp = 0;
		Iterator<N1> iterator1 = nums1.iterator();
		Iterator<N2> iterator2 = nums2.iterator();
		while (iterator1.hasNext()) {
			dp += iterator1.next().doubleValue()*iterator2.next().doubleValue();
		}
		return dp;
	}
}
