import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class register {
	public static void main(String[] args) throws IOException {
		// Variable declaration
		Scanner userIn = new Scanner(System.in);
		LocalDateTime time = java.time.LocalDateTime.now();
		String date = time.toString();
		date = date.substring(0, 10);// Exclude hours, minutes and seconds
		int choice = 0;
		String filePath = "C:\\Users\\Administrator\\Documents\\Official Documents\\Income and Expenses.csv";
		File file = new File(filePath);
		
		//Checks that the file exists and has been established.
		VerifyFile(file, filePath, date, userIn);

		// STANDARD PROGRAM LOOP
		Boolean programRunning = true;
		do {
			PrintWhiteSpace();
			System.out.println("Choose an option from the following list:");
			System.out.println("1 - Add an entry");
			System.out.println("2 - Lookup Data");
			System.out.println("3 - List Transactions");
			System.out.println("4 - ");
			System.out.println("5 - ");
			System.out.println("6 - ");
			System.out.println("7 - ");
			System.out.println("8 - Create backup");
			System.out.println("9 - End program");
			System.out.print("Your selection: ");
			choice = userIn.nextInt();
			switch (choice) {
			case 1:
				AddEntry(file, date, userIn);
				break;
			case 2:
				LookupData(file, userIn);
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8: 
				CreateBackup(file, userIn);
				break;
			case 9:
				programRunning = false;
				break;
			}// end switch
		} // end loop
		while (programRunning);
		// End program
		userIn.close();
		PrintWhiteSpace();
		System.out.println("Program terminated.");
	}// end main

	//CREATES A BACKUP OF THE FILE.
	private static void CreateBackup(File file, Scanner userIn) throws IOException {
		//Variable declaration
		Scanner in = new Scanner(file);
		String filePath = "C:\\Users\\Administrator\\Documents\\Official Documents\\Income and Expenses Backup.csv";
		File backupFile;
		FileWriter out;
		
		// Ask the user if they would like to specify a new file path.
		PrintWhiteSpace();
		System.out.print("Creating a new file. Would you like to change the file path? [Yes/No]: ");
		String answer = userIn.next();
		userIn.nextLine();
		if (answer.equals("Yes")) {
			System.out.print("Specify a new file path here (ex. C:\\Users\\Administrator\\Documents\\): ");
			filePath = userIn.nextLine();
			filePath = filePath + "Income and Expenses Backup.csv";
			System.out.println("Remember to change the default filepath in your program before you load it again!");
		}
		
		//Create a new file at a designated path
		PrintWhiteSpace();
		backupFile = new File(filePath);
		backupFile.createNewFile();
		System.out.println("Created a new file at " + filePath);
		
		//Copy one file onto the other
		out = new FileWriter(backupFile);
		while (in.hasNextLine()) {
			String temp = in.nextLine();
			out.write(temp);
			out.write("\n");
		}
		out.close();
		in.close();
		System.out.println("Backup successfully created.");
	}//end CreateBackup
	
	//PRINTS WHITESPACE.
	public static void PrintWhiteSpace() {
		for(int i = 0; i < 3; i++) {
			System.out.println("");
		}
	}//end PrintWhiteSpace
	
	// ATTEMPTS TO LOCATE THE FILE AND VERIFIES THAT IT IS NOT EMPTY.
	public static void VerifyFile(File file, String filePath, String date, Scanner userIn) throws IOException {
		// Variable declaration
		Double initialBalance = 0.0;

		try (Scanner test = new Scanner(file)) {
		}
		// If unable to find the file, create a new one.
		catch (FileNotFoundException e) {
			System.out.println("No such file found at " + filePath);
			System.out.print("Creating a new file. Would you like to change the file path? [Yes/No]: ");
			String answer = userIn.next();
			userIn.nextLine();
			// Ask the user if they would like to specify a new filePath.
			if (answer.equals("Yes")) {
				System.out.print("Specify a new file path here (ex. C:\\Users\\Administrator\\Documents\\): ");
				filePath = userIn.nextLine();
				filePath = filePath + "Income and Expenses.csv";
				System.out.println("Remember to change the default filepath in your program before you load it again!");
			}
			PrintWhiteSpace();
			file = new File(filePath);
			file.createNewFile();
			System.out.println("Created a new file at " + filePath);
			try (PrintWriter writer = new PrintWriter(file)) {
				writer.print("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");
				System.out.print("Would you like to enter an initial balance? Otherwise, your balance will be 0.00$. [Yes/No]: ");
				answer = userIn.next();
				userIn.nextLine();
				if (answer.equals("Yes")) {
					PrintWhiteSpace();
					System.out.print("Enter the initial balance of the account (ex. 128.65): ");
					initialBalance = userIn.nextDouble();
					userIn.nextLine();
					writer.println();
					writer.println(date + ",Initial balance,Stating balance transfer,0.00," + initialBalance + ","
							+ initialBalance);
				} else {
					writer.println(date + ",Initial balance,No stating balance transfer,0.00,0.00,0.00");
				}
			} catch (IOException f) {
			}
		}

		// Check if the file is empty due to an error.
		Scanner test = new Scanner(file);
		if (test.hasNextLine() == false) {
			System.out.println("The document was found but is empty. Filling it now.");
			String answer = "";
			try (PrintWriter writer = new PrintWriter(file)) {
				writer.print("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");
				System.out.print("Would you like to enter an initial balance? Otherwise, your balance will be 0.00$. [Yes/No]: ");
				answer = userIn.next();
				userIn.nextLine();
				if (answer.equals("Yes")) {
					System.out.print("Enter the initial balance of the account (ex. 128.65): ");
					initialBalance = userIn.nextDouble();
					userIn.nextLine();
					writer.println();
					writer.println(date + ",Initial balance,Stating balance transfer,0.00," + initialBalance + "," + initialBalance);
				} else {
					writer.println(date + ",Initial balance,No stating balance transfer,0.00,0.00,0.00");
				}
			} catch (IOException f) {
			}
		}
		test.close();
	}// end VerifyFile

	// ADDS ONE OR MORE ENTRIES TO THE FILE.
	public static void AddEntry(File file, String date, Scanner userIn) throws FileNotFoundException {
		// Variable declaration
		Double curBalance = 0.0;
		Double spent = 0.0;
		Double earned = 0.0;
		Boolean anotherEntry = true;
		String description = "";
		String specification = "";

		do {
			// Find the current balance of the account.
			Scanner in = new Scanner(file);
			Scanner lastLine;
			String temp = "";
			while (in.hasNextLine()) {
				temp = in.nextLine();
			}
			lastLine = new Scanner(temp);
			lastLine.useDelimiter(",");
			for (int i = 0; i < 5; i++) {
				lastLine.next();
			}
			curBalance = lastLine.nextDouble();
			lastLine.close();
			in.close();
			PrintWhiteSpace();
			System.out.println("You are now adding an entry to the register. The current balance of this account is " + curBalance + "$.");

			// Gather user data.
			System.out.println("Please enter a one-word description of the change. Common descriptions are: ");
			System.out.println("fast food");
			System.out.println("salary");
			System.out.println("tips");
			System.out.println("online");
			System.out.println("gas");
			System.out.println("donation");
			System.out.println("groceries");
			System.out.print("Your selection: ");
			description = userIn.next();
			userIn.nextLine();
			System.out.print("Please enter a one-word specification if applicable (ex. Burger King, Doordash,  n/a, etc.): ");
			specification = userIn.next();
			userIn.nextLine();
			System.out.print("Enter how much was SPENT, 0 if none: ");
			spent = userIn.nextDouble();
			System.out.print("Enter how much was EARNED, 0 if none: ");
			earned = userIn.nextDouble();

			// Write data to the file.
			try {
				FileWriter writer = new FileWriter(file, true);
				if (spent > 0.0) {
					curBalance -= spent;
				}
				if (earned > 0.0) {
					curBalance += earned;
				}
				writer.write(date + "," + description + "," + specification + "," + spent + "," + earned + "," + curBalance);
				writer.write("\n");
				writer.close();
			} catch (FileNotFoundException d) {
				System.out.println("Unable to write to file - make sure it isn't open!");
				d.printStackTrace();
				System.exit(0);
			} catch (IOException i) {
				System.out.println("Unable to write to file.");
				i.printStackTrace();
				System.exit(0);
			}

			System.out.print("Entry saved. The new balance of the account is " + curBalance
					+ "$. Would you like to add another entry? [Yes/No]: ");
			String answer = userIn.next();
			if (answer.equals("No")) {
				anotherEntry = false;
			}
		} while (anotherEntry);// end outer loop
	}// end AddEntry

	// SEARCHES THE FILE AND PRINTS INFORMATION REQUESTED BY THE USER.
	public static void LookupData(File file, Scanner userIn) throws FileNotFoundException {
		//Variable declaration
		String userInput = "";
		String userYear = "";
		String userMonth = "";
		String userDescChoice = "";
		String userSpecChoice = "";
		Double totalSpent = 0.0;
		Double totalEarned = 0.0;
		int i = 0;
		Scanner in = new Scanner(file);
		ArrayList<String> arr = new ArrayList<>();
		
		//Ask user for a date range, then load array with data from that date range, excluding the date value from each result.
		PrintWhiteSpace();
		System.out.print("Would you like to specify a date range? [Yes/No]: ");
		userInput = userIn.next();
		userIn.nextLine();
		if(userInput.equals("Yes")) {
			PrintWhiteSpace();
			System.out.print("Specify the year you would like to search (ex. 2024): ");
			userYear = userIn.next().substring(2,4);
			userIn.nextLine();
			System.out.print("Specify the month you would like to search as a two-digit number (ex. May = 05) \nEnter 'No' if you do not want to specify a month: ");
			userMonth = userIn.next();
			userIn.nextLine();
			if(!userMonth.equals("No")) {//if user specified a year and a month.
				System.out.println("Searching for entries within " + userMonth + "/" + userYear);
				while(in.hasNextLine()) {
					if(i != 0) {
						Scanner line = new Scanner(in.nextLine());
						line.useDelimiter(",");
						String date = line.next();
						if(date.isEmpty()) {
							break;
						}
						if(date.substring(6,8).equals(userYear) && date.substring(0,2).equals(userMonth)) {
							arr.add(line.next() + " " + line.next() + " " + line.next() + " " + line.next() + " " + line.next());
						}
					}
					else {
						in.nextLine();
					}
					i++;
				}//end while
			}
			else {//if user did not specify a month.
				while(in.hasNextLine()) {
					if(i != 0) {
						Scanner line = new Scanner(in.nextLine());
						line.useDelimiter(",");
						String date = line.next();
						if(date.isEmpty()) {
							break;
						}
						if(date.substring(6,8).equals(userYear)) {
							arr.add(line.next() + " " + line.next() + " " + line.next() + " " + line.next() + " " + line.next());
						}
					}
					else {
						in.nextLine();
					}
					i++;
				}//end while
			}
		}
		else {//if user did not specify a year or a month.
			while(in.hasNextLine()) {
				if(i != 0) {
					Scanner line = new Scanner(in.nextLine());
					line.useDelimiter(",");
					String date = line.next();
					if(date.isEmpty()) {
						break;
					}
					arr.add(line.next() + " " + line.next() + " " + line.next() + " " + line.next() + " " + line.next());
				}
				else {
					in.nextLine();
				}
				i++;
			}//end while
		}
		in.close();
		
		//Close search if no results are found.
		if(arr.size() == 0) {
			System.out.print("No results found. Enter any key to return the main menu: ");
			String failure = userIn.next();
			if(!failure.isEmpty()) {
				System.out.println("Returing to main menu.");
			}
			userIn.nextLine();
			return;
		}
		
		//Ask the user for search critera for that data set.
		PrintWhiteSpace();
		System.out.println("Data loaded. " + arr.size() + " results found.");
		System.out.println("What description would you like to search for? Common descriptions are: ");
		System.out.println("fast food");
		System.out.println("salary");
		System.out.println("tips");
		System.out.println("online");
		System.out.println("gas");
		System.out.println("donation");
		System.out.println("groceries");
		System.out.print("Your selection: ");
		userDescChoice = userIn.next();
		userIn.nextLine();
		
		//Optional specification to add to search.
		PrintWhiteSpace();
		System.out.println("Is there a specification you would like to add to your search? Enter an organization's or person's name.");
		System.out.print("Alternatively, enter No to proceed without a search specification: ");
		userSpecChoice = userIn.next();
		userIn.nextLine();
		
		if(userSpecChoice.equals("No")) {//if user provided only a description.
			for(int j = 0; j < arr.size(); j++) {
				String[] line = arr.get(j).split(" ");
				if(line[0].equals(userDescChoice)) {
					totalSpent += Double.parseDouble(line[2]);
					totalEarned += Double.parseDouble(line[3]);
				}
			}//end for loop
			
			//Print results of search
			PrintWhiteSpace();
			System.out.println("Total spent on " + userDescChoice + ": " + totalSpent);
			System.out.println("Total earned through " + userDescChoice + ": " + totalEarned);
		}
		else {//if user provided only a description.
			for(int j = 0; j < arr.size(); j++) {
				String[] line = arr.get(j).split(" ");
				if(line[0].equals(userDescChoice) && line[1].equals(userSpecChoice)) {
					totalSpent += Double.parseDouble(line[2]);
					totalEarned += Double.parseDouble(line[3]);
				}
			}//end for loop
			
			//Print results of search.
			PrintWhiteSpace();
			System.out.println("Total spent on " + userDescChoice + " from " + userSpecChoice + ": " + totalSpent);
			System.out.println("Total earned through " + userDescChoice + " from " + userSpecChoice + ": " + totalEarned);
		}
		
		//Allow user to view data
		System.out.print("Results printed above. Enter any key to return the main menu: ");
		String success = userIn.next();
		if(!success.isEmpty()) {
			System.out.println("Returing to main menu.");
		}
		userIn.nextLine();
		return;
	}// end LookupData
}// end register class