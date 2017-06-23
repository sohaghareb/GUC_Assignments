package SMSW;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Page implements Serializable {
	
	String [][]content;
	String directory;
	int numberOfColumns;
	int index;
	int n;
	int pageNo;
	int recordNo;
	
	public Page(String directory, int numberOfColumns,int page) throws NumberFormatException, IOException {
		this.directory = directory;
		
		
		
		ReadFromProperties r
		= new ReadFromProperties(
				"config/DBApp.properties");
		 n = Integer.parseInt(r.ReadValueUsingKey("MaximumRowsCountinPage"));
		this.numberOfColumns = numberOfColumns;
		//soha +1 because we need khanaa 3shan nmark when we delete
		content = new String[n][numberOfColumns+3];
		pageNo=page;
		recordNo=0;
		index=0;
		
		for (int i = 0; i < content.length; i++) {
			content[i][0]="0";
			///the 3nd coloumn will be page no
			content[i][2]=String.valueOf(pageNo);
			
		}
		
		
		
	}
	
	public String[][] getContent() {
		return content;
	}
 
	public int getIndex() {
		return index;
	}
	public void delete (int record){
		if (record >index) return ;
		content[record][0]="0";
		
	}
	//soha added to get a record
	
	public String [] getRecord(int record){
		String line="";
		for (int i = 3; i < content[0].length; i++) {
			line+=content[record][i];
			if(i== content[0].length-1 ){
			line+="";	
			}
			else 
				line+=",";
			
			
		}
 String [] split=line.split(",");
 return split;
		
	}

	
	
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	
	public void add ( String line) throws IOException{
		
		content[index][0]="1";
		content[index][1]=String.valueOf(recordNo++);
		String [] split=line.split(",");
		
		for (int i = 3; i < content[index].length; i++) {
			content[index][i]=String.valueOf(split[i-3]);
			
		}
		++index;
		
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(p));
//        oos.writeObject(this);
//        oos.close();
		
		}
	

public boolean full(){
if (index<n)
	return false  ;
else 
	return true;
}

public String toString()
{
	String res = "";
	for (int i = 0; i < index; i++) {
		for (int j = 0; j < content[i].length; j++) {
			res+=content[i][j] + " ";
			
		}
		res += "\n";
	}
	return res;
}
}