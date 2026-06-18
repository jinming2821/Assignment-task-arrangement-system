package App;
import model.*;
import DataMethod.*;
import java.util.*;
import java.time.*;

public class MainApp {
	public static void main(String[] args) {
		partOfApp part=new partOfApp();
		FileProcessor processor=new FileProcessor();
		Scanner scan=new Scanner(System.in);
		ArrayList<Assignment> currentAssignment=new ArrayList<>();
		boolean run=true;
		while(run) {
			
			System.out.println("---------------Assignment sequencing System-------------");
			System.out.println();
			System.out.println("1. Load Assignment from existing file");
			System.out.println("2. Generate Random Assignment List");
            System.out.println("3. Input Assignment Manually (Custom)");
            System.out.println("4. Exit");
            System.out.print("Please enter your choice: ");
            
            String choice=scan.nextLine();
            choice=choice.trim();
            boolean nextStep=false;
            switch(choice) {
            case "1":{
            	currentAssignment=part.loadAssignment();
            	nextStep=true;
            	part.clearScreen();
            	break;
            	
            }
            case "2":{
            	currentAssignment=part.randomAssign();
            	nextStep=true;
            	break;
            }
            case "3":{
            	currentAssignment=part.CustomAssign();
            	nextStep=true;
            	part.clearScreen();
            	break;
            }
            case "4":{
            	run=false;
            	System.out.println("Exiting........ Goodbye");
            	System.out.println("______________________________________________________");
            	System.out.println("    ______                 _     _                     ");
            	System.out.println("   / ____/____   ____   __| |   | |__   __  __ ___     ");
            	System.out.println("  / / __ / __ \\ / __ \\ / _  |   | '_ \\ / / / // _ \\    ");
            	System.out.println(" / /_/ // /_/ // /_/ // (_| |   | |_) / /_/ //  __/    ");
            	System.out.println(" \\____/ \\____/ \\____/ \\____|   |_.__/ \\__, | \\___|    ");
            	System.out.println("                                      /____/           ");
            	System.out.println("______________________________________________________");
            	break;
            }
            default:{
            	part.clearScreen();
            	System.out.println("Invalid input pls try again");
            	break;
            }
            }
            if(!currentAssignment.isEmpty()&&run&&nextStep) {
            	processor.printAssignmentList(currentAssignment);
            	part.strategicSelection(currentAssignment);
            }
            
		}
		
	}
}
