package SMSW;

import java.io.FileWriter;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

/**
 * 09
 * 
 * @author ashraf 10
 * 
 *         11
 */


public class CsvFileWriter {
	

	// Delimiter used in CSV file

	private final String COMMA_DELIMITER = ",";

	private final String NEW_LINE_SEPARATOR = "\n";

	public void writeCsvFile(String fileName, String[][] towrite) {

	
		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter(fileName, true);
			
			for (int i = 0; i < towrite.length; i++) {
				for (int j = 0; j < towrite[i].length; j++) {
					fileWriter.append(towrite[i][j]);
					fileWriter.append(COMMA_DELIMITER);
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
			}


			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {

			System.out.println("Error in CsvFileWriter !!!");

			e.printStackTrace();

		} finally {

			try {

				fileWriter.flush();

				fileWriter.close();

			} catch (IOException e) {

				System.out
						.println("Error while flushing/closing fileWriter !!!");

				e.printStackTrace();

			}

		}

	}

//	public static void main(String[] args) {
//		
//
//	}

}
