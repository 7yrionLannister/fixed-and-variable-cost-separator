package model;

public class Record implements Comparable<Record>{
	private double activityLevel;
	private double totalCost;
	
	public Record(double activityLevel, double totalCost) {
		this.activityLevel = activityLevel;
		this.totalCost = totalCost;
	}
	
	public double getActivityLevel() {
		return activityLevel;
	}
	
	public void setActivityLevel(double activityLevel) {
		this.activityLevel = activityLevel;
	}
	
	public double getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	@Override
	public boolean equals(Object another) {
		Record a = (Record)another; //throws exception if another is not a record
		return a.activityLevel == activityLevel && a.totalCost == totalCost;
	}
	@Override
	public int compareTo(Record r) {
		return Double.compare(activityLevel, r.activityLevel);
	}
}
