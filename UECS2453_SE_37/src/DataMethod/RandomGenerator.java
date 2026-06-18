package DataMethod;
import DataMethod.FileProcessor;
import App.*;
import java.util.*;
import java.time.LocalDate;
import java.util.random.*;
import model.Assignment;
import java.io.*;
public class RandomGenerator {
	private Random rand=new Random();
	private partOfApp part=new partOfApp();
	private FileProcessor processor=new FileProcessor();
	public LocalDate randomDate(int timeRange,LocalDate begin) {
		
		int randomNumber=rand.nextInt(1,timeRange+1);
		return begin.plusDays(randomNumber);
		
	}
	public int randomNumber(int min,int max) {
		int randomNumber=rand.nextInt(min,max+1);
		return randomNumber;
	}
	public int randomMark() {
		return randomNumber(1,100);
	}
	public int randomDuration() {
		return randomNumber(1,5);
	}
	public ArrayList<Assignment> autoGenerate(LocalDate begin) {
        ArrayList<Assignment> list = new ArrayList<>();
        List<String> subjects;
        subjects = processor.readFileToList("subjects.txt");
        if (subjects == null || subjects.isEmpty()) {
        	
        	subjects = Arrays.asList(
        	        "Math Quiz", 
        	        "Java Programming", 
        	        "Data Structures", 
        	        "Database System", 
        	        "Web Development", 
        	        "Statistics", 
        	        "Operating Systems", 
        	        "Software Engineering",
        	        "Network Security",
        	        "Artificial Intelligence"
        	    );
        	System.out.println("subjects.txt not found or empty. Using default subject list.");
        }
       
        	
        
        
        int numberOfAssignments = this.randomNumber(2, 10);
        Collections.shuffle(subjects);

        for (int i = 0; i < numberOfAssignments; i++) {
            String asgmName = subjects.get(i);
            int duration = this.randomDuration();
            int mark = this.randomMark();
            LocalDate deadline = this.randomDate(20, begin);
            
            list.add(new Assignment(asgmName,deadline,mark,duration));
        }
        return list;
    }
}
