package ServiceStrategic;

import java.util.*;
import java.time.LocalDate;
import model.Assignment;
import model.ScheduledResult;
import App.*;
public class EDFMethod extends AbstractScheduler {
	private ArrayList<Assignment> currentAsgm;
    private LocalDate begin;
    private partOfApp part=new partOfApp();
    public EDFMethod(LocalDate begin) {
        this.begin = begin;
    }

    @Override
    public String getAlgorithmName() {
        return "Earliest Deadline First (EDF)";
    }

    @Override
    public ArrayList<Assignment> solve(ArrayList<Assignment> list) {
    	this.currentAsgm=list;
        ArrayList<Assignment> result = new ArrayList<>();
        
        if (begin == null || list.isEmpty()) {
            return result;
        }

        ArrayList<Assignment> tempList = new ArrayList<>(list);
        
        tempList.sort(Comparator.comparing(Assignment::getDeadLine));
        
        LocalDate currentDate = begin;

        for (int i = 0; i < tempList.size(); i++) {
            Assignment asgm = tempList.get(i);
            LocalDate finishDate = currentDate.plusDays(asgm.getDuration()-1); // to make the assignment can do in a same day with deadline 

            if (!finishDate.isAfter(asgm.getDeadLine())) {
                result.add(asgm);
                currentDate = finishDate.plusDays(1);////// next task starting date ,need to be next day 
            }
        }

        return result;
    }

    
    
   
    @Override
    public void printResult(ArrayList<Assignment> result) {
    	part.clearScreen();
    	ArrayList<Assignment> unselectedAssignments=new ArrayList<Assignment>(this.currentAsgm);
		unselectedAssignments.removeAll(result);
        System.out.println("Scheduling start date : " + begin);
        System.out.println("===========================================================================================================");

        if (result.isEmpty()) {
            System.out.println("No assignments can be completed.");
        } else {
            LocalDate currentDate = begin;
            for (int i = 0; i < result.size(); i++) {
                Assignment asgm = result.get(i);
                LocalDate finishDate = currentDate.plusDays(asgm.getDuration()-1);//// same with solve method
                System.out.printf("%-1d. %-30s | Deadline: %-10s | Finish by: %-10s | Marks: %-3d%n", 
                	    (i + 1), 
                	    asgm.getName(), 
                	    asgm.getDeadLine(), 
                	    finishDate, 
                	    asgm.getMark());
                currentDate = finishDate.plusDays(1);///same with solve method 
            }
        }
        if(!unselectedAssignments.isEmpty()) {
        	System.out.println("-----------------------------------------------------------------------------------------------------------");
        	System.out.println("Unselected Assignments:");
			System.out.println("===========================================================================================================");
			for (Assignment a: unselectedAssignments) {
				System.out.printf("%-30s | (Deadline: %s, Duration: %s, Mark: %s)%n", 
					    a.getName(), a.getDeadLine(), a.getDuration(), a.getMark());
			}
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.println(getAlgorithmName()+" algorithm found with Total marks: " + calculateTotal(result));
        System.out.println("======================================================================");
    }

    private int calculateTotal(ArrayList<Assignment> list) {
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i).getMark();
        }
        return total;
    }
}