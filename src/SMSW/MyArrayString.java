package SMSW;

import java.io.Serializable;
import java.util.ArrayList;

public class MyArrayString  implements Serializable{
	
	ArrayList<String> key;
	
	public MyArrayString(ArrayList<String> key) {
		this.key = key;
	}
	
	public boolean equals(MyArrayString newArr) {
		ArrayList<String> newKey = newArr.key;
		for (int i = 0; i < newKey.size(); i++) {
			if (!key.contains(newKey.get(i))) {
				return false;
			}
		}
		
		for (int i = 0; i < key.size(); i++) {
			if (!newKey.contains(key.get(i))) {
				return false;
			}
		}
		
		return true;
	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		ArrayList<String> key = new ArrayList<String>();
//		key.add("lolo");
//		key.add("fofo");
//		MyArrayString koko = new MyArrayString(key);
//		ArrayList<String> key2 = new ArrayList<String>();
//		key2.add("olo");
//		key2.add("fofo");
//		System.out.println(koko.equals(key2));
//
//	}

}
