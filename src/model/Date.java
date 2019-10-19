package model;

public class Date implements Comparable<Date> {

	private int day;
	private int month;
	private int year;

	/**The method allows to create an instance of Date with the specified day, month, year and hour
	 * @param day The day must be between 1 and 31
	 * @param month The month must be between 1 and 12
	 * @param year The year must be any value supported by <b>int</b> as the time is undetermined
	 * */
	public Date(int day, int month, int year) throws IllegalArgumentException {
		setDay(day);
		setMonth(month);
		setYear(year);
	}

	/**The method allows to obtain the day
	 * @return The day for this date
	 * */
	public int getDay() {
		return this.day;
	}

	/**The method allows to change the current day
	 * @param day The day must be between 1 and 31
	 */
	public void setDay(int day) throws IllegalArgumentException {
		if(day < 1 || day > 31) {
			throw new IllegalArgumentException("Invalid day: " + day);
		}
		this.day = day;
	}

	/**The method allows to obtain the month
	 * @return The month for this date
	 * */
	public int getMonth() {
		return this.month;
	}

	/**The method allows to change the current month
	 * @param month The month must be between 1 and 12
	 */
	public void setMonth(int month) throws IllegalArgumentException {
		if(month < 1 || month > 12) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		this.month = month;
	}

	/**The method allows to obtain the year
	 * @return The year for this date
	 * */
	public int getYear() {
		return this.year;
	}

	/**The method allows to change the current year
	 * @param year The year must be any value supported by <b>int</b> as the time is undetermined
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**The method allows knowing when this date is equal, greater or less than another date without taking into account the time<br>
	 * It defines the natural order for Date
	 * @param date The date to compare this date
	 * @return -1 if this date is less than the other<br>
	 * 1 if this date is greater than the other<br>
	 * 0 if the two dates are equal 
	 * */
	@Override
	public int compareTo(Date date) {
		int comparation = 0;
		if(year > date.year) {
			comparation = 1;
		}
		else if(year < date.year) {
			comparation = -1;
		}
		else {
			if(month > date.month) {
				comparation = 1;
			}
			else if(month < date.month) {
				comparation = -1;
			}
			else {
				if(day > date.day) {
					comparation = 1;
				}
				else if(day < date.day ) {
					comparation = -1;
				}
			}
		}
		return comparation;
	}

	/**The method allows to obtain a String representation of this date
	 * @return A String representing this date object
	 * */
	@Override
	public String toString() {
		String date = ""+day;
		date += "/"+month;
		date += "/"+year;
		return date;
	}
}