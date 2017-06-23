package SMSW;

import java.io.Serializable;

public class Column implements Serializable{
	
	String name;
	String type;
	boolean key;
	boolean indexed;
	boolean referenced;
	
	public Column() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
	}
	public boolean isIndexed() {
		return indexed;
	}
	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}
	public boolean isReferenced() {
		return referenced;
	}
	public void setReferenced(boolean referenced) {
		this.referenced = referenced;
	}
	
	
	
	
	


}
