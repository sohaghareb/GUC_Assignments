package SMSW;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvFileReader {
	// private static final String COMMA_DELIMITER = ",";

	public static ArrayList<String> readCsvFile(String fileName) {

		BufferedReader fileReader = null;

		ArrayList<String> lines = new ArrayList<String>();

		try {

			// Create a new list of student to be filled by CSV file data

			String line = "";

			// Create the file reader

			fileReader = new BufferedReader(new FileReader(fileName));

			// Read the CSV file header to skip it

			fileReader.readLine();

			// Read the file line by line starting from the second line

			while ((line = fileReader.readLine()) != null) {

				// System.out.println(line);
				lines.add(line);
			}

		}

		catch (Exception e) {

			System.out.println("Error in CsvFileReader !!!");

			e.printStackTrace();

		} finally {

			try {

				fileReader.close();

			} catch (IOException e) {

				System.out.println("Error while closing fileReader !!!");

				e.printStackTrace();

			}

		}

		return lines;

	}

	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// // this is to try to read
	// ArrayList<String> lines = readCsvFile("data/metadata.csv");
	// String line = lines.get(2);
	// String[] columns= line.split(",");
	// for (String string : columns) {
	// System.out.println(string);
	// }
	//
	// }

}
