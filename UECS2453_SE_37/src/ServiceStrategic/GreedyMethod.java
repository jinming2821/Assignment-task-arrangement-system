package ServiceStrategic;
import App.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import model.Assignment;
import model.ScheduledResult;

public class GreedyMethod extends AbstractScheduler{
    	private int totalMarks =0;
    	private LocalDate begin;
    	private partOfApp part = new partOfApp();
    	private ScheduledResult result;	
    	private ArrayList<String>arrangedSchedule = new ArrayList<>();
		private int freeDays = 0;
		private int totalDays = 0 ;
    	
    	@Override
    	public String getAlgorithmName() {
    		return "Greedy";
    	}
        		
    	@Override	
    	public ArrayList<Assignment> solve(ArrayList<Assignment> list) {
    		
    	ArrayList<Assignment> sortedList = new ArrayList<>(list);
    	ArrayList<Assignment> selectedAssignments = new ArrayList<>();
    	ArrayList<Assignment> unselectedAssignments = new ArrayList<>();
    	
    	totalMarks = 0;
    	arrangedSchedule.clear();
    	this.begin = part.askForBeginingDate();
    	
    	if(begin!=null) {
    		
    	sortedList.sort(new Comparator<Assignment>() {
    	    @Override
    	    public int compare(Assignment a, Assignment b) {
    	        return Integer.compare(b.getMark(),a.getMark());// higher mark first
    	    }
    	});	
    	int maxDeadline = 0;
    	for(Assignment a: sortedList) {
    		int d = a.getRelativeDeadline(begin);
    		if (d>maxDeadline) {
    			maxDeadline = d;
    		}
    	}
    	boolean[] timeSlot = new boolean[maxDeadline];
		totalDays = maxDeadline;
    	
    	for (Assignment a : sortedList) {
    		int deadLineIndex = a.getRelativeDeadline(begin)-1;
    		int duration = a.getDuration();
    		if (deadLineIndex <0) {
    			unselectedAssignments.add(a);
    			continue;
    		}
    		int startSlot= findSlot(timeSlot,deadLineIndex,duration);
    		if (startSlot!=-1) {
    			for(int i=0; i<duration;i++) {
    				timeSlot[startSlot + i] = true;
    			}
    			selectedAssignments.add(a);
    			totalMarks+= a.getMark();
    			LocalDate startDate = begin.plusDays(startSlot);
    			LocalDate endDate = begin.plusDays(startSlot+duration-1);
				int daysBeforeStart = startSlot;
				String formattedLine = String.format(
					    "Do from %s to %s : %-25s Start in %-2d day(s) | Deadline: %-10s | Duration: %-2d | Mark: %-3d\n",
					    startDate,endDate,a.getName(), daysBeforeStart, a.getDeadLine(),duration, a.getMark());
    			arrangedSchedule.add(formattedLine);
    		}else {
    				unselectedAssignments.add(a);
    				}
    		}
			freeDays = 0;
			for (boolean slot : timeSlot){
				if(!slot) freeDays++;

			}}
			
    		result = new ScheduledResult(selectedAssignments, unselectedAssignments, totalMarks); 
    		return selectedAssignments;
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
   
    	
    	@Override
    	public void printResult	(ArrayList<Assignment>list) {
    		part.clearScreen();
    		if (checkBegin()) {
    			System.out.println("Scheduling start date:" +begin);
    			System.out.println("===========================================================================================================");
    			if(list.isEmpty()) {
    				System.out.println("There are not enough time slot to do any assignment");
    			}else {
    				System.out.print("Scheduled Assignments:\n");
    				System.out.println("===========================================================================================================");
    				for (String line :arrangedSchedule) {
    					System.out.print(line);
    					
    				}
    			}
    			if (result!=null&&!result.getUnselectedAssignment().isEmpty()) {
    				System.out.println("Unselected Assignments:");
    				System.out.println("===========================================================================================================");
    				for (Assignment a:result.getUnselectedAssignment()) {
    					System.out.printf("%-30s | (Deadline: %s, Duration: %s, Mark: %s)%n", 
    						    a.getName(), a.getDeadLine(), a.getDuration(), a.getMark());
    				}
    			}
    			
    			System.out.println("-----------------------------------------------------------------------------------------------------------");
    			System.out.println("Greedy Schedule found with total marks :" +totalMarks);
				System.out.println("===========================================================================================================");
				if (freeDays>0){
					System.out.println("You have "+ freeDays+" free day(s) out of "+ totalDays+" day(s).");
					System.out.println("Consider starting your assignment early to reduce last-minute pressure.");
				}
				else{
					System.out.println("Your time is fully packed.");
					System.out.println("Follow the schedule and start early");
				}
    		}
    	}
    	public boolean checkBegin(){
    		return begin!= null;
    		}
    	

    	public int getTotalMarks() {
    		return totalMarks;
    	}
    	public ScheduledResult getResult() {
    		return result;
}}
