package SMSW;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;

public class Table implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int noOfPages;
	String tableName;
	
     LinkedList<String> pages;
    Page lastUsedPage; // not important
	int numberOfColumns;
	ArrayList<String> columnNames;
	Hashtable<MyArrayString, KDTree> kdTrees;
	ArrayList<Column> columns;
	Hashtable<String, LinearHash<String, String>> linearHashes;
	

	public ListIterator selectFromColumns3(
			Hashtable<String, String> colNameValue, String strOperation)
			throws IOException, ClassNotFoundException {
		if (strOperation.equals("OR")) {
			ArrayList<String> references = new ArrayList<String>();
			ArrayList<String[]> iteratorsOr = new ArrayList<String[]>();
			Enumeration<String> enumKey = colNameValue.keys();
			while (enumKey.hasMoreElements()) {
				String key = enumKey.nextElement();
				String val = colNameValue.get(key);
				if (this.linearHashes.containsKey(key)) {
					String valueFromLinearHash = linearHashes.get(key).get(val);
					if (valueFromLinearHash != null) {
						if (!references.contains(valueFromLinearHash)) {
							references.add(valueFromLinearHash);
						}
					}
					colNameValue.remove(key);
				}
			}

			iteratorsOr = selectLinearlyOr(colNameValue);
			for (int i = 0; i < references.size(); i++) {
				String reference = references.get(i);
				String[] splitted1 = reference.split(",");
				int pageNumber = Integer.parseInt(splitted1[1]);
				int recordNumber = Integer.parseInt(splitted1[2]);
				

				FileInputStream x = new FileInputStream("tablePage/" + tableName + "/" + pageNumber + ".ser");
				ObjectInputStream ois = new ObjectInputStream(x);
				Page p = (Page) ois.readObject();
				ois.close();
				
				String[] record = p.content[recordNumber];
				if (!iteratorsOr.contains(record)) {
					iteratorsOr.add(record);
				}
			}
			return iteratorsOr.listIterator();
		} else {
			Enumeration<String> enumKey = colNameValue.keys();
			boolean hasHashIndex = false;
			String columnHashIndex = "";
			while (enumKey.hasMoreElements()) {
				String name = enumKey.nextElement();
				if (this.linearHashes.containsKey(name)) {
					hasHashIndex = true;
					columnHashIndex = name;
					break;
				}
			}

			if (hasHashIndex) {
				LinearHash l = this.linearHashes.get(columnHashIndex);
				String val = (String) l.get(colNameValue.get(columnHashIndex));
				ArrayList<String[]> result = new ArrayList<String[]>();
				if (val != null) {
					String[] splitted = val.split(",");
					int pageNumber = Integer.parseInt(splitted[1]);
					int recordNumber = Integer.parseInt(splitted[2]);
					

					FileInputStream x = new FileInputStream("tablePage/" + tableName + "/" + pageNumber + ".ser");
					ObjectInputStream ois = new ObjectInputStream(x);
					Page p2 = (Page) ois.readObject();
					ois.close();
					
					String[] record = p2.content[recordNumber];
					Enumeration<String> enumKey2 = colNameValue.keys();
					boolean toBeIncluded = true;
					while (enumKey2.hasMoreElements()) {
						String colName = enumKey2.nextElement();
						String colValue = colNameValue.get(colName);
						int tempIndex = this.columnNames.indexOf(colName);
						if (record[tempIndex + 3].compareTo(colValue) != 0) {
							toBeIncluded = false;
							break;
						}
					}
					if (toBeIncluded) {
						result.add(record);
					}
				}
				return result.listIterator();

			} else {
				boolean hasKDTree = false;
				ArrayList<String> columnNamesRequired = new ArrayList<String>();
				Enumeration<String> enumKDTree = colNameValue.keys();
				while (enumKDTree.hasMoreElements()) {
					String nameOfColumn = enumKDTree.nextElement();
					columnNamesRequired.add(nameOfColumn);
				}
				boolean goOut = false;
				MyArrayString columnsKD = null;
				KDTree kd = null;
				for (String string : columnNamesRequired) {
					if (goOut) {
						break;
					}
					for (String string2 : columnNamesRequired) {
						ArrayList<String> testArrayList = new ArrayList<String>();
						testArrayList.add(string);
						testArrayList.add(string2);
						columnsKD = new MyArrayString(testArrayList);
						if (this.kdTrees.containsKey(columnsKD)) {
							hasKDTree = true;
							kd = kdTrees.get(columnsKD);
							goOut = true;
							break;
						}
					}
				}
				//
				if (hasKDTree) {
					ArrayList<String> colNames = columnsKD.key;
					String column1 = colNames.get(0);
					String column2 = colNames.get(1);
					String value1 = colNameValue.get(column1);
					String value2 = colNameValue.get(column2);
					ArrayList<String> references = kd.get(value1, value2);
					ArrayList<String[]> results = new ArrayList<String[]>();
					for (String reference : references) {
						String splitted[] = reference.split(",");
						int pageNumber = Integer.parseInt(splitted[1]);
						int recordNumber = Integer.parseInt(splitted[2]);
						

						FileInputStream x = new FileInputStream("tablePage/" + tableName + "/" + pageNumber + ".ser");
						ObjectInputStream ois = new ObjectInputStream(x);
						Page p3 = (Page) ois.readObject();
						ois.close();
						
						String record[] = p3.content[recordNumber];
						Enumeration<String> enum5 = colNameValue.keys();
						boolean toInclude = true;
						while (enum5.hasMoreElements()) {
							String columnNameInKD = enum5.nextElement();
							String v = colNameValue.get(columnNameInKD);
							int index = this.columnNames
									.indexOf(columnNameInKD) + 3;
							if (!record[index].equals(v)) {
								toInclude = false;
								break;
							}
						}
						if (toInclude) {
							results.add(record);
						}
					}
					return results.listIterator();
				} else {
					return selectLinearlyAnd(colNameValue);
				}
			}

		}
	}
	
	public ListIterator selectLinearlyAnd(Hashtable<String, String> colNameVal)
			throws IOException, ClassNotFoundException {
		ArrayList result = new ArrayList();
		Enumeration<String> enumKey = colNameVal.keys();
		ArrayList<Integer> number = new ArrayList<Integer>();
		while (enumKey.hasMoreElements()) {
			String key = enumKey.nextElement();
			String val = colNameVal.get(key);

			number.add(columnNames.indexOf(key));

		}

		for (String page : pages) {
			if (page != null) {
				

				FileInputStream x = new FileInputStream("tablePage/" + tableName + "/" + page + ".ser");
				ObjectInputStream ois = new ObjectInputStream(x);
				Page p = (Page) ois.readObject();
				ois.close();
				
				String[][] content = p.content;

				for (int i = 0; i < content.length; i++) {

					String[] record = content[i];

					boolean ok = true;

					Enumeration<String> enumKey2 = colNameVal.keys();
					while (enumKey2.hasMoreElements()) {
						String key = enumKey2.nextElement();
						String val = colNameVal.get(key);
						int n = columnNames.indexOf(key) + 3;

						if (content[i][n] == null || !content[i][n].equals(val)
								|| Integer.parseInt(content[i][0]) != 1) {
							ok = false;
							break;
						}
					}
					if (ok) {
						result.add(record);
					}

				}
			}
		}

		return result.listIterator();
	}
	
	public ArrayList selectLinearlyOr(Hashtable<String, String> colNameVal)
			throws IOException, ClassNotFoundException {
		ArrayList result = new ArrayList();
		Enumeration<String> enumKey = colNameVal.keys();
		ArrayList<Integer> number = new ArrayList<Integer>();
		while (enumKey.hasMoreElements()) {
			String key = enumKey.nextElement();
			String val = colNameVal.get(key);

			number.add(columnNames.indexOf(key));

		}

		for (String page : pages) {
			if (page != null) {
				

				FileInputStream x = new FileInputStream("tablePage/" + tableName + "/" + page + ".ser");
				ObjectInputStream ois = new ObjectInputStream(x);
				Page p = (Page) ois.readObject();
				ois.close();
				
				String[][] content = p.content;

				for (int i = 0; i < content.length; i++) {

					String[] record = content[i];

					boolean ok = false;

					Enumeration<String> enumKey2 = colNameVal.keys();
					while (enumKey2.hasMoreElements()) {
						String key = enumKey2.nextElement();
						String val = colNameVal.get(key);
						int n = columnNames.indexOf(key) + 3;

						if (content[i][n] != null && content[i][n].equals(val)
								&& Integer.parseInt(content[i][0]) == 1) {
							ok = true;
							break;
						}
					}
					if (ok) {
						result.add(record);
						// System.out.println(record[3]);
					}

				}
			}
		}

		return result;
	}

	public LinkedList<String> getPages() {
		return pages;
	}

	public void setPages(LinkedList<String> pages) {
		this.pages = pages;
	}

	public String getLastUsedPage() {
		if (pages.size() == 0) {
			return null;
		}
		return pages.getLast();
	}

	public void setLastUsedPage(Page lastUsedPage) {
		this.lastUsedPage = lastUsedPage;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(ArrayList<String> columnNames) {
		this.columnNames = columnNames;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}

	public Hashtable<String, LinearHash<String, String>> getLinearHashes() {
		return linearHashes;
	}

	public void setLinearHashes(
			Hashtable<String, LinearHash<String, String>> linearHashes) {
		this.linearHashes = linearHashes;
	}


	public Table(String tableName, int numberOfColumns,
			ArrayList<String> columnNames) {
		this.tableName = tableName;
		this.pages = new LinkedList<String>();
		this.numberOfColumns = numberOfColumns;
		this.columnNames = columnNames;
		this.kdTrees = new Hashtable<MyArrayString, KDTree>();
		this.columns = new ArrayList<Column>();
		this.linearHashes = new Hashtable<String, LinearHash<String, String>>();
		noOfPages = 0;
	}

	public Table() {
		this.columnNames = new ArrayList<String>();
		this.pages = new LinkedList<String>();
		this.kdTrees = new Hashtable<MyArrayString, KDTree>();
		this.columns = new ArrayList<Column>();
		this.linearHashes = new Hashtable<String, LinearHash<String, String>>();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Hashtable<MyArrayString, KDTree> getKdTrees() {
		return kdTrees;
	}

	public void setKdTrees(Hashtable<MyArrayString, KDTree> kdTrees) {
		this.kdTrees = kdTrees;
	}

	public int getNoOfPages() {
		return noOfPages;
	}

	public void setNoOfPages(int noOfPages) {
		this.noOfPages = noOfPages;
	}

	// public ListIterator selectFromColumns(
	// Hashtable<String, String> colNameValue, String strOperation)
	// throws IOException, ClassNotFoundException {
	// // firstly: check on 2 columns that have kd tree
	// // secondly: check on columns that have linear hash
	// // thirdly: linear searching
	//
	// ArrayList<ListIterator> iterators = new ArrayList<ListIterator>();

	// if (strOperation.equals("AND")) {
	// // iterate over the hashtable to get those with kd tree
	// Enumeration<String> enumKey = colNameValue.keys();
	// ArrayList<String> columnNamesOfHashtable = new ArrayList<String>();
	// while (enumKey.hasMoreElements()) {
	// String name = enumKey.nextElement();
	// // String value = colNameValue.get(name);
	// columnNamesOfHashtable.add(name);
	// }
	// int n = columnNamesOfHashtable.size();
	// for (int j = 0; j < n; j++) {
	// String name = columnNamesOfHashtable.get(j);
	// for (int i = 0; i < columnNamesOfHashtable.size(); i++) {
	// String otherName = columnNamesOfHashtable.get(i);
	// if (!otherName.equals(name)) {
	// ArrayList<String> tempArrayList = new ArrayList<String>();
	// tempArrayList.add(name);
	// tempArrayList.add(otherName);
	// MyArrayString tempArrayString = new MyArrayString(
	// tempArrayList);
	// if (kdTrees.containsKey(tempArrayString)) {
	// iterators.add(selectFromKd(name, otherName,
	// colNameValue.get(name),
	// colNameValue.get(otherName)));
	// columnNamesOfHashtable.remove(j);
	// columnNamesOfHashtable.remove(i);
	// colNameValue.remove(name);
	// colNameValue.remove(otherName);
	// }
	// }
	// }
	// }
	//
	// // ////////////////////////////////////////////////////////////////
	// }
	// // search for those with linearHash
	//
	// Enumeration<String> enumKey2 = colNameValue.keys();
	// while (enumKey2.hasMoreElements()) {
	// String key = enumKey2.nextElement();
	// String val = colNameValue.get(key);
	//
	// }

	// ////////////////////////////////////////////////////////////////

	// the rest with linear searching

	// Enumeration<String> enumKey3 = colNameValue.keys();
	// while (enumKey3.hasMoreElements()) {
	// String key = enumKey3.nextElement();
	// String val = colNameValue.get(key);
	// iterators.add(selectLinearly(key, val));
	// }
	//
	// if (strOperation.equals("AND")) {
	// iterators.add(selectLinearlyAnd(colNameValue));
	// // while(iterators.get(0).hasNext()) {
	// // String[] rec = (String[]) iterators.get(0).next();
	// // System.out.println(rec[4]);
	// // }
	// return iterators.get(0);
	// }
	// else {
	// iterators.add(selectLinearlyOr(colNameValue));
	//
	// return iterators.get(0);
	// }

	// ListIterator iteratorSoFar = null;
	//
	// for (ListIterator iterator : iterators) {
	// if (strOperation.equals("AND")) {
	// iteratorSoFar = compareIteratorsForAnd(iteratorSoFar, iterator);
	// } else {
	// iteratorSoFar = compareIteratorsForOr(iteratorSoFar, iterator);
	// }
	// }
	//
	// return iteratorSoFar;
	// }

	public ListIterator compareIteratorsForAnd(ListIterator it1,
			ListIterator it2) {
		if (it1 == null || it2 == null) {
			return null;
		} else {
			ArrayList copy_of_it1 = new ArrayList();
			ArrayList copy_of_it2 = new ArrayList();
			while (it1.hasNext()) {
				copy_of_it1.add(it1.next());
			}
			while (it2.hasNext()) {
				copy_of_it2.add(it2.next());
			}
			int n = copy_of_it1.size();
			ArrayList toRemove = new ArrayList();
			for (int i = 0; i < n; i++) {
				Object object = copy_of_it1.get(i);
				if (!copy_of_it2.contains(object)) {
					toRemove.add(object);
				}
			}
			for (int i = 0; i < toRemove.size(); i++) {
				copy_of_it1.remove(toRemove.get(i));
			}
			ListIterator itTotal = copy_of_it1.listIterator();
			return itTotal;
		}
	}

	public ListIterator compareIteratorsForOr(ListIterator it1, ListIterator it2) {
		if (it1 == null) {
			if (it2 == null) {
				return null;
			} else {
				return it2;
			}
		} else {
			if (it2 == null) {
				return it1;
			} else {
				ArrayList copy_of_it1 = new ArrayList();
				ArrayList copy_of_it2 = new ArrayList();
				while (it1.hasNext()) {
					copy_of_it1.add(it1.next());
				}
				while (it2.hasNext()) {
					copy_of_it2.add(it2.next());
				}
				int n = copy_of_it1.size();
				for (int i = 0; i < n; i++) {
					Object object = copy_of_it1.get(i);
					if (!copy_of_it2.contains(object)) {
						copy_of_it2.add(object);
					}
				}
				ListIterator itTotal = copy_of_it2.listIterator();
				return itTotal;
			}
		}

	}

	public ListIterator selectFromLinearHash(String columnName, String value) {
		ArrayList<String> result;
		return null;
	}

	// start of change
	// public ListIterator selectLinearly(String columnName, String condition)
	// throws IOException, ClassNotFoundException {
	// ArrayList result = new ArrayList();
	// for (Page page : pages) {
	//
	// // ObjectInputStream ois = new ObjectInputStream(new
	// // FileInputStream(
	// // new File(page.directory)));
	// // Page p = (Page) ois.readObject();
	// // ois.close();
	//
	// if (page != null) {
	// String[][] content = page.content;
	// // assumme the needed column is in index 2
	// int n = columnNames.indexOf(columnName) + 3;
	//
	// for (int i = 0; i < content.length; i++) {
	// if (content[i][n] != null
	// && content[i][n].equals(condition)
	// && Integer.parseInt(content[i][0]) == 1) {
	// String[] record = content[i];
	// result.add(record);
	// }
	// }
	// }
	// }

	// return result.listIterator();
	// }

	// public ListIterator selectLinearlyAnd(Hashtable<String, String>
	// colNameVal)
	// throws IOException, ClassNotFoundException {
	// ArrayList result = new ArrayList();
	// Enumeration<String> enumKey = colNameVal.keys();
	// ArrayList<Integer> number = new ArrayList<Integer>();
	// while (enumKey.hasMoreElements()) {
	// String key = enumKey.nextElement();
	// String val = colNameVal.get(key);
	//
	// //number.add(columnNames.indexOf(key));
	//
	// }
	//
	// for (Page page : pages) {
	// if (page != null) {
	// String[][] content = page.content;
	//
	// for (int i = 0; i < content.length; i++) {
	//
	// String[] record = content[i];
	//
	// boolean ok = true;
	//
	// Enumeration<String> enumKey2 = colNameVal.keys();
	// while (enumKey2.hasMoreElements()) {
	// String key = enumKey2.nextElement();
	// String val = colNameVal.get(key);
	// int n = columnNames.indexOf(key) + 3;
	//
	// if (content[i][n] == null || !content[i][n].equals(val)
	// || Integer.parseInt(content[i][0]) != 1) {
	// ok = false;
	// break;
	// }
	// }
	// if (ok) {
	// result.add(record);
	// }
	//
	// }
	// }
	// }
	//
	// return result.listIterator();
	// }

	// public ListIterator selectLinearlyOr(Hashtable<String, String>
	// colNameVal)
	// throws IOException, ClassNotFoundException {
	// ArrayList result = new ArrayList();
	// Enumeration<String> enumKey = colNameVal.keys();
	// ArrayList<Integer> number = new ArrayList<Integer>();
	// while (enumKey.hasMoreElements()) {
	// String key = enumKey.nextElement();
	// String val = colNameVal.get(key);
	//
	// number.add(columnNames.indexOf(key));
	//
	// }
	//
	// for (Page page : pages) {
	// if (page != null) {
	// String[][] content = page.content;
	//
	// for (int i = 0; i < content.length; i++) {
	//
	// String[] record = content[i];
	//
	// boolean ok = false;
	//
	// Enumeration<String> enumKey2 = colNameVal.keys();
	// while (enumKey2.hasMoreElements()) {
	// String key = enumKey2.nextElement();
	// String val = colNameVal.get(key);
	// int n = columnNames.indexOf(key) + 3;
	//
	// if (content[i][n] != null && content[i][n].equals(val)
	// && Integer.parseInt(content[i][0]) == 1) {
	// ok = true;
	// break;
	// }
	// }
	// if (ok) {
	// result.add(record);
	// System.out.println("rec = " + record[3]);
	// //System.out.println(record[3]);
	// }
	//
	// }
	// }
	// }
	//
	// return result.listIterator();
	// }
	//
	//
	// end of change
}