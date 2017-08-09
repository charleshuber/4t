package fr.metz.surfthevoid.tttt.rest.time;

import java.time.LocalDateTime;

public class TimeDuration {
	
	private int years;
	private int months;
	private int weeks;
	private int days;
	private int hours;
	private int minutes;
	private int seconds;
	
	public int getYears() {
		return years;
	}
	public void setYears(int years) {
		this.years = years;
	}
	public int getMonths() {
		return months;
	}
	public void setMonths(int months) {
		this.months = months;
	}
	public int getWeeks() {
		return weeks;
	}
	public void setWeeks(int weeks) {
		this.weeks = weeks;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public LocalDateTime addTo(LocalDateTime dateTime){
		if(dateTime != null){
			return dateTime
					.plusYears(years)
					.plusMonths(months)
					.plusDays(weeks * 7)
					.plusDays(days)
					.plusHours(hours)
					.plusMinutes(minutes)
					.plusSeconds(seconds);
		}
		return null;
	}
}
