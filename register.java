import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Scanner;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

/*
 * CONVENTIONS
 * 
 * 1. When formatting money output, use the following format, where curBalance is the variable containing the money amount:
 * Formatter dollarFormat = new Formatter();
 * System.out.println("$" + dollarFormat.format("%.2f", curBalance) + ".");
 * dollarFormat.close();
 * MAKE SURE TO CLOSE THE FORMATTER AFTER EACH USE.
 * 
 * 2. Always read user input data using nextLine(). 
 * Do not use next() or nextInt(). 
 * If reading a number from the user, use the following:
 * choice = Integer.parseInt(userIn.nextLine());
 * or
 * choice = Double.parseDouble(userIn.nextLine());
 * 
 * 3. The comparator used in LoadBarclaysAnnualData, LoadBarclaysMonthData and LoadNFData is the same.
 */

public class register {
	public static void main(String[] args) throws IOException {
		// Variable declaration
		Scanner userIn = new Scanner(System.in);
		LocalDateTime time = java.time.LocalDateTime.now();
		String date = time.toString();
		date = date.substring(0, 10);// Exclude hours, minutes and seconds
		int choice = 0;

		// Starts the program.
		File file = StartProgram(date, userIn);

		// Let user know the program started normally.
		PrintWhiteSpace();
		System.out.println("File accessed successfully.");

		// STANDARD PROGRAM LOOP
		Boolean programRunning = true;
		do {
			PrintWhiteSpace();
			System.out.println("Choose an option from the following list:");
			System.out.println("1 - Add an entry");
			System.out.println("2 - Lookup Data");
			System.out.println("3 - Load file from Navy Federal");
			System.out.println("4 - Load annual summary from Barclay's");
			System.out.println("5 - Load monthly summary from Barclay's");
			System.out.println("6 - Create backup");
			System.out.println("7 - End program");
			System.out.print("Your selection: ");
			try {
				choice = Integer.parseInt(userIn.nextLine());
			} catch (NumberFormatException i) {
				PrintWhiteSpace();
				System.out.print("Please enter a valid number!");
				continue;
			}
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
				LoadBarclaysAnnualData(file, userIn);
				break;
			case 5:
				LoadBarclaysMonthlyData(file, userIn);
			case 6:
				CreateBackup(file, userIn);
				break;
			case 7:
				programRunning = false;
				break;
			}// end switch FIXME: Add a function to the register to calculate a percentage of earned income of a type from a specific date range.
		} // end loop
		while (programRunning);
		// End program
		userIn.close();
		PrintWhiteSpace();
		System.out.println("Program terminated.");
	}// end main

	// LOADS DATA FROM A BARCLAY'S PDF ANNUAL SUMMARY FILE.
	public static void LoadBarclaysAnnualData(File file, Scanner userIn) throws IOException {
		// Variable declaration
		Scanner baseFile = new Scanner(file);
		String filePath = "";
		ArrayList<String> inputData = new ArrayList<>();
		ArrayList<String> existingData = new ArrayList<>();
		ArrayList<String> sortedDupeData = new ArrayList<>();
		ArrayList<String> sortedData = new ArrayList<>();
		Boolean invalidPath = false;
		File barclaysFile;
		Scanner newFile;

		// Ask user for a file path to the input file.
		do {
			invalidPath = false;
			PrintWhiteSpace();
			System.out.print(
					"Enter the file path of the input file here (ex. C:\\Users\\Administrator\\Downloads\\Temporary\\Annual Summary): ");
			filePath = userIn.nextLine() + ".pdf";
			// userIn.nextLine();
			barclaysFile = new File(filePath);
			try {// Check to make sure the provided file path is valid and the loader is
					// operational.
				PDDocument pdf = Loader.loadPDF(barclaysFile);
				new PDFTextStripper().getText(pdf);
			} catch (FileNotFoundException f) {
				PrintWhiteSpace();
				System.out.println("The system cannot find the file specified. Please check the entered file path.");
				invalidPath = true;
			} catch (NoClassDefFoundError n) {// Indicates an issue with installed libraries.
				PrintWhiteSpace();
				System.out.println("Critical error. Unable to call the PDF Loader.");
				System.out.println("The system cannot run without Apache's PDFBox Library installed.");
				System.out.println("Only pdfbox-app-3.0.2.jar needs to be added as an external JAR file.");
				System.out.println("https://pdfbox.apache.org/download.cgi");
				System.exit(0);
			}
		} while (invalidPath);// Repeat previous steps if the path is invalid. End do while.

		// Load the new file's data and close the file scanner.
		PrintWhiteSpace();
		System.out.println("Loading file data from");
		System.out.println(filePath);
		System.out.println("to the current register.");
		PDDocument pdf = Loader.loadPDF(barclaysFile);
		String newFileOutput = new PDFTextStripper().getText(pdf);// Collect a string containing all the lines of the
																	// PDF document.
		newFile = new Scanner(newFileOutput);
		while (newFile.hasNextLine()) {
			String curLine = newFile.nextLine();
			if (curLine.length() > 2) {
				String date = curLine.substring(0, 3);
				if (date.equals("Jan") || date.equals("Feb") || date.equals("Mar") || date.equals("Apr")
						|| date.equals("May") || date.equals("Jun") || date.equals("Jul") || date.equals("Aug")
						|| date.equals("Sep") || date.equals("Oct") || date.equals("Nov") || date.equals("Dec")) {
					Scanner lineScanner = new Scanner(curLine);
					lineScanner.useDelimiter(" ");
					// Decipher the date. Ex. "Dec 30, 2023"
					String month = lineScanner.next();
					if (month.equals("Jan"))
						month = "01";
					else if (month.equals("Feb"))
						month = "02";
					else if (month.equals("Mar"))
						month = "03";
					else if (month.equals("Apr"))
						month = "04";
					else if (month.equals("May"))
						month = "05";
					else if (month.equals("Jun"))
						month = "06";
					else if (month.equals("Jul"))
						month = "07";
					else if (month.equals("Aug"))
						month = "08";
					else if (month.equals("Sep"))
						month = "09";
					else if (month.equals("Oct"))
						month = "10";
					else if (month.equals("Nov"))
						month = "11";
					else if (month.equals("Dec"))
						month = "12";
					String day = lineScanner.next();
					day = day.replace(",", "");
					String year = lineScanner.next();
					String entryDate = month + "/" + day + "/" + year;
					String description = "No Description"; // Barclay's does not offer a description.
					// Use the remainder of the string for the specification and amount spent or
					// earned.
					// Ex. "PAYPAL *WILDLIFESOS $70.00"
					String remainder = lineScanner.nextLine();
					int endingIndex = remainder.indexOf('$');
					String specification = remainder.substring(0, endingIndex - 1);
					specification = specification.replace(",", "");
					Double earned = 0.0;
					Double spent = 0.0;
					if (remainder.contains("(") && remainder.contains(")")) {// Parentheses imply this transaction is a
																				// deposit.
						int moneyPosition = remainder.indexOf('$');
						String amount = remainder.substring(moneyPosition + 1, remainder.length() - 1);
						amount = amount.replace(",", "");
						earned = Double.parseDouble(amount);
					} else {
						int moneyPosition = remainder.indexOf('$');
						String amount = remainder.substring(moneyPosition + 1, remainder.length() - 1);
						amount = amount.replace(",", "");
						spent = Double.parseDouble(amount);
					}
					String entry = entryDate + "," + description + "," + specification + "," + spent + "," + earned;
					inputData.add(entry);
					lineScanner.close();
				}
			}
		} // end while loop
		newFile.close();

		// Load the existing file's data and close the file scanner.
		baseFile.nextLine(); // Clear the first line (header) from the base file.
		while (baseFile.hasNextLine()) {// Write every other line to an ArrayList.
			String curLine = baseFile.nextLine();
			if (curLine.isEmpty() == false) {
				existingData.add(curLine);
			}
		} // end while loop
		baseFile.close();

		// Add all data to a single array list.
		for (int i = 0; i < existingData.size(); i++) {
			sortedDupeData.add(existingData.get(i));
		} // end for loop
		for (int i = 0; i < inputData.size(); i++) {
			sortedDupeData.add(inputData.get(i));
		} // end for loop

		// Remove duplicates from the sorted list, if any.
		for (int i = 0; i < sortedDupeData.size(); i++) {
			String balEntry = sortedDupeData.get(i);
			Scanner curLine = new Scanner(balEntry);
			curLine.useDelimiter(",");
			String noBalEntry = curLine.next() + "," + curLine.next() + "," + curLine.next() + "," + curLine.next()
					+ "," + curLine.next();// Add every field but balance
			curLine.close();
			if (!sortedData.contains(noBalEntry)) {
				sortedData.add(noBalEntry);
			}
		} // end for loop

		// Sort the array list by date.
		SortByDate(sortedData);

		// Write the sorted data back to the base file.
		// Overwrite all data in the base file.
		Double balance = 0.0;
		try {
			FileWriter out = new FileWriter(file);
			out.write("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");// Write the file header.
			// Decipher the new balance as new entries are added.
			for (int i = 0; i < sortedData.size(); i++) {
				String curLine = sortedData.get(i);
				Scanner lineScan = new Scanner(curLine);
				lineScan.useDelimiter(",");
				lineScan.next();// Skip date
				lineScan.next();// Skip description
				lineScan.next();// Skip specification
				Double spent = lineScan.nextDouble();// FIXME: comma in organization name
				Double earned = lineScan.nextDouble();
				balance -= spent;
				balance += earned;
				lineScan.close();
				out.write("\n");
				out.write(curLine + "," + balance);
			}
			out.close();
		} catch (FileNotFoundException e) {
			PrintWhiteSpace();
			System.out.println("Unable to write to the output file. Make sure it isn't open!");
			System.out.print("Press any key to return to main menu: ");
			userIn.nextLine();
			return;
		}

		// Take user back to the main menu.
		PrintWhiteSpace();
		System.out
				.print("Successfully loaded data into the selected register.\nPress any key to return to main menu: ");
		userIn.nextLine();
		return;
	}// End LoadBarclaysAnnualData

	// LOADS DATA FROM A BARCLAY'S PDF MONTHLY SUMMARY FILE.
	public static void LoadBarclaysMonthlyData(File file, Scanner userIn) throws IOException {
		// Variable declaration
		Scanner baseFile = new Scanner(file);
		String filePath = "";
		ArrayList<String> inputData = new ArrayList<>();
		ArrayList<String> existingData = new ArrayList<>();
		ArrayList<String> sortedDupeData = new ArrayList<>();
		ArrayList<String> sortedData = new ArrayList<>();
		Boolean invalidPath = false;
		File barclaysFile;
		Scanner newFile;

		// Ask user for a file path to the input file.
		do {
			invalidPath = false;
			PrintWhiteSpace();
			System.out.print(
					"Enter the file path of the input file here (ex. C:\\Users\\Administrator\\Downloads\\Temporary\\Barclay_June_2024): ");
			filePath = userIn.nextLine() + ".pdf";
			// userIn.nextLine();
			barclaysFile = new File(filePath);
			try {// Check to make sure the provided file path is valid and the loader is
					// operational.
				PDDocument pdf = Loader.loadPDF(barclaysFile);
				new PDFTextStripper().getText(pdf);
			} catch (FileNotFoundException f) {
				PrintWhiteSpace();
				System.out.println("The system cannot find the file specified. Please check the entered file path.");
				invalidPath = true;
			} catch (NoClassDefFoundError n) {// Indicates an issue with installed libraries.
				PrintWhiteSpace();
				System.out.println("Critical error. Unable to call the PDF Loader.");
				System.out.println("The system cannot run without Apache's PDFBox Library installed.");
				System.out.println("Only pdfbox-app-3.0.2.jar needs to be added as an external JAR file.");
				System.out.println("https://pdfbox.apache.org/download.cgi");
				System.exit(0);
			}
		} while (invalidPath);// Repeat previous steps if the path is invalid. End do while.

		// Load the new file's data and close the file scanner.
		PrintWhiteSpace();
		System.out.println("Loading file data from");
		System.out.println(filePath);
		System.out.println("to the current register.");
		PDDocument pdf = Loader.loadPDF(barclaysFile);
		String newFileOutput = new PDFTextStripper().getText(pdf);// Collect a string containing all the lines of the
																	// PDF document.
		newFile = new Scanner(newFileOutput);
		while (newFile.hasNextLine()) {
			String curLine = newFile.nextLine();
			if (curLine.length() > 2) {
				String date = curLine.substring(0, 3);
				if (date.equals("Jan") || date.equals("Feb") || date.equals("Mar") || date.equals("Apr")
						|| date.equals("May") || date.equals("Jun") || date.equals("Jul") || date.equals("Aug")
						|| date.equals("Sep") || date.equals("Oct") || date.equals("Nov") || date.equals("Dec")) {
					Scanner lineScanner = new Scanner(curLine);
					lineScanner.useDelimiter(" ");
					// Decipher the date. Ex. "May 31 May 31" FIXME: record transaction date or posting date?
					String month = lineScanner.next();
					if (month.equals("Jan"))
						month = "01";
					else if (month.equals("Feb"))
						month = "02";
					else if (month.equals("Mar"))
						month = "03";
					else if (month.equals("Apr"))
						month = "04";
					else if (month.equals("May"))
						month = "05";
					else if (month.equals("Jun"))
						month = "06";
					else if (month.equals("Jul"))
						month = "07";
					else if (month.equals("Aug"))
						month = "08";
					else if (month.equals("Sep"))
						month = "09";
					else if (month.equals("Oct"))
						month = "10";
					else if (month.equals("Nov"))
						month = "11";
					else if (month.equals("Dec"))
						month = "12";
					String day = lineScanner.next();
					day = day.replace(",", "");
					String year = lineScanner.next();//FIXME: year will be the same for the whole file and is not listed in the entries.
					String entryDate = month + "/" + day + "/" + year;
					String description = "No Description"; // Barclay's does not offer a description.
					// Use the remainder of the string for the specification and amount spent or
					// earned.
					// Ex. "PAYPAL *WILDLIFESOS $70.00"
					String remainder = lineScanner.nextLine();
					// System.out.println(remainder);//debugging
					int endingIndex = remainder.indexOf('$');
					String specification = remainder.substring(0, endingIndex - 1);//FIXME: program failing on entry that takes more than one line.
					specification = specification.replace(",", "");
					Double earned = 0.0;
					Double spent = 0.0;
					if (remainder.contains("(") && remainder.contains(")")) {// Parentheses imply this transaction is a
																				// deposit. FIXME: Deposit now designated with a negative, or "-$".
						int moneyPosition = remainder.indexOf('$');
						String amount = remainder.substring(moneyPosition + 1, remainder.length() - 1);
						amount = amount.replace(",", "");
						earned = Double.parseDouble(amount);
					} else {
						int moneyPosition = remainder.indexOf('$');
						String amount = remainder.substring(moneyPosition + 1, remainder.length() - 1);
						amount = amount.replace(",", "");
						spent = Double.parseDouble(amount);
					}
					String entry = entryDate + "," + description + "," + specification + "," + spent + "," + earned;
					inputData.add(entry);
					lineScanner.close();
				}
			}
		} // end while loop
		newFile.close();

		// Load the existing file's data and close the file scanner.
		baseFile.nextLine(); // Clear the first line (header) from the base file.
		while (baseFile.hasNextLine()) {// Write every other line to an ArrayList.
			String curLine = baseFile.nextLine();
			if (curLine.isEmpty() == false) {
				existingData.add(curLine);
			}
		} // end while loop
		baseFile.close();

		// Add all data to a single array list.
		for (int i = 0; i < existingData.size(); i++) {
			sortedDupeData.add(existingData.get(i));
		} // end for loop
		for (int i = 0; i < inputData.size(); i++) {
			sortedDupeData.add(inputData.get(i));
		} // end for loop

		// Remove duplicates from the sorted list, if any.
		for (int i = 0; i < sortedDupeData.size(); i++) {
			String balEntry = sortedDupeData.get(i);
			Scanner curLine = new Scanner(balEntry);
			curLine.useDelimiter(",");
			String noBalEntry = curLine.next() + "," + curLine.next() + "," + curLine.next() + "," + curLine.next()
					+ "," + curLine.next();// Add every field but balance
			curLine.close();
			if (!sortedData.contains(noBalEntry)) {
				sortedData.add(noBalEntry);
			}
		} // end for loop

		// Sort the ArrayList by date.
		SortByDate(sortedData);

		// Write the sorted data back to the base file.
		// Overwrite all data in the base file.
		Double balance = 0.0;
		try {
			FileWriter out = new FileWriter(file);
			out.write("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");// Write the file header.
			// Decipher the new balance as new entries are added.
			for (int i = 0; i < sortedData.size(); i++) {
				String curLine = sortedData.get(i);
				Scanner lineScan = new Scanner(curLine);
				lineScan.useDelimiter(",");
				lineScan.next();// Skip date
				lineScan.next();// Skip description
				lineScan.next();// Skip specification
				Double spent = lineScan.nextDouble();// FIXME: comma in organization name
				Double earned = lineScan.nextDouble();
				balance -= spent;
				balance += earned;
				lineScan.close();
				out.write("\n");
				out.write(curLine + "," + balance);
			}
			out.close();
		} catch (FileNotFoundException e) {
			PrintWhiteSpace();
			System.out.println("Unable to write to the output file. Make sure it isn't open!");
			System.out.print("Press any key to return to main menu: ");
			userIn.nextLine();
			return;
		}
		// Take user back to the main menu.
		PrintWhiteSpace();
		System.out
				.print("Successfully loaded data into the selected register.\nPress any key to return to main menu: ");
		userIn.nextLine();
		return;
	}// end LoadBarclaysMonthData

	// SORTS AN ARRAYLIST OF ENTRIES BY THIER DATES.
	private static void SortByDate(ArrayList<String> sortedData) {
		sortedData.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				DateTimeFormatter formatter = null;
				String firstDate = o1.substring(0, 10);
				String secondDate = o2.substring(0, 10);

				// Analyze the format of the first date.
				if (firstDate.substring(0, 3).matches("[0-9]+")) {// Check if the date begins with the year (2024-06-07)
					formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				} else if (firstDate.substring(6).matches("[0-9]+") == false && firstDate.charAt(9) != ',') {// Check if
																												// the
																												// year
																												// is
																												// represented
																												// with
																												// only
																												// two
																												// digits
																												// (05/17/24)
					firstDate = firstDate.substring(0, 7);
					formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
				} else if (firstDate.substring(3, 5).matches("[0-9]+") == false) {// Check if the day is represented
																					// with only one digit (03/9/2023)
					firstDate = firstDate.substring(0, 9);// remove the trailing comma
					// Add a zero before the day figure
					String firstHalf = firstDate.substring(0, 2);
					String secondHalf = firstDate.substring(3);
					firstDate = firstHalf + "/0" + secondHalf;
					formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				} else
					formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				LocalDate firstRealDate = LocalDate.parse(firstDate, formatter);
				// Analyze the format of the second date.
				if (secondDate.substring(0, 3).matches("[0-9]+")) {// Check if the date begins with the year
																	// (2024-06-07)
					formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				} else if (secondDate.substring(6).matches("[0-9]+") == false && secondDate.charAt(9) != ',') {// Check
																												// if
																												// the
																												// year
																												// is
																												// represented
																												// with
																												// only
																												// two
																												// digits
																												// (05/17/24)
					secondDate = secondDate.substring(0, 7);
					formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
				} else if (secondDate.substring(3, 5).matches("[0-9]+") == false) {// Check if the day is represented
																					// with only one digit (03/9/2023)
					secondDate = secondDate.substring(0, 9);// remove the trailing comma
					// Add a zero before the day figure
					String firstHalf = secondDate.substring(0, 2);
					String secondHalf = secondDate.substring(3);
					secondDate = firstHalf + "/0" + secondHalf;
					formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				} else
					formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				LocalDate secondRealDate = LocalDate.parse(secondDate, formatter);

				// Compare both dates.
				return firstRealDate.compareTo(secondRealDate);
			}
		});
	}// end SortByDate

	// PERFORMS STANDARD PROGRAM STARTUP. TRIGGERS VerifyFile().
	public static File StartProgram(String date, Scanner userIn) throws IOException {
		// Variable declaration
		int userChoice;
		String filePath = "";
		String userEntry = "";
		ArrayList<String> filePaths = new ArrayList<>();
		File pathsMemoryFile = new File("Register Paths Memory.txt");
		Scanner memoryScan;
		Boolean invalidChoice;

		// Welcome message
		System.out.println("Starting up your personal register system!");

		// Verify paths memory text file exists, create one if not and tell the user.
		if (pathsMemoryFile.exists() == false) {
			pathsMemoryFile.createNewFile();
			PrintWhiteSpace();
			System.out.println(
					"Created a new memory file (Register Paths Memory.txt) at the directory in which this program is stored.");
			System.out.println("This file will store filepaths for quick startup of this program in the future.");
		}

		// Find saved file paths.
		memoryScan = new Scanner(pathsMemoryFile);
		if (memoryScan.hasNextLine() == false) {// If none exist, prompt the user to create one.
			PrintWhiteSpace();
			System.out.println(
					"No file path is saved in memory. Please enter a file path and name at which to output the register file.");
			System.out.print("(ex. C:\\Users\\Administrator\\Documents\\Official Documents\\Income and Expenses): ");
			String userInFile = userIn.nextLine() + ".csv";// FIXME: validate this path before saving it to memory.
			FileWriter out = new FileWriter(pathsMemoryFile, true);
			out.write(userInFile);
			out.close();
			PrintWhiteSpace();
			System.out.println("Path saved to memory file. Please reload this program to access the new path.");
			System.exit(0);
		}

		// Look through all lines of memory and add them to an array list.
		while (memoryScan.hasNextLine()) {
			String currLine = memoryScan.nextLine();
			filePaths.add(currLine);
		}
		memoryScan.close();

		// Show available paths to user and ask them to choose one.
		do {
			PrintWhiteSpace();
			System.out.println("Which file would you like to use? Enter the integer corresponding to your choice.");
			for (int i = 0; i < filePaths.size(); i++) {
				System.out.println(i + 1 + ": " + filePaths.get(i));
			}
			System.out.println(
					"Alternatively, add a new file path by typing 'Add', or remove a file path by typing 'Remove'.");
			System.out.print("Your selection: ");
			userEntry = userIn.nextLine();
			if (userEntry.equals("Add") || userEntry.equals("add")) {// If user adds a memory path.
				PrintWhiteSpace();
				System.out.println("Please enter a file path and name to output the document.");
				System.out
						.print("(ex. C:\\Users\\Administrator\\Documents\\Official Documents\\Income and Expenses): ");
				String userInFile = userIn.nextLine();
				FileWriter out = new FileWriter(pathsMemoryFile, true);
				out.write("\n");
				out.write(userInFile + ".csv");
				out.close();

				// Advise the user that the path is added and
				// exit the program to update the list.
				PrintWhiteSpace();
				System.out.println("Path saved to memory file. Please reload this program to access the new path.");
				System.exit(0);
			}
			if (userEntry.equals("Remove")) {// If user removes a remembered path.
				PrintWhiteSpace();
				System.out.println("Please enter the name and file path to remove.");
				System.out.print("(ex. " + filePaths.get(0) + "): ");
				String fileToRemove = userIn.nextLine();
				Scanner removeScan = new Scanner(pathsMemoryFile);
				ArrayList<String> newList = new ArrayList<>();
				while (removeScan.hasNextLine()) {
					String currLine = removeScan.nextLine();
					if (!currLine.equals(fileToRemove)) {
						newList.add(currLine);
					}
				}
				FileWriter out = new FileWriter(pathsMemoryFile);
				for (int i = 0; i < newList.size(); i++) {
					out.write(newList.get(i));
				}
				out.close();
				removeScan.close();

				// Advise the user that the path is removed and
				// exit the program to update the list.
				PrintWhiteSpace();
				System.out.println(
						"Path removed from memory file. Keep in mind that this action did not delete the file.\nPlease reload this program to continue.");
				System.exit(0);
			}

			// Attempt to open the file selected by the user.
			invalidChoice = false;
			try {
				userChoice = Integer.parseInt(userEntry);
				filePath = filePaths.get(userChoice - 1);
			} catch (IndexOutOfBoundsException i) {// If the user enters an unlisted number.
				PrintWhiteSpace();
				System.out.println("Invalid choice. Please enter one of the available numbers or add a new path.");
				invalidChoice = true;
			} catch (NumberFormatException n) {// If the user enters something other than a number.
				PrintWhiteSpace();
				System.out.println("Invalid choice. Please enter a number or type 'Add'.");
				invalidChoice = true;
			}
		} while (invalidChoice);// end dowhile loop

		File file = new File(filePath);

		// Checks that the file exists and has been established.
		VerifyFile(file, filePath, date, userIn);

		// Return the desired file and begin the program.
		return file;
	}// end StartProgram

	// LOADS DATA FROM A NAVY FEDERAL .csv FILE.
	public static void LoadNFData(File file, Scanner userIn) throws IOException {
		// Variable declaration
		Scanner baseFile = new Scanner(file);
		Scanner newFile = null;
		String filePath = "";
		ArrayList<String> inputData = new ArrayList<>();
		ArrayList<String> existingData = new ArrayList<>();
		ArrayList<String> sortedDupeData = new ArrayList<>();
		ArrayList<String> sortedData = new ArrayList<>();
		Boolean invalidPath = false;

		// Ask user for a file path to the input file.
		do {
			invalidPath = false;
			PrintWhiteSpace();
			System.out.print(
					"Enter the file path of the input file here (ex. C:\\Users\\Administrator\\Downloads\\NFCU_Credit_Card): ");
			filePath = userIn.nextLine() + ".csv";
			File nfFile = new File(filePath);
			try {// Check to make sure the provided file path is valid.
				newFile = new Scanner(nfFile);
			} catch (FileNotFoundException f) {
				PrintWhiteSpace();
				System.out.println("The system cannot find the file specified. Please check the entered file path.");
				invalidPath = true;
			}
		} while (invalidPath);// Repeat previous steps

		// Load the new file's data and close the file.
		PrintWhiteSpace();
		System.out.println("Loading file data from");
		System.out.println(filePath);
		System.out.println("to the current register.");
		newFile.nextLine(); // Clear the first line (header) from the input file.
		while (newFile.hasNextLine()) {
			String line = newFile.nextLine();// Get the current line from the input file.

			// Check that the number of commas in this line is expected.
			int numCommas = line.length() - line.replace(",", "").length();
			if (numCommas != 12) { // If unexpected, remove additional commas within quotes.
				int firstQuotePos = 0;
				int secondQuotePos = 0;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == '"' && firstQuotePos == 0)
						firstQuotePos = i;
					if (firstQuotePos != 0) {
						if (line.charAt(i) == '"')
							secondQuotePos = i;
					}
				}
				String problemName = line.substring(firstQuotePos, secondQuotePos + 1);
				problemName = problemName.replaceAll("\\,", "");// Remove commas
				problemName = problemName.replaceAll("\"", "");// Remove quotations, as those seem to cause problems as
																// well.
				String remainder = line.substring(secondQuotePos + 1, line.length() - 1);
				line = line.substring(0, firstQuotePos) + problemName + remainder;// Proceed with analyzing the
																					// corrected string.
			}

			// Work the line into the correct format.
			Scanner curLine = new Scanner(line);
			curLine.useDelimiter(",");
			String entry = curLine.next();// Start entry by adding the date.
			Double amount = curLine.nextDouble();// Record amount of change.
			String changeType = curLine.next();// Debit or Credit.
			curLine.next();// Skip type.
			curLine.next();// Skip Type Group.
			curLine.next();// Skip Reference.
			curLine.next();// Skip Instructed Currency.
			curLine.next();// Skip Currency Exchange Rate.
			curLine.next();// Skip Instructed Amount.
			String specification = curLine.next();// Company/organization name.
			String description = curLine.next();// Type of charge.
			curLine.nextLine();// No further data required; skip Check Serial Number and Card Ending.
			curLine.close();
			Double spent = 0.0;
			Double earned = 0.0;
			if (changeType.equals("Debit"))
				spent = amount;
			else if (changeType.equals("Credit"))
				earned = amount;
			entry = entry + "," + description + "," + specification + "," + spent + "," + earned;
			inputData.add(entry);
		} // end while loop
		newFile.close();

		// Load the existing file's data and close the file.
		baseFile.nextLine(); // Clear the first line (header) from the base file.
		while (baseFile.hasNextLine()) {// Add every other line to an ArrayList.
			String curLine = baseFile.nextLine();
			if (curLine.isEmpty() == false) {
				existingData.add(curLine);
			}
		} // end while
		baseFile.close();

		// Add all data to a single array list.
		for (int i = 0; i < existingData.size(); i++) {
			sortedDupeData.add(existingData.get(i));
		}
		for (int i = 0; i < inputData.size(); i++) {
			sortedDupeData.add(inputData.get(i));
		}

		// Remove duplicates from the sorted list, if any.
		for (int i = 0; i < sortedDupeData.size(); i++) {
			String balEntry = sortedDupeData.get(i);
			Scanner curLine = new Scanner(balEntry);
			curLine.useDelimiter(",");
			String noBalEntry = curLine.next() + "," + curLine.next() + "," + curLine.next() + "," + curLine.next()
					+ "," + curLine.next();// Add every field but balance //FIXME: NoSuchElementException
			curLine.close();
			if (!sortedData.contains(noBalEntry)) {
				sortedData.add(noBalEntry);
			}
		}

		// Sort the ArrayList by date.
		SortByDate(sortedData);

		// Write the sorted data back to the base file; overwrite all data in the base
		// file.
		Double balance = 0.0;
		try {
			FileWriter out = new FileWriter(file);
			out.write("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");// Write the file header.
			// Decipher the new balance as new entries are added.
			for (int i = 0; i < sortedData.size(); i++) {
				String curLine = sortedData.get(i);
				Scanner lineScan = new Scanner(curLine);
				lineScan.useDelimiter(",");
				lineScan.next();// Skip date
				lineScan.next();// Skip description
				lineScan.next();// Skip specification
				Double spent = lineScan.nextDouble();
				Double earned = lineScan.nextDouble();
				balance -= spent;
				balance += earned;
				lineScan.close();
				out.write("\n");
				out.write(curLine + "," + balance);
			}
			out.close();
		} catch (FileNotFoundException e) {
			PrintWhiteSpace();
			System.out.println("Unable to write to the output file. Make sure it isn't open!");
			System.out.print("Press any key to return to main menu: ");
			userIn.nextLine();
			return;
		}

		// Take user back to the main menu.
		PrintWhiteSpace();
		System.out.print("Successfully loaded data into the selected register. Press any key to return to main menu: ");
		userIn.nextLine();
		return;
	}// end LoadNFData

	// CREATES A BACKUP OF THE FILE.
	public static void CreateBackup(File file, Scanner userIn) throws IOException {
		// Variable declaration
		Scanner in = new Scanner(file);
		String filePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4) + " Backup.csv";
		File backupFile;
		FileWriter out;

		// Ask the user if they would like to specify a new file path.
		PrintWhiteSpace();
		System.out.println("Creating a new backup file. It will be output as " + filePath);
		System.out.print("Would you like to change the file path? [Yes/No]: ");
		String answer = userIn.nextLine();
		if (answer.equals("Yes")) {
			System.out.print("Specify a new file path here (ex. C:\\Users\\Administrator\\Documents\\): ");
			filePath = userIn.nextLine();
			filePath = filePath + "Income and Expenses Backup.csv";
			System.out.println("Remember to change the default filepath in your program before you load it again!");
		}

		// Create a new file at a designated path
		PrintWhiteSpace();
		backupFile = new File(filePath);
		backupFile.createNewFile();

		// Copy one file onto the other
		out = new FileWriter(backupFile);
		while (in.hasNextLine()) {
			String temp = in.nextLine();
			out.write(temp);
			out.write("\n");
		}
		out.close();
		in.close();
		System.out.println("Backup successfully created at " + filePath);
	}// end CreateBackup

	// PRINTS WHITESPACE.
	public static void PrintWhiteSpace() {
		for (int i = 0; i < 3; i++) {
			System.out.println("");
		}
	}// end PrintWhiteSpace

	// ATTEMPTS TO LOCATE THE FILE AND VERIFIES THAT IT IS NOT EMPTY.
	public static void VerifyFile(File file, String filePath, String date, Scanner userIn) throws IOException {
		// Variable declaration
		Double initialBalance = 0.0;

		// If unable to find the file, create a new one.
		try (Scanner test = new Scanner(file)) {
		} catch (FileNotFoundException e) {
			PrintWhiteSpace();
			System.out.println("No file yet exists at " + filePath);
			System.out
					.print("Creating a new register file. Would you like to change the file path or name? [Yes/No]: ");
			String answer = userIn.nextLine();
			// Ask the user if they would like to specify a new file path and name.
			if (answer.equals("Yes")) {
				System.out.print(
						"Specify a new file path and file name here (ex. C:\\Users\\Administrator\\Documents\\Income and Expenses): ");
				filePath = userIn.nextLine();
				filePath = filePath + ".csv";
			}
			PrintWhiteSpace();
			file = new File(filePath);
			file.createNewFile();
			System.out.println("Created a new file at " + filePath);
			try (PrintWriter writer = new PrintWriter(file)) {
				writer.print("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");
				System.out.print(
						"Would you like to enter an initial balance? Otherwise, your balance will be 0.00$. [Yes/No]: ");
				answer = userIn.nextLine();
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
			} catch (IOException f) {
			}
		}

		// Check if the file is empty due to an error.
		Scanner test = new Scanner(file);
		if (test.hasNextLine() == false) {
			PrintWhiteSpace();
			System.out.println("The document was found but is empty. Filling it now.");
			String answer = "";
			try (PrintWriter writer = new PrintWriter(file)) {
				writer.print("DATE,DESCRIPTION,SPECIFICATION,$ SPENT,$ EARNED,BALANCE");
				System.out.print(
						"Would you like to enter an initial balance? Otherwise, your balance will be 0.00$. [Yes/No]: ");
				answer = userIn.nextLine();
				if (answer.equals("Yes")) {
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
		Formatter balFormat;
		Scanner lastLine;
		Scanner in;

		// Verify descriptions memory file exists, create one if not and tell the user.
		if (descsMemoryFile.exists() == false) {
			descsMemoryFile.createNewFile();
			PrintWhiteSpace();
			System.out.println(
					"Created a new description preference file (Descriptions Memory.txt) at the directory in which this program is stored.");
			System.out.println("This file will store already-used descriptions for future organizational assistance.");
		}

		// Enter main method loop.
		do {
			// Find the last entry of the register. Remove empty rows from the file, to
			// allow for easier access of the last valid row.
			in = new Scanner(file);
			while (in.hasNextLine()) {
				String curLine = in.nextLine();
				if (curLine.length() > 5) {// Only add non-empty rows. //FIXME: why 5?
					validRows.add(curLine);
				}
			} // end while loop

			lastLine = new Scanner(validRows.get(validRows.size() - 1));// Scan the last line.
			lastLine.useDelimiter(",");

			// Find the current balance of the account from the last line.
			for (int i = 0; i < 5; i++) {// Scroll to the balance column of the last entry.
				lastLine.next();
			} // end for loop
			try {// Ensure the last line is formatted properly; remove the ", " and then attempt
					// to parse it as a Double.
				curBalance = Double.parseDouble(lastLine.nextLine().substring(1));
			} catch (NumberFormatException n) {
				PrintWhiteSpace();
				System.out.println("Unable to properly read the final line of the document.");
				System.out.println("Please ensure the balance figure of the final line is formatted appropriately.");
				System.exit(0);
			}
			lastLine.close();
			in.close();
			PrintWhiteSpace();
			balFormat = new Formatter();
			System.out.println("You are now adding an entry to the register. The current balance of this account is $"
					+ balFormat.format("%.2f", curBalance) + ".");
			balFormat.close();

			// Gather user data; present list of existing general descriptions. //FIXME Data
			// not writing to file?
			System.out.print("Please enter a one-word description of the change.");
			Scanner descScanner = new Scanner(descsMemoryFile);
			if (descScanner.hasNextLine()) {// Print out saved descriptions
				System.out.println("Existing descriptions are: ");
				while (descScanner.hasNextLine()) {
					String curDescLine = descScanner.nextLine();
					loadedDescs.add(curDescLine);
					System.out.println(curDescLine);
				}
				descScanner.close();
			} else {
				System.out.println(
						"Common descriptions are: \nGasoline/Fuel\nGroceries\nRestaurants/Dining\nSalary\nTips\nGeneralMerchandise");
			}
			System.out.print("Your selection: ");
			description = userIn.next();
			userIn.nextLine();
			for (int i = 0; i < loadedDescs.size(); i++) {// If the user's description does not match an existing
															// description, add it to the file.
				if (description.equals(loadedDescs.get(i)) == false) {
					FileWriter out = new FileWriter(descsMemoryFile, true);
					out.write("\n");
					out.write(loadedDescs.get(i));
					out.close();
				}
			}

			// Gather data on specification (organization/individual name), if any.
			PrintWhiteSpace();
			System.out.print("Please enter a specification if applicable (ex. Burger King, Doordash, etc.): ");
			specification = userIn.nextLine();
			System.out.print("Enter how much was SPENT, 0 if none: ");
			spent = Double.parseDouble(userIn.nextLine());
			System.out.print("Enter how much was EARNED, 0 if none: ");
			earned = Double.parseDouble(userIn.nextLine());

			// Write data to the file.
			try {
				FileWriter writer = new FileWriter(file, true);
				if (spent > 0.0) {
					curBalance -= spent;
				}
				if (earned > 0.0) {
					curBalance += earned;
				}
				writer.write(
						date + "," + description + "," + specification + "," + spent + "," + earned + "," + curBalance);
				writer.write("\n");
				writer.close();
			} catch (FileNotFoundException d) {
				PrintWhiteSpace();
				System.out.println("Unable to write to file - make sure it isn't open!");
				System.exit(0);
			} catch (IOException i) {
				PrintWhiteSpace();
				System.out.println("Unable to write to file.");
				System.exit(0);
			}
			balFormat = new Formatter();

			PrintWhiteSpace();
			System.out.println(
					"Entry saved. The new balance of the account is: $" + balFormat.format("%.2f", curBalance) + ".");
			balFormat.close();
			System.out.print("Would you like to add another entry? [Yes/No]: ");
			String answer = userIn.nextLine();
			if (answer.equals("No")) {
				anotherEntry = false;
			}
		} while (anotherEntry);// end outer loop
	}// end AddEntry

	// SEARCHES THE FILE AND PRINTS INFORMATION REQUESTED BY THE USER.
	public static void LookupData(File file, Scanner userIn) throws FileNotFoundException {
		// Variable declaration
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

		// Ask user for a date range, then load array with data from that date range,
		// excluding the date value from each result.
		PrintWhiteSpace();
		System.out.print("Would you like to specify a date range? [Yes/No]: ");
		userInput = userIn.nextLine();
		if (userInput.equals("Yes")) {
			PrintWhiteSpace();
			System.out.print("Specify the year you would like to search (ex. 2024): ");
			userYear = userIn.nextLine().substring(2, 4);
			System.out.print(
					"Specify the month you would like to search as a two-digit number (ex. May = 05) \nEnter 'No' if you do not want to specify a month: ");
			userMonth = userIn.nextLine();
			if (!userMonth.equals("No")) {// if user specified a year and a month.
				System.out.println("Searching for entries within " + userMonth + "/" + userYear);
				while (in.hasNextLine()) {
					if (i != 0) {
						Scanner line = new Scanner(in.nextLine());
						line.useDelimiter(",");
						String date = line.next();
						if (date.isEmpty()) {
							break;
						}
						if (date.substring(6, 8).equals(userYear) && date.substring(0, 2).equals(userMonth)) {
							arr.add(line.next() + "," + line.next() + "," + line.next() + "," + line.next() + ","
									+ line.next());
						}
						line.close();
					} else {
						in.nextLine();
					}
					i++;
				} // end while
			} else {// if user did not specify a month.
				while (in.hasNextLine()) {
					if (i != 0) {
						Scanner line = new Scanner(in.nextLine());
						line.useDelimiter(",");
						String date = line.next();
						if (date.isEmpty()) {
							break;
						}
						if (date.substring(6, 8).equals(userYear)) {
							arr.add(line.next() + "," + line.next() + "," + line.next() + "," + line.next() + ","
									+ line.next());
						}
						line.close();
					} else {
						in.nextLine();
					}
					i++;
				} // end while
			}
		} else {// if user did not specify a year or a month.
			while (in.hasNextLine()) {
				if (i != 0) {
					Scanner line = new Scanner(in.nextLine());
					line.useDelimiter(",");
					String date = line.next();
					if (date.isEmpty()) {
						break;
					}
					arr.add(line.next() + "," + line.next() + "," + line.next() + "," + line.next() + ","
							+ line.next());
					line.close();
				} else {
					in.nextLine();
				}
				i++;
			} // end while
		}
		in.close();

		// Close search if no results are found.
		if (arr.size() == 0) {
			System.out.print("No results found. Enter any key to return the main menu: ");
			String failure = userIn.nextLine();
			if (!failure.isEmpty()) {
				System.out.println("Returing to main menu.");
			}
			descScanner.close();
			return;
		}
		// Resulting array list will contain strings of DESCRIPTION,SPECIFICATION,$
		// SPENT,$ EARNED,BALANCE

		// Ask the user for search criteria for that data set.
		PrintWhiteSpace();
		System.out.println("Data loaded. " + arr.size() + " results found.");
		System.out.println("What description would you like to search for?");
		if (descScanner.hasNextLine()) {// Print out saved descriptions
			System.out.println("Existing descriptions are: ");
			while (descScanner.hasNextLine()) {
				String curDescLine = descScanner.nextLine();
				loadedDescs.add(curDescLine);
				System.out.println(curDescLine);
			}
			descScanner.close();
		} else {
			System.out.println(
					"Common descriptions are: \nGasoline/Fuel\nGroceries\nRestaurants/Dining\nSalary\nTips\nGeneralMerchandise\nHomeImprovement");
		}
		System.out.print("Your selection: ");
		userDescChoice = userIn.nextLine();

		// Optional specification to add to search.
		PrintWhiteSpace();
		System.out.println(
				"Is there a specification you would like to add to your search? Enter an organization's or person's name.");
		System.out.print("Alternatively, enter No to proceed without a search specification: ");
		userSpecChoice = userIn.nextLine();
		if (userSpecChoice.equals("No")) {// if the user provided only a description.
			for (int j = 0; j < arr.size(); j++) {
				String[] line = arr.get(j).split(",");
				if (line[0].equals(userDescChoice)) {
					totalSpent += Double.parseDouble(line[2]);
					totalEarned += Double.parseDouble(line[3]);
				}
			} // end for loop

			// Print results of the search without specification.
			PrintWhiteSpace();
			Formatter spentFormat = new Formatter();
			System.out.println("Total spent on " + userDescChoice + ": $" + spentFormat.format("%.2f", totalSpent));
			spentFormat.close();
			Formatter earnedFormat = new Formatter();
			System.out.println(
					"Total earned through " + userDescChoice + ": $" + earnedFormat.format("%.2f", totalEarned));
			earnedFormat.close();
		} else {// if the user provided a description and a specification.
			for (int j = 0; j < arr.size(); j++) {
				String[] line = arr.get(j).split(",");
				if (line[0].equals(userDescChoice) && line[1].equals(userSpecChoice)) {
					totalSpent += Double.parseDouble(line[2]);
					totalEarned += Double.parseDouble(line[3]);
				}
			} // end for loop

			// Print results of search with specification.
			PrintWhiteSpace();
			Formatter spentFormat = new Formatter();
			System.out.println("Total spent on " + userDescChoice + " from " + userSpecChoice + ": $"
					+ spentFormat.format("%.2f", totalSpent));
			spentFormat.close();
			Formatter earnedFormat = new Formatter();
			System.out.println("Total earned through " + userDescChoice + " from " + userSpecChoice + ": $"
					+ earnedFormat.format("%.2f", totalEarned));
			earnedFormat.close();
		}

		// Allow user to view data
		System.out.print("Results printed above. Enter any key to return the main menu: ");
		String success = userIn.nextLine();
		if (!success.isEmpty()) {
			System.out.println("Returing to main menu.");
		}
		return;
	}// end LookupData
}// end register class