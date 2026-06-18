package model;

import java.time.LocalDate;
public class Assignment {
	private String name;
	private LocalDate deadLine; ///// easy the date calculation
	private int mark;
	private int duration;
	
	public Assignment(String name,LocalDate deadLine, int mark,int duration) {
		this.name=name;
		this.deadLine=deadLine;
		this.mark=mark;
		this.duration=duration;
	}
	
	public String getName() {
		return this.name;
	}
	public LocalDate getDeadLine() {
		return this.deadLine;
	}
	public int getMark() {
		return mark;
	}
	public int getDuration() {
		return duration;
	}
	public int getRelativeDeadline(LocalDate beginDate) {
	    long days = this.deadLine.toEpochDay() - beginDate.toEpochDay();
	    return (int) days + 1;
	}
	@Override
	public String toString() {
		return name+"|"+deadLine+"|"+mark+"|"+duration;
	}
	
}
