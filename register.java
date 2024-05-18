import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
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
			System.out.println("2 - List statistics");
			System.out.println("3 - ");
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
				ListStatistics(file); //also remember to pass this the scanner, since things break otherwise...
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
			System.out.println("Please enter a description of the change. Common descriptions are: ");
			System.out.println("fast food");
			System.out.println("salary");
			System.out.println("tips");
			System.out.println("online order");
			System.out.println("gas");
			System.out.println("donation");
			System.out.println("groceries");
			System.out.print("Your selection: ");
			description = userIn.next();
			userIn.nextLine();
			System.out.print("Please enter specification if applicable (ex. Burger King, Doordash,  n/a, etc.): ");
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

	// LISTS VARIOUS STATISTICS ABOUT THE FILE.
	public static void ListStatistics(File file) {
		
	}// end ListStatistics
}// end class
