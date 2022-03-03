package com.ekielzan.JDBCChecker;
/*
 *
 *
 * */
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigFileGenerator {
	Properties properties = new Properties();
	String fileName = "";
	/**
	* Constructor with 2 Params
	*
	* @param fileName
	*          a destination file name
	* @param prop
	*          a Properties object to save
	*/
	ConfigFileGenerator(String fileNameIn, Properties prop) {
		this.fileName = fileNameIn;
		this.properties = prop;
		ConfigFileWriter();
	}
	/**
	* Constructor with 1 Params for dummy config file
	*
	* @param fileNamel
	*          a destination file name
	*/
	ConfigFileGenerator(String fileNameIn) {
		this.fileName = fileNameIn;
		properties.setProperty("db.oracle.jdbc.url","jdbc:oracle:thin:@${HOSTNAME}:1521:${ORACLE_SID}");
		properties.setProperty("db.oracle.user","");
		properties.setProperty("db.oracle.pass","");
		properties.setProperty("db.query","select 1 from dual");
		ConfigFileWriter();
	}
	/**
	* Write configuration file
	*/
	private void ConfigFileWriter() {
		try {
			if(!this.fileName.endsWith(".config")) {
				this.fileName = this.fileName + ".config";
			}
			OutputStream  fo = new FileOutputStream(this.fileName);
			properties.store(fo, null);
			fo.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("File Not Writable!");
			System.exit(11);
		}
	}
}
