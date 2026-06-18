package model;
import java.util.ArrayList;

public class ScheduledResult {
	private ArrayList<Assignment> selectedAssignments;
    private ArrayList<Assignment> unselectedAssignments;
    private int totalMarks;
    
    public ScheduledResult(ArrayList<Assignment> selectedAssignments,ArrayList<Assignment> unselectedAssignments,
    int totalMarks) {
    	this.selectedAssignments = selectedAssignments;
    	this.unselectedAssignments = unselectedAssignments;
        this.totalMarks = totalMarks;
    }
    
    public ArrayList<Assignment> getSelectedAssignment(){
    	return selectedAssignments;
    }
    
    public ArrayList<Assignment> getUnselectedAssignment(){
    	return unselectedAssignments;
    }
    
    public int getTotalMarks() {
        return totalMarks;
    }
    
}
