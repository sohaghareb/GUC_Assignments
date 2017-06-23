package SMSW;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class DBApp {

	Hashtable<String, Table> tables;

	public void init() throws ClassNotFoundException, IOException {

		// initialize tables
		// this.tables = new Hashtable<String, Table>();
		// ArrayList<String> allLinesOfMetadata = CsvFileReader
		// .readCsvFile("data/metadata.csv");
		// for (int i = 1; i < allLinesOfMetadata.size(); i++) {
		// String line = allLinesOfMetadata.get(i);
		// String[] splitted = line.split(",");
		// String tableName = splitted[0];
		// Table table = null;
		// if (tables.containsKey(tableName)) {
		// table = tables.get(tableName);
		// } else {
		// table = new Table();
		// }
		// table.setTableName(tableName);
		// ArrayList<String> colNamesOfTable = new ArrayList<String>();
		// colNamesOfTable.add(splitted[1]);
		// tables.put(tableName, table);
		// ArrayList<Column> tableColumns = new ArrayList<Column>();
		// table.columns = tableColumns;
		// Column temp = new Column();
		// temp.name = splitted[1];
		// temp.type = splitted[2];
		// temp.key = splitted[3].equals("True") ? true : false;
		// temp.indexed = splitted[4].equals("True") ? true : false;
		// temp.referenced = splitted[5].equals("True") ? true : false;
		// tableColumns.add(temp);
		// while (true) {
		// i++;
		// String newLine = allLinesOfMetadata.get(i);
		// String[] splitted2 = line.split(",");
		// String tableName2 = splitted[0];
		// if (!tableName.equals(tableName2)) {
		// i--;
		// break;
		// } else {
		// Column temp2 = new Column();
		// temp2.name = splitted2[1];
		// temp2.type = splitted2[2];
		// temp2.key = splitted2[3].equals("True") ? true : false;
		// temp2.indexed = splitted2[4].equals("True") ? true : false;
		// temp2.referenced = splitted2[5].equals("True") ? true
		// : false;
		// tableColumns.add(temp2);
		// }
		//
		// }
		// }

		this.tables = new Hashtable<String, Table>();
		File dir = new File("tables");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				// System.out.println(child.getName());
				FileInputStream x = new FileInputStream(child);

				ObjectInputStream ois = new ObjectInputStream(x);
				Table t = (Table) ois.readObject();
				ois.close();
				tables.put(t.tableName, t);
			}

		} else {
			System.out.println("no");
		}

		// LinkedList <Page> s=t.getPages();
		//
		// for (int i = 0; i < s.size(); i++) {
		//
		// System.out.println("my records are "+s.get(i).toString());
		// }
		//

	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException, IOException, ClassNotFoundException {

		String[][] lines = new String[htblColNameType.size()][6];
		Iterator<Entry<String, String>> it = htblColNameType.entrySet()
				.iterator();
		Entry<String, String> things;

		int i = 0;
		while (it.hasNext()) {
			String line = "";
			things = it.next();
			String trueOrFalse = isKey(things.getKey(), strKeyColName) ? "true"
					: "false";
			String isIndexed = isKey(things.getKey(), strKeyColName) ? "true"
					: "false";
			boolean isRef = htblColNameRefs.contains(things.getKey());
			String ref = isRef ? "true" : "false";
			lines[i][0] = strTableName;
			lines[i][1] = things.getKey();
			lines[i][2] = things.getValue();
			lines[i][3] = trueOrFalse;
			lines[i][4] = isIndexed;
			lines[i][5] = ref;

			i++;
		}

		CsvFileWriter write = new CsvFileWriter();
		write.writeCsvFile("data/metadata.csv", lines);

		// after writing into metadata
		File dir = new File("tables/" + strTableName + ".ser"); // create a file
																// of the table
																// in table
																// folder
		// dir.mkdirs();
		File dir2 = new File("tablePage/" + strTableName);
		dir2.mkdir();

		if (dir.createNewFile()) {
			System.out.println("create the file in createTable");

		}
		if (this.tables == null) {
			this.tables = new Hashtable<String, Table>();
		}
		ArrayList<String> columnNames = new ArrayList<String>();
		Enumeration<String> enumKey = htblColNameType.keys();
		while (enumKey.hasMoreElements()) {
			String key = enumKey.nextElement();
			columnNames.add(key);
		}
		Table newTable = new Table(strTableName, htblColNameType.size(),
				columnNames);
		// soha added
		this.tables.put(strTableName, newTable);
		 createIndex(strTableName, strKeyColName);

	}

	public boolean isKey(String s1, String s2) {
		return s1.equals(s2);
	}

	// /////////////////////////////////////////////////////////////////////

	public Iterator selectFromTable(String strTable,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws IOException, ClassNotFoundException {
		Table table = tables.get(strTable);
		return table.selectFromColumns3(htblColNameValue, strOperator);
	}

	public void createMultiDimIndex(String strTableName,
			ArrayList<String> htblColNames) throws DBAppException,
			NumberFormatException, IOException, ClassNotFoundException {
		Table table = tables.get(strTableName);
		MyArrayString col = new MyArrayString(htblColNames);
		ReadFromProperties rfp = new ReadFromProperties(
				"config/DBApp.properties");
		int maxPerBucket = Integer.parseInt(rfp
				.ReadValueUsingKey("MaxPerBucket"));
		KDTree kdTree = new KDTree(maxPerBucket);
		table.kdTrees.put(col, kdTree);
		kdTree.table = table;
		kdTree.columnNumberOfKey1 = table.columnNames.indexOf(htblColNames
				.get(0));
		kdTree.columnNumberOfKey2 = table.columnNames.indexOf(htblColNames
				.get(1));
		kdTree.key1Name = htblColNames.get(0);
		kdTree.key2Name = htblColNames.get(1);
		LinkedList<String> pages = table.pages;
		int n = table.columnNames.indexOf(htblColNames.get(0)) + 3;
		int m = table.columnNames.indexOf(htblColNames.get(1)) + 3;
		int count = 0;
		for (String page : pages) {

			FileInputStream x = new FileInputStream("tablePage/" + strTableName
					+ "/" + pages.indexOf(page) + ".ser");
			ObjectInputStream ois = new ObjectInputStream(x);
			Page p = (Page) ois.readObject();
			ois.close();

			String[][] pageContent = p.content;
			for (int i = 0; i < pageContent.length; i++) { // start from
															// index/row 0
				if (pageContent[i][0].equals("1")) {
					String toPut = strTableName + "," + String.valueOf(count)
							+ "," + String.valueOf(i);
					kdTree.put(pageContent[i][n], pageContent[i][m], toPut);
				}
			}
			count++;
		}
	}

	public void insertIntoTable(String strTableName,
			Hashtable<String, String> htblColNameValue) throws DBAppException,
			NumberFormatException, IOException, ClassNotFoundException {

		Table current = null;
		Enumeration<String> enumm = tables.keys();
		// /loop on the hash table to get all the values
		while (enumm.hasMoreElements()) {
			String key = enumm.nextElement();
			if (key.equals(strTableName)) {
				current = tables.get(key);
				break;
			}

		}
		if (current == null) {
			System.out.println("there is no table with that name ");
			return;
		}

		Page page = null;
		File x = null;
		String directory = null;

		if (current.getNoOfPages() == 0) {
			directory = String.valueOf(current.getNoOfPages());
			x = new File("tablePage/" + strTableName + "/" + directory + ".ser");
			page = new Page(directory, current.getNumberOfColumns(),
					current.getNoOfPages());
			current.setNoOfPages(current.getNoOfPages() + 1);
			current.getPages().add(directory);
		} else {
			directory = current.getLastUsedPage();// get the page directory
			File dir = new File("tablePage/" + strTableName + "/" + directory
					+ ".ser");
			x = dir;
			FileInputStream xx = new FileInputStream(x);

			ObjectInputStream ois = new ObjectInputStream(xx);
			page = (Page) ois.readObject();
			ois.close();

		}

		String line = "";
		Enumeration<String> enumKey = htblColNameValue.keys();
		// /loop on the hash table to get all the values
		while (enumKey.hasMoreElements()) {
			String key = enumKey.nextElement();
			String val = htblColNameValue.get(key);
			line += val + ",";
		}
		page.add(line);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(x));
		oos.writeObject(page);
		oos.close();
		// add the record to all my indices
		String pointer = strTableName + "," + String.valueOf(page.getPageNo())
				+ "," + String.valueOf(page.getIndex() - 1);
		Hashtable<String, LinearHash<String, String>> my_linear = current
				.getLinearHashes();
		if (my_linear == null)
			return;// if no indices are made return
		Enumeration<String> keys = my_linear.keys();
		while (keys.hasMoreElements()) {
			String col_name = keys.nextElement();
			String value_of_key = htblColNameValue.get(col_name);
			LinearHash now_linear = my_linear.get(col_name);
			now_linear.put(value_of_key, pointer);
		}

		Hashtable<MyArrayString, KDTree> tempKDTrees = this.tables
				.get(strTableName).kdTrees;
		Enumeration<MyArrayString> enumMyArrayString = tempKDTrees.keys();
		while (enumMyArrayString.hasMoreElements()) {
			MyArrayString tempArrayString = enumMyArrayString.nextElement();
			KDTree currentKDTree = tempKDTrees.get(tempArrayString);
			String value1 = htblColNameValue.get(tempArrayString.key.get(0));
			String value2 = htblColNameValue.get(tempArrayString.key.get(1));

			currentKDTree.put(value1, value2, pointer);

		}
	}

	public void createIndex(String tableName, String ColName)
			throws IOException, ClassNotFoundException {
		Table current = null;
		Enumeration<String> enumm = tables.keys();
		// /loop on the hash table to get all the values
		while (enumm.hasMoreElements()) {
			String key = enumm.nextElement();
			if (key.equals(tableName)) {
				current = tables.get(key);
				break;
			}

		}
		if (current == null) {
			System.out.println("the table doesnot exist");
			return;
		}

		LinearHash<String, String> st = new LinearHash<String, String>();
		LinkedList<String> currentpage = current.getPages();
		for (int i = 0; i < currentpage.size(); i++) {
			String directory = current.getPages().get(i);
			File dir = new File("tablePage/" + tableName + "/" + directory
					+ ".ser");
			FileInputStream xx = new FileInputStream(dir);
			ObjectInputStream ois = new ObjectInputStream(xx);
			Page page = (Page) ois.readObject();
			ois.close();
			int page_no = page.getPageNo();
			String[][] tmp = page.getContent();
			int col_no = current.getColumnNames().indexOf(ColName) + 3;
			for (int j = 0; j < page.getIndex(); j++) {
				if (tmp[j][0].equals("1")) {
					String value = tableName;
					String key = tmp[j][col_no];
					value += "," + page_no + "," + tmp[j][1];
					st.put(key, value);

				}
			}
		}

		ArrayList<String> tryy = st.keys();
		// /loop on the hash table to get all the values
		for (int i = 0; i < tryy.size(); i++) {
			String key = tryy.get(i);
			String v = st.get(key);
			System.out.println(key + "," + v);

		}

		current.getLinearHashes().put(ColName, st);
		// /System.out.println(st.toString());

	}

	public void delete(String tableName, Hashtable<String, String> x, String op)
			throws ClassNotFoundException, IOException {
		Table current = null;
		Enumeration<String> enumm = tables.keys();
		// /loop on the hash table to get all the values
		while (enumm.hasMoreElements()) {
			String key = enumm.nextElement();
			if (key.equals(tableName)) {
				current = tables.get(key);
				break;
			}
		}
		if (current == null) {
			System.out.println("the table you are deleting from doesnt exist");
			return;
		}

		Iterator tmp = selectFromTable(tableName, x, op);

		while (tmp.hasNext()) {
			String[] t1 = (String[]) tmp.next();
			int page_no = Integer.parseInt(t1[2]);
			int record_no = Integer.parseInt(t1[1]);
			String directory = current.getPages().get(page_no);
			File dir = new File("tablePage/" + tableName + "/" + directory
					+ ".ser");
			FileInputStream xx = new FileInputStream(dir);
			ObjectInputStream ois = new ObjectInputStream(xx);
			Page page = (Page) ois.readObject();
			ois.close();
			page.delete(record_no);

			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(dir));
			oos.writeObject(page);
			oos.close();

			Hashtable<String, LinearHash<String, String>> current_linear = current
					.getLinearHashes();
			if (current_linear != null) {
				Enumeration<String> keys = current_linear.keys();
				while (keys.hasMoreElements()) {
					String col_name = keys.nextElement();
					LinearHash now_linear = current_linear.get(col_name);
					int col_pos = current.getColumnNames().indexOf(col_name);
					String key = t1[col_pos + 3]; // got yhe key value from the
					now_linear.delete(key);

				}
			}
			String pointer = tableName + "," + String.valueOf(page_no) + ","
					+ String.valueOf(record_no);

			System.out
					.println("**** "
							+ String.valueOf(this.tables.get(current.tableName) == null));
			Enumeration<MyArrayString> enumDelKD = this.tables
					.get(current.tableName).kdTrees.keys();
			while (enumDelKD.hasMoreElements()) {
				MyArrayString tempArrString = enumDelKD.nextElement();
				KDTree tempKDTree = this.tables.get(current.tableName).kdTrees
						.get(tempArrString);
				int index1 = current.columnNames.indexOf(tempArrString.key
						.get(0)) + 3;
				int index2 = current.columnNames.indexOf(tempArrString.key
						.get(1)) + 3;
				String value1 = t1[index1];
				String value2 = t1[index2];
				tempKDTree.delete(value1, value2, pointer);
			}

		}

	}

	public void saveAll() throws DBEngineException, IOException {
		Enumeration<String> enumKey = tables.keys();
		int i = 0;
		while (enumKey.hasMoreElements()) {
			System.out.println("iam in the while");
			String key = enumKey.nextElement();// has the table name
			System.out.println(key);

			// /File dir = new File("tables/" +key);
			File dir = new File("tables");
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
				System.out.println("ana shayf el directory");
				for (File child : directoryListing) {
					System.out.println("looping on the files");
					System.out.println(child.getName());
					if (child.getName().equals(key + ".ser")) {
						ObjectOutputStream oos = new ObjectOutputStream(
								new FileOutputStream(child));

						oos.writeObject((Table) tables.get(key));
						oos.flush();
						System.out.println("found the file looking for");

						++i;

						oos.close();

						System.out.println("katbt");

					}
				}
			} else {
				System.out.println("I can't store!!!");
			}
		}
	}

	public static void main(String[] args) throws DBAppException, IOException,
			NumberFormatException, ClassNotFoundException, DBEngineException {
		DBApp d = new DBApp();
		d.init();
		// Hashtable<String, String> htblColNameType = new Hashtable<String,
		// String>();
		// htblColNameType.put("Name", "String");
		// htblColNameType.put("age", "int");
		// htblColNameType.put("id", "int");
		// Hashtable<String, String> htblColNameRefs = new Hashtable<String,
		// String>();
		// d.createTable("mmm", htblColNameType, htblColNameRefs, "id");
		//
		Hashtable<String, String> x = new Hashtable<String, String>();
		x.put("Name", "mariam");
		x.put("age", "21");
		x.put("id", "25");
		d.insertIntoTable("mmm", x);
		d.saveAll();

		ArrayList<String> arList = new ArrayList<String>();
		arList.add("Name");
		arList.add("age");
		MyArrayString m = new MyArrayString(arList);

		 ArrayList<String> cols = new ArrayList<String>();
		 cols.add("Name");
		 cols.add("age");
		 d.createMultiDimIndex("mmm", cols);
		 System.out.println(d.tables.get("mmm").tableName);

		Enumeration<MyArrayString> e = d.tables.get("mmm").kdTrees.keys();
		while (e.hasMoreElements()) {
			MyArrayString temp = e.nextElement();
			System.out.println(temp.key.get(0));
			System.out.println(temp.key.get(1));
			KDTree k = d.tables.get("mmm").kdTrees.get(temp);
			System.out.println(k.get("soha", "20").get(0));
			System.out.println(k.get("soha", "20").size());
			// k.delete("soha", "20", "mmm,0,0");
			// System.out.println(k.get("soha", "20").size());
		}
		// ArrayList<String> koko =
		// d.tables.get("mmm").kdTrees.get(arList).get("soha", "20");
		// for (String string : koko) {
		// System.out.println(string);
		// }
		// //
		Hashtable<String, String> xx = new Hashtable<String, String>();
		xx.put("Name", "soha");
		xx.put("age", "20");
		xx.put("id", "1");

		File x2 = new File("tablePage/mmm/0.ser");

		FileInputStream x3 = new FileInputStream(x2);
		ObjectInputStream ois = new ObjectInputStream(x3);
		Page p = (Page) ois.readObject();
		ois.close();

		System.out.println(p.toString());

		// //
		// // d.insertIntoTable("Employee", xx);
		// d.saveAll();

		// xx.put("Name", "Mariam");
		// xx.put("age", "40");
		// xx.put("id", "7");
		//
		// d.insertIntoTable("Employee", xx);
		// // System.out.println( insert.toString());

		// /System.out.println("after delletion");
		d.delete("mmm", x, "AND");

		FileInputStream x4 = new FileInputStream(x2);
		ObjectInputStream ois2 = new ObjectInputStream(x4);
		Page p2 = (Page) ois2.readObject();
		ois2.close();

		System.out.println(p2.toString());

		// Iterator it = d.selectFromTable("Employee", x, "AND");
		// while(it.hasNext()) {
		// String[] rec = (String[]) it.next();
		// System.out.println(rec[3]);
		// }

		// KDTree kd = new KDTree(3);
		// kd.table = d.tables.get("Employee");
		// kd.columnNumberOfKey1 = 1;
		// kd.columnNumberOfKey2 = 2;
		// kd.put("soha", "20", "Employee,0,0");
		// kd.put("Mariam", "40", "Employee,0,1");
		// kd.put("Mariam", "40", "Employee,0,2");
		// ArrayList roro = kd.get("Mariam", "40");
		// System.out.println(roro.get(1));

		// System.out.println( insert.toString());

		// Hashtable<String, String> xxx = new Hashtable<String, String>();
		// xxx.put("Name", "Mariam");
		// xxx.put("age", "40");
		// xxx.put("id", "5");
		// d.insertIntoTable("Employee", xxx);

		// / d.createIndex("Employee","id");

	}

}
