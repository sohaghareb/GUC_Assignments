package SMSW;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadFromProperties {
	Properties defaultProps;
	FileInputStream in;
	String path;

	public ReadFromProperties(String path) {
		this.path = path;
		defaultProps = new Properties();
	}

	public void loadFile() throws IOException {
		defaultProps = new Properties();
		in = new FileInputStream(this.path);
		defaultProps.load(in);
		in.close();
	}

	public String ReadValueUsingKey(String key) throws IOException {
		if (in == null || defaultProps == null) {
			loadFile();
		}
		if (defaultProps.containsKey(key)) {
			return defaultProps.getProperty(key);
		}
		return null;
	}

//	 public static void main(String[] args) throws IOException {
//	 ReadFromProperties r = new ReadFromProperties(
//	 "config/DBApp.properties");
//	 System.out.println(r.ReadValueUsingKey("MaximumRowsCountinPage"));
//	
//	 }

}
