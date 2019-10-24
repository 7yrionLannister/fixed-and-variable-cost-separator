package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Separator {
	private ArrayList<Record> accountingRecords;

	public Separator() {
		accountingRecords = new ArrayList<Record>();
	}

	/**Overwrite existing assigned keys if they overlap
	 * */
	public void addRecord(double activityLevel, double totalCost) {
		removeRecord(activityLevel);
		accountingRecords.add(new Record(activityLevel, totalCost));
		Collections.sort(accountingRecords);
	}
	
	public void removeRecord(double activityLevel) {
		accountingRecords.remove(new Record(activityLevel, Integer.MAX_VALUE));
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
			X.add(r.getActivityLevel());
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

	public void clear() {
		accountingRecords.clear();
	}

	public ArrayList<Record> getAccountingRecords() {
		return accountingRecords;
	}
	
	public double[] getBudgetedHighPoint(double fixed, double variable) {
		Record high = accountingRecords.get(accountingRecords.size()-1);
		return new double[] {high.getActivityLevel(), fixed + high.getActivityLevel()*variable};
	}
	
	public double[] getBudgetedLowPoint(double fixed, double variable) {
		Record low = accountingRecords.get(0);
		return new double[] {low.getActivityLevel(), fixed + low.getActivityLevel()*variable};
	}
}
