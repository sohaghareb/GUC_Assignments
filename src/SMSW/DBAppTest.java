package SMSW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class DBAppTest {

	public static void testCreateTable() throws ClassNotFoundException,
			IOException, DBAppException, DBEngineException {
		DBApp d = new DBApp();
		d.init();
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		htblColNameType.put("Name", "String");
		htblColNameType.put("age", "int");
		htblColNameType.put("id", "int");
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		d.createTable("mmm", htblColNameType, htblColNameRefs, "id");
		d.saveAll();
	}

	public static void testInsert() throws ClassNotFoundException, IOException,
			DBAppException, DBEngineException {
		DBApp d = new DBApp();
		d.init();
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		htblColNameType.put("Name", "String");
		htblColNameType.put("age", "int");
		htblColNameType.put("id", "int");
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		d.createTable("mmm", htblColNameType, htblColNameRefs, "id");
		Hashtable<String, String> x = new Hashtable<String, String>();
		x.put("Name", "mariam");
		x.put("age", "21");
		x.put("id", "25");
		d.insertIntoTable("mmm", x);
		d.saveAll();
	}

	public static void testCreateIndex() throws ClassNotFoundException,
			IOException, DBEngineException, DBAppException {
		DBApp d = new DBApp();
		d.init();
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		htblColNameType.put("Name", "String");
		htblColNameType.put("age", "int");
		htblColNameType.put("id", "int");
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		d.createTable("mmm", htblColNameType, htblColNameRefs, "id");
		Hashtable<String, String> x = new Hashtable<String, String>();
		x.put("Name", "mariam");
		x.put("age", "21");
		x.put("id", "25");
		d.insertIntoTable("mmm", x);
		d.saveAll();
		ArrayList<String> cols = new ArrayList<String>();
		cols.add("Name");
		cols.add("age");
		d.createIndex("mmm", "Name");
		d.saveAll();
	}

	public static void testCreateMultiIndex() throws DBAppException,
			IOException, ClassNotFoundException, DBEngineException {
		DBApp d = new DBApp();
		d.init();
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		htblColNameType.put("Name", "String");
		htblColNameType.put("age", "int");
		htblColNameType.put("id", "int");
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		d.createTable("mmm", htblColNameType, htblColNameRefs, "id");
		Hashtable<String, String> x = new Hashtable<String, String>();
		x.put("Name", "mariam");
		x.put("age", "21");
		x.put("id", "25");
		d.insertIntoTable("mmm", x);
		d.saveAll();
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
		d.saveAll();
	}

	public static void testSelect() throws DBEngineException, IOException, DBAppException, ClassNotFoundException {
		DBApp d = new DBApp();
		d.init();
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		htblColNameType.put("Name", "String");
		htblColNameType.put("age", "int");
		htblColNameType.put("id", "int");
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		d.createTable("mmm", htblColNameType, htblColNameRefs, "id");
		Hashtable<String, String> x = new Hashtable<String, String>();
		x.put("Name", "mariam");
		x.put("age", "21");
		x.put("id", "25");
		d.insertIntoTable("mmm", x);
		d.saveAll();
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
		d.saveAll();
		d.selectFromTable("mmm", x, "AND");
		d.saveAll();
	}

	public static void testDelete() throws ClassNotFoundException, IOException, DBAppException, DBEngineException {
		DBApp d = new DBApp();
		d.init();
		Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
		htblColNameType.put("Name", "String");
		htblColNameType.put("age", "int");
		htblColNameType.put("id", "int");
		Hashtable<String, String> htblColNameRefs = new Hashtable<String, String>();
		d.createTable("mmm", htblColNameType, htblColNameRefs, "id");
		Hashtable<String, String> x = new Hashtable<String, String>();
		x.put("Name", "mariam");
		x.put("age", "21");
		x.put("id", "25");
		d.insertIntoTable("mmm", x);
		d.saveAll();
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
		d.saveAll();
		d.delete("mmm", x, "AND");
		d.saveAll();
	}

	public static void main(String[] args) {

	}

}
