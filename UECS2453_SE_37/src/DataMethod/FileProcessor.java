package DataMethod;
import java.util.*;
import java.io.*;
import java.time.LocalDate;

import model.Assignment;

public class FileProcessor {
	
	
	public ArrayList<String> readFileToList(String fileName){
		ArrayList<String> list=new ArrayList<>();
		try {
			File myFile=new File(fileName);
			Scanner scan=new Scanner(myFile);
			while(scan.hasNextLine()){
				String line=scan.nextLine();
				list.add(line);
			}
		}
		catch (Exception e){
			System.out.println("Failed to read file with the file path:"+fileName);
		}
		return list;
	}
	public ArrayList<Assignment> getAssignmentList(String fileName){
		ArrayList<String> list=new ArrayList<>();
		ArrayList<Assignment> asgmList=new ArrayList<>();
		list=readFileToList(fileName);
		for(String line:list) {
			String[] elementList=line.split("\\|"); //seperate element with |
			String name=elementList[0];
			LocalDate date=LocalDate.parse(elementList[1]);
			int mark=Integer.parseInt(elementList[2]);
			int duration=Integer.parseInt(elementList[3]);
			Assignment asgm=new Assignment(name,date,mark,duration);
			asgmList.add(asgm);
		}
		return asgmList;
		
	}
	public void writeItem(ArrayList<Assignment> list, String fileName) {
		try {
			PrintWriter writer=new PrintWriter(new FileWriter(fileName,false));
			for(int i=0;i<list.size();i++) {
				Assignment asgm=list.get(i);
				if(i==list.size()-1) {
					writer.print(asgm.toString());
				}
				else {
					writer.println(asgm.toString());
				}
			}
			writer.close();
		}
		catch(IOException e) {
			System.out.println("Failed to Write into file");
		}
		
	}
	public void writeFileNameAndNumber(String Filename, int subjectNumber) {
		try {
			PrintWriter writer=new PrintWriter(new FileWriter("fileName.txt",true));
			File myFile=new File("fileName.txt");
			if (myFile.length() > 0) {
	            writer.println(); //change next row
	        }
			writer.print(Filename + "|" + subjectNumber);
			
			writer.close();
		}
		catch(IOException e) {
			System.out.println("Failed to Write into file");
		}
	}
	public void printFileIndex() {
	    File file = new File("fileName.txt");
	    try (Scanner scan = new Scanner(file)) {
	        while (scan.hasNextLine()) {
	            String line = scan.nextLine();
	            if (line.contains("|")) {
	                String[] parts = line.split("\\|");
	                String name = parts[0];
	                String count = parts[1];
	                System.out.printf("%-25s (%-2s subjects)%n", name, count);
	            }
	        }
	        System.out.println("--------------------------------------------------");
	    } 
	    catch (FileNotFoundException e) {
	        System.out.println("Error in read Filname txt");
	    }
	}
	
	public void printFile(String fileName) {
		ArrayList<Assignment> list =this.getAssignmentList(fileName);
		this.printAssignmentList(list);
	}

	public void printAssignmentList(ArrayList<Assignment> list) {
	    if (list == null || list.isEmpty()) {
	        System.out.println("No assignments available.");
	        return;
	    }
	    System.out.println("======================================================================================");
	    System.out.printf("%-2s | %-30s | %-12s | %-6s | %-10s%n", "ID", "Assignment Name", "Deadline", "Mark", "Duration");
	    System.out.println("--------------------------------------------------------------------------------------");

	    int count = 1;
	    for (Assignment asgm : list) {
	        System.out.printf("%-2d | %-30s | %-12s | %-6d | %d day(s)%n", 
	            count, asgm.getName(), asgm.getDeadLine(), asgm.getMark(), asgm.getDuration());
	        count++;
	    }
	    System.out.println("======================================================================================");
	}
	
	
}
