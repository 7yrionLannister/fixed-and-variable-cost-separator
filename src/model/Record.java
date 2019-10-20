package model;

public class Record implements Comparable<Record>{
	private int activityLevel;
	private double totalCost;
	
	public Record(int activityLevel, double totalCost) {
		this.activityLevel = activityLevel;
		this.totalCost = totalCost;
	}
	
	public int getActivityLevel() {
		return activityLevel;
	}
	
	public void setActivityLevel(int activityLevel) {
		this.activityLevel = activityLevel;
	}
	
	public double getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	@Override
	public int compareTo(Record r) {
		return activityLevel - r.activityLevel;
	}
}
