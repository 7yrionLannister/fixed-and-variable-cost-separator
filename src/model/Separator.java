package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class Separator {
	/**The list of records that are going to lead to a budget function
	 * */
	private ArrayList<Record> accountingRecords;
	/**This boolean tells whether or not the budget function stored in here is trusted or not
	 * */
	private boolean trustedEquation;
	/**The double stores the fixed part of the mixed cost of the records
	 * */
	private double fixed;
	/**The double stores the variable cost per unit of the records
	 * */
	private double variablePerUnit;
	
	/**Returns an instance of this class with an empty record list
	 * */
	public Separator() {
		accountingRecords = new ArrayList<Record>();
	}

	/**The method allows to register a new record overwriting existing assigned keys(activity levels) if they already exist
	 * @param activityLevel The activity level of the new record
	 * @param totalCost The mixed cost associated to the specified activity level
	 * */
	public void addRecord(double activityLevel, double totalCost) {
		removeRecord(activityLevel);
		accountingRecords.add(new Record(activityLevel, totalCost));
		Collections.sort(accountingRecords);
	}
	
	/**The method allows to remove records of the list specifying their key(activity level)
	 * @param activityLevel The key of the record to be removed
	 * */
	public void removeRecord(double activityLevel) {
		accountingRecords.remove(new Record(activityLevel, Integer.MAX_VALUE));
		trustedEquation = false;
	}

	/**This method allows to compute the budget function for the set of records stored in the list by the high and low point method<br>
	 *  After this method is called, the fixed and variable cost per unit are stored in their corresponding attributes for further retrieval
	 * */
	public void budgetFunctionByHighAndLowPoint() {
		double lowActivity = accountingRecords.get(0).getActivityLevel();
		double lowCost = accountingRecords.get(0).getTotalCost();
		double highActivity = accountingRecords.get(accountingRecords.size()-1).getActivityLevel();
		double highCost = accountingRecords.get(accountingRecords.size()-1).getTotalCost();

		variablePerUnit = (highCost-lowCost)/(highActivity-lowActivity);
		fixed = highCost - variablePerUnit*highActivity;
		trustedEquation = true;
	}

	/**This method allows to compute the budget function for the set of records stored in the list by the linear regression method<br>
	 *  After this method is called, the fixed and variable cost per unit are stored in their corresponding attributes for further retrieval
	 * */
	public void budgetFunctionByLinearRegression() {
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

		fixed = (ySum*x2Sum - xSum*xySum)/(pairs*x2Sum - xSum2);
		variablePerUnit = (pairs*xySum - xSum*ySum)/(pairs*x2Sum - xSum2);
		trustedEquation = true;
	}

	/**This method computes the sum of a collection of numbers
	 * @param nums The collection of numbers to be added
	 * @return The double value of the addition of the numbers 
	 * */
	public <N extends Number> double sum(Collection<N> nums) {
		double sum = 0;
		for (Number n : nums) {
			sum += n.doubleValue();
		}
		return sum;
	}

	/**This method computes the dot product of two collections of numbers
	 * @param nums1 One of the collections of numbers to be multiplied
	 * @param nums2 The second collection of numbers to be multiplied
	 * @return The double value resulting of multiplying the components of the collections one by one and adding the results 
	 * */
	public <N1 extends Number, N2 extends Number> double dotProduct(Collection<N1> nums1, Collection<N2> nums2) {
		double dp = 0;
		Iterator<N1> iterator1 = nums1.iterator();
		Iterator<N2> iterator2 = nums2.iterator();
		while (iterator1.hasNext()) {
			dp += iterator1.next().doubleValue()*iterator2.next().doubleValue();
		}
		return dp;
	}

	/**This method makes the list of records to be empty
	 * */
	public void clear() {
		accountingRecords.clear();
		trustedEquation = false;
	}

	/**This method allows to obtain the accounting records that are registered at the moment
	 * @return An arraylist containing all the records that have been added but not removed
	 * */
	public ArrayList<Record> getAccountingRecords() {
		return accountingRecords;
	}
	
	/**The method allows to obtain the point in which the budget function puts the highest activity level
	 * @return A double array of two components, the highest activity level and its corresponding budgeted cost 
	 * */
	public double[] getBudgetedHighPoint() {
		Record high = accountingRecords.get(accountingRecords.size()-1);
		return new double[] {high.getActivityLevel(), fixed + high.getActivityLevel()*variablePerUnit};
	}
	
	/**The method allows to obtain the point in which the budget function puts the lowest activity level
	 * @return A double array of two components, the lowest activity level and its corresponding budgeted cost 
	 * */
	public double[] getBudgetedLowPoint() {
		Record low = accountingRecords.get(0);
		return new double[] {low.getActivityLevel(), fixed + low.getActivityLevel()*variablePerUnit};
	}
	
	/**The method allows to obtain the cost that the budget function yields for a specified activity level
	 * @return A double containing the computed budgeted cost 
	 * */
	public double getCostEstimate(double activityLevel) {
		return fixed + variablePerUnit*activityLevel;
	}

	/**This method allows to obtain the budgeted fixed part of the cost for the set of records
	 * @return The double value of the fixed budgeted cost
	 * */
	public double getFixed() {
		return fixed;
	}

	/**This method allows to obtain the budgeted variable cost per unit for the set of records
	 * @return The double value of the budgeted variable cost per unit
	 * */
	public double getVariablePerUnit() {
		return variablePerUnit;
	}

	/**This boolean allows to know whether the budget function is trusted or not, that is, if no data has been added or removed since the last time the function was computed- 
	 * @return TRUE if no data was added or removed since the last time the function was computed and FALSE otherwise
	 * */
	public boolean isTrustedEquation() {
		return trustedEquation;
	}
}
