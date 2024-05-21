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
		
		//Starts the program.
		File file = StartProgram(date, userIn);
		
		// STANDARD PROGRAM LOOP
		Boolean programRunning = true;
		do {
			PrintWhiteSpace();
			System.out.println("File accessed successfully.\nChoose an option from the following list:");
			System.out.println("1 - Add an entry");
			System.out.println("2 - Lookup Data");
			System.out.println("3 - Load File from Navy Federal");
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
				LoadNFData(file, userIn);
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

	//PERFORMS STANDARD PROGRAM STARTUP. TRIGGERS VerifyFile().
	public static File StartProgram(String date, Scanner userIn) throws IOException {
		//Variable declaration
		int userChoice;
		String filePath = "";
		String userEntry = "";
		ArrayList<String> filePaths = new ArrayList<>();
		File pathsMemoryFile = new File("Register Paths Memory.txt");
		Scanner memoryScan;
		Boolean invalidChoice;
		
		//Welcome message
		System.out.println("Starting up your personal register system!");
				
		//Verify paths memory text file exists, create one if not and tell the user.
		if(pathsMemoryFile.exists() == false) {
			pathsMemoryFile.createNewFile();
			PrintWhiteSpace();
			System.out.println("Created a new memory file (Register Paths Memory.txt) at the directory in which this program is stored.");
			System.out.println("This file will store filepaths for quick startup of this program in the future.");
		}
				
		//Find saved filepaths; determine which to use.
		memoryScan = new Scanner(pathsMemoryFile);
		if(memoryScan.hasNextLine() == false) {
			PrintWhiteSpace();
			System.out.println("No file path is saved in memory. Please enter a file path and name at which to output the register file.");
			System.out.print("(ex. C:\\Users\\Administrator\\Documents\\Official Documents\\Income and Expenses): ");
			String userInFile = userIn.nextLine() + ".csv";
			FileWriter out = new FileWriter(pathsMemoryFile, true);
			out.write(userInFile);
			out.close();
			PrintWhiteSpace();
			System.out.println("Path saved to memory file. Please reload this program to access the new path.");
			System.exit(0);
		}
		
		//Look through all lines of memory and add them to an arraylist.
		while(memoryScan.hasNextLine()) {
			String currLine = memoryScan.nextLine();
			filePaths.add(currLine);
		}
		memoryScan.close();
		
		//Show available paths to user and ask them to choose one.
		do {
			PrintWhiteSpace();
			System.out.println("Which file would you like to use? Enter the integer corresponding to your choice.");
			for(int i = 0; i < filePaths.size(); i++) {
				System.out.println(i+1 + ": " + filePaths.get(i));
			}
			System.out.println("Alternatively, add a new filepath by typing 'Add'.");
			System.out.print("Your selection: ");
			userEntry = userIn.next();
			userIn.nextLine();
			if(userEntry.equals("Add")) {
				PrintWhiteSpace();
				System.out.println("Please enter a file path and name to output the document.");
				System.out.print("(ex. C:\\Users\\Administrator\\Documents\\Official Documents\\Income and Expenses): ");
				String userInFile = userIn.nextLine();
				FileWriter out = new FileWriter(pathsMemoryFile, true);
				out.write("\n");
				out.write(userInFile + ".csv");
				out.close();
				PrintWhiteSpace();
				System.out.println("Path saved to memory file. Please reload this program to access the new path.");
				System.exit(0);
			}
			
			//Attempt to open the file selected by the user.
			invalidChoice = false;
			try {
				userChoice = Integer.parseInt(userEntry);
				filePath = filePaths.get(userChoice - 1);
			}
			catch(IndexOutOfBoundsException i) {
				PrintWhiteSpace();
				System.out.println("Invalid choice. Please enter one of the available numbers or add a new path.");
				invalidChoice = true;
			}
			catch(NumberFormatException n) {
				PrintWhiteSpace();
				System.out.println("Invalid choice. Please enter a number or type 'Add'.");
				invalidChoice = true;
			}
		}
		while(invalidChoice);
		File file = new File(filePath);
		
		//Checks that the file exists and has been established.
		VerifyFile(file, filePath, date, userIn);
		
		//Return the desired file and begin the program.
		return file;
	}//end StartProgram
	
	//LOADS DATA FROM A NAVY FEDERAL .csv FILE.
	public static void LoadNFData(File file, Scanner userIn) throws IOException {
		//Variable declaration
		Scanner baseFile = new Scanner(file);
		Scanner newFile = null;
		String filePath = "";
		ArrayList<String> inputData = new ArrayList<>();
		ArrayList<String> existingData = new ArrayList<>();
		ArrayList<String> sortedData = new ArrayList<>();
		Boolean invalidPath = false;
		
		//Ask user for a file path to the input file.
		do {
			invalidPath = false;
			PrintWhiteSpace();
			System.out.print("Enter the file path of the input file here (ex. C:\\Users\\Administrator\\Downloads\\NFCU_Credit_Card): ");
			filePath = userIn.next() + ".csv";
			userIn.nextLine();
			File nfFile = new File(filePath);
			try {//Check to make sure the provided file path is valid.
				newFile = new Scanner(nfFile);
			}
			catch(FileNotFoundException f) {
				PrintWhiteSpace();
				System.out.println("The system cannot find the file specified. Please check the entered file path.");
				invalidPath = true;
			}
		}
		while(invalidPath);//Repeat previous steps

		//Load the new file's data and close the file.
		PrintWhiteSpace();
		System.out.println("Loading file data from");
		System.out.println(filePath);
		System.out.println("to the current register.");
		newFile.nextLine(); //Clear the first line (header) from the input file.
		while(newFile.hasNextLine()) {
			Scanner curLine = new Scanner(newFile.nextLine());
			curLine.useDelimiter(",");
			String entry = curLine.next();//Start entry by adding the date.
			Double amount = curLine.nextDouble();//Record amount of change.
			String changeType = curLine.next();//Debit or Credit.
			curLine.next();//Skip unnecessary/nonexistent data +5 lines.
			curLine.next();
			curLine.next();
			curLine.next();
			curLine.next();
			curLine.next();
			String specification = curLine.next();//Company/organization name.
			if(specification.equals("CRONIN ACE BAYMEADOWS"))//TROUBLE: THIS ORGANIZATION HAS A COMMA IN THEIR NAME!
				curLine.next();
			String description = curLine.next();//Type of charge.
			curLine.nextLine();//No further data required.
			Double spent = 0.0;
			Double earned = 0.0;
			if(changeType.equals("Debit"))
				spent = amount;
			else if(changeType.equals("Credit"))
				earned = amount;
			entry = entry + "," + description + "," + specification + "," + spent + "," + earned;
			inputData.add(entry);
		}
		newFile.close();
		
		//Load the existing file's data and close the file.
		baseFile.nextLine(); //Clear the first line (header) from the base file.
		while(baseFile.hasNextLine()) {
			String curLine = baseFile.nextLine();
			if(curLine.isEmpty() == false) {
				existingData.add(curLine);
			}
		}
		baseFile.close();
		
		//Sort the new file's data by date along with the existing file's data.
		//sortedData.add(existingData.get(0));//Seed value to begin sorting with.
		for(int i = 0; i < existingData.size(); i++) {
			sortedData.add(existingData.get(i));
		}
		
		for(int i = 0; i < inputData.size(); i++) {
			sortedData.add(inputData.get(i));
		}//FIXME: ADD AN ACTUAL SORTING ALGORITHM.
		
		//Write the sorted data back to the base file; overwrite all data in the base file.
		try {
			FileWriter out = new FileWriter(file);
			out.write("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");//Write the file header.
			for(int i = 0; i < sortedData.size(); i++) {
				out.write("\n");
				out.write(sortedData.get(i));
			}
			out.close();
		}
		catch (FileNotFoundException e) {
			PrintWhiteSpace();
			System.out.println("Unable to write to the output file. Make sure it isn't open!");
			System.out.print("Press any key to return to main menu: ");
			userIn.next();
			userIn.nextLine();
			return;
		}
		
		//Take user back to the main menu.
		PrintWhiteSpace();
		System.out.print("Successfully loaded data into the selected register. Press any key to return to main menu: ");
		userIn.next();
		userIn.nextLine();
		return;
	}//end LoadNFData

	//CREATES A BACKUP OF THE FILE.
	public static void CreateBackup(File file, Scanner userIn) throws IOException {
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
		
		// If unable to find the file, create a new one.
		try (Scanner test = new Scanner(file)) {}
		catch (FileNotFoundException e) {
			PrintWhiteSpace();
			System.out.println("No file yet exists at " + filePath);
			System.out.print("Creating a new register file. Would you like to change the file path or name? [Yes/No]: ");
			String answer = userIn.next();
			userIn.nextLine();
			// Ask the user if they would like to specify a new file path and name.
			if (answer.equals("Yes")) {
				System.out.print("Specify a new file path and file name here (ex. C:\\Users\\Administrator\\Documents\\Income and Expenses): ");
				filePath = userIn.nextLine();
				filePath = filePath + ".csv";
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
					writer.println();
					writer.println(date + ",Initial balance,No stating balance transfer,0.00,0.00,0.00");
				}
			} catch (IOException f) {}
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
	public static void AddEntry(File file, String date, Scanner userIn) throws IOException {
		// Variable declaration
		Double curBalance = 0.0;
		Double spent = 0.0;
		Double earned = 0.0;
		Boolean anotherEntry = true;
		String description = "";
		String specification = "";
		File descsMemoryFile = new File("Descriptions Memory.txt");
		ArrayList<String> loadedDescs = new ArrayList<>();
		ArrayList<String> validRows = new ArrayList<>();

		//Verify descriptions memory file exists, create one if not and tell the user.
		if(descsMemoryFile.exists() == false) {
			descsMemoryFile.createNewFile();
			PrintWhiteSpace();
			System.out.println("Created a new description preference file (Descriptions Memory.txt) at the directory in which this program is stored.");
			System.out.println("This file will store already-used descriptions for future organizational assistance.");
		}
		
		//Enter main method loop.
		do {
			//Find the last entry of the register. Remove empty rows from the file, to allow for easier access of the last valid row.
			Scanner in = new Scanner(file);
			while (in.hasNextLine()) {
				String curLine = in.nextLine();
				if(curLine.length() > 5) {//Only add non-empty rows.
					validRows.add(curLine);
				}
			}//end while loop
			Scanner lastLine = new Scanner(validRows.get(validRows.size()-1));//Scan the last line.
			lastLine.useDelimiter(",");
			
			// Find the current balance of the account from the last line. 
			for (int i = 0; i < 5; i++) {
				lastLine.next();
			}
			curBalance = lastLine.nextDouble();
			lastLine.close();
			in.close();
			PrintWhiteSpace();
			System.out.println("You are now adding an entry to the register. The current balance of this account is " + curBalance + "$.");

			// Gather user data; present list of existing general descriptions. //FIXME Data not writing to file? 
			System.out.println("Please enter a one-word description of the change.");
			Scanner descScanner = new Scanner(descsMemoryFile);
			if(descScanner.hasNextLine()) {//Print out saved descriptions
				System.out.println("Existing descriptions are: ");
				while(descScanner.hasNextLine()) {
					String curDescLine = descScanner.nextLine();
					loadedDescs.add(curDescLine);
					System.out.println(curDescLine);
				}
				descScanner.close();
			}
			else {
				System.out.println("Common descriptions are: \nGasoline/Fuel\nGroceries\nRestaurants/Dining\nSalary\nTips\nGeneralMerchandise\nHomeImprovement");
			}
			System.out.print("Your selection: ");
			description = userIn.next();
			userIn.nextLine();
			for(int i = 0; i < loadedDescs.size(); i++) {//If the user's description does not match an existing description, add it to the file.
				if(description.equals(loadedDescs.get(i)) == false) {
					FileWriter out = new FileWriter(descsMemoryFile, true);
					out.write("\n");
					out.write(loadedDescs.get(i));
					out.close();
				}
			}
			System.out.print("Please enter a one-word specification if applicable (ex. Burger King, Doordash, etc.): ");
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
		File descsMemoryFile = new File("Descriptions Memory.txt");
		Scanner in = new Scanner(file);
		Scanner descScanner = new Scanner(descsMemoryFile);
		ArrayList<String> arr = new ArrayList<>();
		ArrayList<String> loadedDescs = new ArrayList<>();
		
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
			descScanner.close();
			return;
		}
		
		//Ask the user for search critera for that data set.
		PrintWhiteSpace();
		System.out.println("Data loaded. " + arr.size() + " results found.");
		System.out.println("What description would you like to search for?");
		if(descScanner.hasNextLine()) {//Print out saved descriptions
			System.out.println("Existing descriptions are: ");
			while(descScanner.hasNextLine()) {
				String curDescLine = descScanner.nextLine();
				loadedDescs.add(curDescLine);
				System.out.println(curDescLine);
			}
			descScanner.close();
		}
		else {
			System.out.println("Common descriptions are: \nGasoline/Fuel\nGroceries\nRestaurants/Dining\nSalary\nTips\nGeneralMerchandise\nHomeImprovement");
		}
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