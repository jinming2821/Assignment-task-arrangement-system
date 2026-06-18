package ServiceStrategic;
import model.Assignment;
import model.ScheduledResult;

import java.time.LocalDate;
import java.util.*;
import DataMethod.*;
import App.*;

public class BacktrackingMethod extends AbstractScheduler{
	private FileProcessor processor=new FileProcessor();
	private ArrayList<Assignment> bestSchedule= new ArrayList<>();;
	private Scanner scan=new Scanner(System.in);
	private int maxMark=0;
	private LocalDate begin;
	private partOfApp part=new partOfApp();
	private ArrayList<Assignment> assignmentList;
	private HashMap<Assignment, Integer> bestSlots = new HashMap<>(); 
	private HashMap<Assignment, Integer> currentSlots = new HashMap<>();
	private int freeDays;
	private ScheduledResult result;
	private boolean[] timeSlot;
	
	@Override
	public String getAlgorithmName() {
		return "Backtracking";
	}
	@Override
	public ArrayList<Assignment> solve (ArrayList<Assignment> list){
		this.maxMark = 0;                   //
	    this.bestSchedule = new ArrayList<>(); // 
	    this.bestSlots = new HashMap<>();      // 
	    this.currentSlots = new HashMap<>();   // set initial 
		this.assignmentList=list;
		list=this.sortDeadLine(list);
		this.begin=part.askForBeginingDate();
		if(begin!=null) {
			LocalDate latestDate =findLatestDate(list,begin);
			timeSlot=new boolean[calculateRemainingDay(begin,latestDate)];
			backTracking(list,timeSlot);
		}// if end
		this.setScheduleResult();
		return this.bestSchedule; // check is this empty
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
				freeDays = timeSlot.length;
				for (Assignment asgm :this.bestSchedule) {
					int StartDayIndex=bestSlots.get(asgm);
					LocalDate startDate=begin.plusDays(StartDayIndex)
;					LocalDate endDate=startDate.plusDays(asgm.getDuration()-1);
					System.out.print("Do from "+startDate+" to "+endDate+" : ");
					System.out.printf("%-25s Start in %-2d day(s) | ",asgm.getName(),StartDayIndex);
					System.out.println("DeadLine : "+asgm.getDeadLine()+ "| Duration : "+ asgm.getDuration()+
							" | Mark : "+ asgm.getMark());
					freeDays-=asgm.getDuration();
							
				}
			}
			if (result!=null&&!result.getUnselectedAssignment().isEmpty()) {
				System.out.println("-----------------------------------------------------------------------------------------------------------");
				System.out.println("Unselected Assignments:");
				System.out.println("===========================================================================================================");
				for (Assignment a:result.getUnselectedAssignment()) {
					System.out.printf("%-30s | (Deadline: %s, Duration: %s, Mark: %s)%n", 
						    a.getName(), a.getDeadLine(), a.getDuration(), a.getMark());
				}
			}
			System.out.println("-----------------------------------------------------------------------------------------------------------");
			System.out.println("BackTracking algorithm found with total marks :" +maxMark);
			System.out.println("===========================================================================================================");
						
			
			if (freeDays>0){
				System.out.println("You have "+ freeDays+" free day(s) out of "+ timeSlot.length+" day(s).");
				System.out.println("Consider starting your assignment early to reduce last-minute pressure.");
			}
			else{
				System.out.println("Your time is fully packed.");
				System.out.println("Follow the schedule and start early");
			}
		}
	}
	
	public void setScheduleResult() {
		ArrayList<Assignment> unselectedAssignments=new ArrayList<Assignment>(assignmentList);
		unselectedAssignments.removeAll(this.bestSchedule);
		result = new ScheduledResult(this.bestSchedule, unselectedAssignments, maxMark); 
	}
	
	public int getMaxMark() {
		return maxMark;
	}
	public void backTracking(ArrayList<Assignment> list,boolean[] timeSlot) {
		backTracking(list,0,0,timeSlot,new ArrayList<Assignment>());
	}
	public void backTracking(ArrayList<Assignment> list,int index,int currentMark,boolean[] timeSlot,ArrayList<Assignment> currentPath){
		
		if(index==list.size()) {//when calculate all the possible
			if(currentMark>maxMark) {//
				maxMark=currentMark;//renew the bigger way and new maxMark
				bestSchedule=new ArrayList<>(currentPath);//store the current best way
				bestSlots=new HashMap<>(currentSlots);
			}
			
		}
		else {
			
			Assignment asgm=list.get(index);
			LocalDate deadLine=asgm.getDeadLine();
			int deadLineIndex=this.calculateRemainingDay(this.begin, deadLine);
			int findSlot=findSlot(timeSlot,deadLineIndex,asgm.getDuration());
			if(findSlot!=-1) {//have space
				for(int i=0;i<asgm.getDuration();i++) {
					timeSlot[i+findSlot]=true; //book the slot
				}
				currentPath.add(asgm);
				currentSlots.put(asgm,findSlot);
				backTracking(list,index+1,currentMark + asgm.getMark(),timeSlot,currentPath);// continue try to put following assignment in list
				currentPath.remove(currentPath.size() - 1);// delete the previous booked slot for that index case 
		        currentSlots.remove(asgm);
				for (int i = 0; i < asgm.getDuration(); i++) {
					timeSlot[findSlot + i] = false;
				}
					
				
			}
			backTracking(list,index+1,currentMark,timeSlot,currentPath);//give up/ throw away current assignment continue calculate other assignment 
			
		}
	}
	
	public ArrayList<Assignment> sortDeadLine(ArrayList<Assignment> list){	
		list.sort(Comparator.comparing(Assignment::getDeadLine));
		return list;
	}
	
	public int calculateRemainingDay(LocalDate begin,LocalDate deadLine) { //use after verify the deadLine >begin
		return (int)(deadLine.toEpochDay()-begin.toEpochDay())+1; //calculate diff day +1 to make it can do assignment in the beginning day 14/4begin 14/4 start do not 15/4
	}
	public int findSlot(boolean[] timeSlot, int deadLineIndex,int duration) {
		for(int d=deadLineIndex-duration;d>=0;d--) {//start do from what day, most ealier is index/d 0 mean fistday
			boolean free=true;
			for(int i=0;i<duration;i++) {
				if(timeSlot[d+i]) { //exp : try 7 start do, check 7,8,9 is free or not
					free=false;
					break;
				}
			}
			if(free) {
				return d; ///can do in here
			}
		}
		return -1;//no space 
	}
	
	public LocalDate findLatestDate(ArrayList<Assignment> list,LocalDate begin) {
		LocalDate latestDate=begin;
		for(Assignment asgm:list) {
			if(asgm.getDeadLine().isAfter(latestDate)) {
				latestDate=asgm.getDeadLine();
			}
		}
		return latestDate;
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
