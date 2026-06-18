package ServiceStrategic;
import java.util.*;
import java.time.*;
import App.*;
import model.*;

public class BruteForceMethod extends AbstractScheduler{
	private int maxTotalMark = 0;
	private ArrayList<Assignment> finalAssignmentSchedule = new ArrayList<Assignment>();
	private LocalDate begin;
	private int freeDays;
	private int totalDays;
	private ScheduledResult result;
	private partOfApp part = new partOfApp();
	private boolean timeSlot[];
	
	@Override
	public String getAlgorithmName() {
		return "Brute Force Method";
	}
	
	@Override
	public ArrayList<Assignment> solve (ArrayList<Assignment> list){
		this.maxTotalMark = 0;
		this.finalAssignmentSchedule = new ArrayList<>();
		this.begin = part.askForBeginingDate();  //Ask for start scheduling date /////////////////////////////////
		if(checkBegin()) {
			int maxDeadline = 0;
	    	for(Assignment task: list) {
	    		int deadline = task.getRelativeDeadline(begin);
	    		if (deadline>maxDeadline) {
	    			maxDeadline = deadline;
	    		}
	    	}
	    	totalDays = maxDeadline;
			this.timeSlot = new boolean[maxDeadline + 1];
			Stack<Assignment> taskStack = new Stack<Assignment>();
			for(Assignment task : list) {
				taskStack.push(task);
			}
			BruteForce(taskStack,timeSlot);
			ArrayList<Assignment> selectedAssignment = new ArrayList<Assignment>();
			ArrayList<Assignment> unselectedAssignment = new ArrayList<Assignment>(list);
			selectedAssignment.addAll(finalAssignmentSchedule);
			unselectedAssignment.removeAll(selectedAssignment);
			result = new ScheduledResult(selectedAssignment, unselectedAssignment, maxTotalMark);
			for(boolean free: timeSlot) {
				if(!free) {
					freeDays++;
				}
			}
		}
		
		return finalAssignmentSchedule;
	}
	
	@Override
	public void printResult(ArrayList<Assignment> list) {
		part.clearScreen();
		System.out.println("Scheduling start date:" + begin);
		if(list.isEmpty()) {
			System.out.println("There are not any assignment can do with the start date");
		}
		else {
			finalAssignmentSchedule.sort(Comparator.comparing(Assignment::getDeadLine));
			System.out.println("=".repeat(100));
			System.out.println("\nScheduled Assignment");
			System.out.println("=".repeat(100));
			boolean[] printSlots = new boolean[totalDays + 1];
	        int calculatedFreeDays = totalDays + 1;

	        for (Assignment task : result.getSelectedAssignment()) {
	            int deadlineIndex = task.getRelativeDeadline(begin) - 1;
	            int startDayIndex = findSlot(printSlots, deadlineIndex, task.getDuration());
	            for (int i = 0; i < task.getDuration(); i++) {
	                printSlots[startDayIndex + i] = true;
	            }
	            LocalDate startDate = begin.plusDays(startDayIndex);
	            LocalDate endDate = startDate.plusDays(task.getDuration() - 1);
				System.out.print("Do from "+startDate+" to "+endDate+" : ");
				System.out.printf("%-25s Start in %-2d day(s) | ",task.getName(),startDayIndex);
				System.out.println("DaedLine : "+task.getDeadLine()+ "| Duration : "+ task.getDuration()+ " | Mark : "+ task.getMark());
				freeDays-=task.getDuration();
			}
		}
		System.out.println("Unselected Assignment:");
		System.out.println("=".repeat(100));
		for (Assignment task: result.getUnselectedAssignment()) {
			System.out.printf("%-30s | (Deadline: %s, Duration: %s, Mark: %s)%n", 
					   task.getName(), 
					   task.getDeadLine(), 
					   task.getDuration(), 
					   task.getMark());
		}
			
		System.out.println("-".repeat(100));
		System.out.println("Brute Force algorithm found with total marks :" +maxTotalMark);
		System.out.println("=".repeat(100));
						
			
		if (freeDays>0){
			System.out.println("You have "+ freeDays+" free day(s) out of "+ timeSlot.length+" day(s).");
			System.out.println("Consider starting your assignment early to reduce last-minute pressure.");
		}
		else{
			System.out.println("Your time is fully packed.");
			System.out.println("Follow the schedule and start early");
		}
	}
	
	public void BruteForce(Stack<Assignment> remainingTask, boolean[] timeSlot){
		ArrayList<Assignment> currentSchedule = new ArrayList<Assignment>();
		BruteForce(remainingTask, timeSlot, currentSchedule);
	}
	
	public void BruteForce(Stack<Assignment> remainingTask, boolean [] timeSlot, ArrayList<Assignment> currentSchedule) {
		if(remainingTask.isEmpty()) {         //Base Case, if Stack is empty
			if(validSchedule(currentSchedule)) {   //Check the combination of assignment is work or not
				int currentMark = calculateTotalMark(currentSchedule);  //Calculate the total mark if the combination of assignment is work
				if(currentMark > maxTotalMark) {  //Check the schedule assignment mark which is higher
					maxTotalMark = currentMark;
					this.finalAssignmentSchedule = new ArrayList<>(currentSchedule);
				}
			}
			return;
		}
		Assignment currentTask = remainingTask.pop();    //Get first assignment/lab. Exp: [A, B, C] get C
		currentSchedule.add(currentTask);                
			
		//Get next assignment/lab. Exp: [A, B] get B
		BruteForce(remainingTask, timeSlot, currentSchedule); 
			
		//Remove the current assignment/lab and check other combination. Exp: current Assignment schedule [C, B, A] remove A
		currentSchedule.remove(currentSchedule.size()-1);

		//Check for another combination after remove the current one;
		BruteForce(remainingTask, timeSlot, currentSchedule);
		
		//Push the current task back to the stack for maintain the integrity of the recursive call;
		remainingTask.push(currentTask);
		return;
	}
	
	public boolean validSchedule(ArrayList<Assignment> schedule) { //Check the combination is can fit to the time slot or not
		boolean[] tempSlots = new boolean[totalDays +1];
		
		for(Assignment task : schedule) {
			int start = findSlot(tempSlots, task.getRelativeDeadline(begin)-1, task.getDuration());
			if(start == -1) {
				return false;
			}
			for(int i = 0;i < task.getDuration(); i++) {
				tempSlots[start + i] = true;
			}
		}
		
		return true;
	}
	
	public int calculateTotalMark(ArrayList<Assignment> list) { //Calculate the total assignment mark can get with the assignment schedule
		int mark = 0;
		for(Assignment task : list) {
			mark += task.getMark();
		}
		return mark;
	}
	
	public int findSlot (boolean[]timeSlot ,int deadLineIndex, int duration) {		
		int latestStart = deadLineIndex-duration+1;
		for (int start = latestStart; start>=0; start --) {
			boolean free = true;
			
			for (int i=0; i<duration; i++) {
				if (timeSlot[start + i]) {
				free=false;
				break;
			}
		}	
			if (free) {
				return start;
			}
		}
		return -1;
	}
	
	public boolean checkBegin() {
		if(begin!=null) {
			return true;
		}
		else {
			return false;
		}
	}
	

}
