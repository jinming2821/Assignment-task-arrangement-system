package App;
import java.util.*;
import model.Assignment;
import DataMethod.*;
import java.io.*;
import java.time.*;
import ServiceStrategic.*;
public class partOfApp {
	private FileProcessor processor=new FileProcessor();
	private Scanner scan=new Scanner(System.in);
	
	public void clearScreen() {
	    for (int i = 0; i < 50; i++) {
	        System.out.println();
	    }
	}
	
	public ArrayList<Assignment> loadAssignment() {
		this.clearScreen();
		ArrayList<Assignment> list=new ArrayList<>();
		boolean run=true;
		while(run) {
			System.out.println("----------------load file page------------------- ");
			processor.printFileIndex();
			System.out.println("Please enter the file name that you what to load");
			System.out.println("Q/q to exit");
			String name=scan.nextLine();
			String fileName=name+".txt";
			if(name.equals("Q")||(name.equals("q"))) {
				run=false;
				this.clearScreen();
			}
			else {
				list=processor.getAssignmentList(fileName);
				if(!(list.isEmpty())){
					run=false;
				}
			}
			
		}
		return list;
	}
	
	public ArrayList<Assignment> randomAssign() {
		this.clearScreen();
		ArrayList<Assignment> list=new ArrayList<>();
		boolean run=true;
		while(run) {
			ArrayList<String> nameList=new ArrayList<>();
			System.out.println("---------------random Generat page--------------- ");
			System.out.println("Enter the new file name to store it Q to exits");
			String name=scan.nextLine();
			String fileName=name+".txt";
			File file=new File(fileName);
			this.clearScreen();
			if(name.toUpperCase().equals("Q")) {
				run=false;
				this.clearScreen();
			}
			else if(name.equals("")) {
				System.out.println("Invalid input, file name cannot be empty");
			}
			else if(!(file.exists())) {
				RandomGenerator random=new RandomGenerator();
				list=random.autoGenerate(LocalDate.now());
				processor.writeItem(list, fileName);
				processor.writeFileNameAndNumber(name, list.size());
				run=false;
			}
			else {
				System.out.println("File already exits, please use other name");
			}
			
		}
		return list;
	}
	public ArrayList<Assignment> CustomAssign(){
		this.clearScreen();
		ArrayList<Assignment> list=new ArrayList<>();
		System.out.println("\n+------------------------------------------------+");
		System.out.println("    Custom Assignment input      ");
		System.out.println("+------------------------------------------------+");
		
		boolean enterMark=false;
		boolean validFile=false;
		while(!(validFile)) {
			System.out.println("Please enter the filename (.txt): ");
			String fileName=scan.nextLine();//////////////////////////
			String fileTxt=fileName+".txt";
			File file=new File(fileTxt);
			if(fileName.equalsIgnoreCase("Q")) {
				validFile=true;
				this.clearScreen();
			}
			else if(file.exists()) {
				System.out.println("File already exits, please use other name");
			}
			else if(fileName.equals("")) {
				System.out.println("Invalid input, file name cannot be empty");
			}
			else {
				validFile=true;
				boolean run=true;
				while(run) {
					LocalDate date=null;
					int mark=0;
					System.out.println("\n+------------------------------------------------+");
					System.out.println("      New Assignment Name    ");
					System.out.println("+------------------------------------------------+");
					System.out.print("  > Enter Assignment Name : ");
					String name=scan.nextLine();
					this.clearScreen();///
					if(name.equalsIgnoreCase("Q")) {
						run=false;
					}
					else if(name.equals("")) {
						System.out.println("Invalid input"); 
					}
					else {
						
						boolean enterDate=true;
						while(enterDate) {
							System.out.println("+------------------------------------------------+");
							System.out.println("      DEADLINE SETTING      ");
							System.out.println("+------------------------------------------------+");
							System.out.printf("%-15s : ", "  [1] Enter Month (1-12)");
							String month=scan.nextLine();
							if(month.equalsIgnoreCase("Q")) {
								enterDate=false;
							}
							else {
								System.out.printf("%-15s : ", "  [2] Enter Day   ");
								String day=scan.nextLine();
								try {
									date=LocalDate.of(2026,Integer.parseInt(month),Integer.parseInt(day));
									enterDate=false;
									enterMark=true;
									this.clearScreen();
								}
								
								catch(Exception e) {
									System.out.println("Invalid input of date format");
								}
							}
							
						}
						while(enterMark) {
							System.out.println("\n+-----------------------------------+");
							System.out.println("|      Assignment mark (1-100)      |");
							System.out.println("+-----------------------------------+");
							String strMark=scan.nextLine();
							this.clearScreen();
							try {
								mark=Integer.parseInt(strMark);
								if(!(mark>0&&mark<=100)) {
									System.out.println("Invalid mark format");
								}
								else {
									enterMark=false;
								}
							}
							catch(Exception e) {
								System.out.println("Invalid mark format");
							}
						}
						if(date==null||mark==0) {
							System.out.println("Failed to add new Assignment please try again");
						}
						else {
							boolean askDu=true;
							while(askDu) {
								System.out.println("\n--------------------------------------------------");
								System.out.println("   [ RESOURCE ALLOCATION ]");
								System.out.print("   ➜ Workload Duration (Total Days) : ");
								String duration=scan.nextLine();
								this.clearScreen();
								try {
									Assignment asgm=new Assignment(name,date,mark,Integer.parseInt(duration));
									list.add(asgm);
									System.out.println("current Assignment list");
									processor.printAssignmentList(list);
									System.out.println("Do you like to continue? Y/y to continue");
									String choice=scan.nextLine();
									askDu=false;
									if(!(choice.toUpperCase().equals("Y"))) { // no continue
										run=false;
										
									}
								}
								catch (Exception e) {
									System.out.println("Invalid duration input pls enter a number");
								}
								
								}
							}
							
						}
					}//while run
				if (!list.isEmpty()) { 
				    processor.writeItem(list, fileTxt); 
				    processor.writeFileNameAndNumber(fileName, list.size());
				}
				
			}
		}
					
		return list;
		
	}
	public LocalDate askForBeginingDate() {////after call this need to verify is this return null mean user quit 
		boolean ask=true;
		LocalDate begin=null;
		while(ask) {
			this.clearScreen();
			System.out.println("+------------------------------------------------+");
			System.out.println("      STARTING DATE SETTING      ");
			System.out.println("+------------------------------------------------+");
			System.out.println("  Please specify the start date for your plan:");
			System.out.printf("%-15s : ", "  [1] Enter Month (1-12)");
			String month=scan.nextLine();
			if(month.equalsIgnoreCase("Q")) {
				ask=false;
			}
			else {
				System.out.printf("%-15s : ", "  [2] Enter Day   ");
				String day=scan.nextLine();
				if(day.equalsIgnoreCase("Q")) {
					ask=false;
				}
				else {
					try {
						begin=LocalDate.of(2026, Integer.parseInt(month),Integer.parseInt(day));
						ask=false;
					}
					catch(Exception e){
						System.out.println("Invalid date");
					}
				}
			}
		}
		return begin;
		
		
	}
	public void strategicSelection(ArrayList<Assignment> list) {
		boolean run=true;
		while(run) {
			
			System.out.println("---------------Strategic Selection Page-------------------");
			System.out.println("1. Earliest Deadline First (EDF)");
			System.out.println("2. Backtracking Algorithm");
			System.out.println("3. Greedy + Disjoint Set / Union-Find");
			System.out.println("4. Brute-Force/ Exhaustive Search");
			System.out.println("Q to exits");
			String choice=scan.nextLine();
			switch(choice) {//inside the method ask beginning date first and then calculate with the different deadline
			case "1":{
				this.clearScreen();
				LocalDate begin = this.askForBeginingDate();
                if (begin != null) {          // null means user pressed Q
                    EDFMethod edf = new EDFMethod(begin);
                    edf.printResult(edf.solve(list));
                }
                System.out.println("Continue with other algorithm? Y/y to continue");
                if (!scan.nextLine().equalsIgnoreCase("Y")) {
                    run = false;
                }
                this.clearScreen();
                break;
			}
			case "2":{
				
				BacktrackingMethod backTracking=new BacktrackingMethod();
				backTracking.printResult(backTracking.solve(list));
				if(backTracking.checkBegin()) {
					System.out.println("Continue with other algorithm? Y/y to continue");
					if(!scan.nextLine().equalsIgnoreCase("Y")) {
						run=false;
					}
				}
				else {
					run=false;
				}
				this.clearScreen();
				break;
			}
			case "3":{
				GreedyMethod greedy = new GreedyMethod();
				ArrayList<Assignment> result = greedy.solve(list);
				greedy.printResult(result);
				System.out.println("Continue with other algorithm? Y/y to continue");
				if(!scan.nextLine().equalsIgnoreCase("Y")) {
					run=false;
				}
				this.clearScreen();
				break;
			}
			case "4":{
				BruteForceMethod bruteForce = new BruteForceMethod();
				ArrayList<Assignment> result = bruteForce.solve(list);
				bruteForce.printResult(result);
				System.out.println("Continue with other algorithm? Y/y to continue");
				if(!scan.nextLine().equalsIgnoreCase("Y")) {
					run=false;
				}
				this.clearScreen();
				break;
			}
			case "Q":
			case "q":{
				run=false;
				this.clearScreen();
				break;
			}
			default:{
				this.clearScreen();
				System.out.println("Invalid input");
				break;
			}
			}
		}
		
	}

}
