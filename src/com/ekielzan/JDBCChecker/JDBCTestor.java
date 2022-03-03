package com.ekielzan.JDBCChecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

public class JDBCTestor {
	private String errMsg="Execution OK";
	private String hasGUI;
	private String fullStackTrace;
	private String toConsole;
	String newline = System.getProperty("line.separator");
	String outputFile = null;
	String queryOut="";
	String verboseDft = null;
	String versionDft = null;
	Vector<String> columnNames = new Vector<String>();
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	private String stackTrace="";
	/*
	* Default Constructor
	*
	* @param prop
	*          a Properties Object containing variables : oracle.jdbc.url, oracle.user, oracle.pass, oracle.driver
	*/
	public JDBCTestor(Properties prop){
		versionDft = prop.getProperty("apps.version");
		verboseDft = prop.getProperty("apps.verbose");
		outputFile = prop.getProperty("apps.outputFile");
		hasGUI = prop.getProperty("apps.gui");
		fullStackTrace = prop.getProperty("apps.stack.full");
		toConsole = prop.getProperty("apps.stack.toConsole");
		String url = prop.getProperty("db.oracle.jdbc.url");
		String username = prop.getProperty("db.oracle.user");
		String password = prop.getProperty("db.oracle.pass");
		String query = prop.getProperty("db.query");
		String driver = "";
        // Make the db.driver optional and look for the driver automatically
		Connection conn = null;
		try {
			if (verboseDft.equals("true") && !hasGUI.equals("true")){
				driver = DriverManager.getDriver(url).toString();
				System.out.println("   JDBC URL = "+url);
				System.out.println("    DB USER = "+username);
				System.out.println("   DB PASWD = "+password);
				System.out.println("JDBC DRIVER = "+driver);
			}
			conn = DriverManager.getConnection(url, username, password);
			isValidConnection(conn,query);
			if (!hasGUI.equals("true")){
				System.out.println(getOutput());
			}
		}catch (Exception e) {
			// handle the exception
			setError(e);
			if (!hasGUI.equals("true")){
				System.out.println();
				System.out.println(getErrorMessage());
				System.exit(1);
			}
		} finally {
			// release database resources
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
	private List<LinkedHashMap<String,String>> convertResultSetToList(ResultSet rs) throws SQLException {
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    List<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String,String>>();
	    while (rs.next()) {
	    	LinkedHashMap<String,String> row = new LinkedHashMap<String, String>(columns);
	        for(int i=1; i<=columns; ++i) {
	        	String valval = rs.getString(i);
	        	row.put(md.getColumnName(i),(valval==null)?"":valval);
	        }
	        list.add(row);
	    }
	    return list;
	}
	/*
	* Describe Table
	*
	* @param conn
	*          a JDBC connection object
	* @param query
	*          a sql query to test against database connection
	* @return true if a given connection object is a valid one; otherwise return
	*         false.
	*/
	private boolean executeDescribe(Connection conn, String query) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Pattern par = Pattern.compile(" ");
		String[] mat = par.split(query);
		String tableName=mat[1];
    if (hasGUI.equals("false")){
		  System.out.println();
		  System.out.println("Describe "+tableName);
    }
		try{
			stmt = conn.prepareStatement("select * from "+tableName);
			rs = stmt.executeQuery();
			if (rs == null) {
				return false;
			}
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount=rsmd.getColumnCount();
		if (hasGUI.equals("true")){
            columnNames.add("Column Name");
            columnNames.add("Type");
            for (int i = 1; i <= columnCount; i++) {
            	Vector<Object> vector = new Vector<Object>();
		        vector.add(rsmd.getColumnName(i));
		        vector.add(rsmd.getColumnTypeName(i));
		        data.add(vector);
            }
		}else{
			System.out.println("----------------------------");
			for (int i = 1; i <= columnCount ; i++) {
				System.out.format("%-10s\t:%s\n",rsmd.getColumnName(i),rsmd.getColumnTypeName(i));
			}
		}
		}catch (Exception e){
			setError(e);
			return false;
		}
		return true;
	}
	/*
	* Test Validity of a Connection
	*
	* @param conn
	*          a JDBC connection object
	* @param query
	*          a sql query to test against database connection
	* @return true if a given connection object is a valid one; otherwise return
	*         false.
	*/
	private boolean executeQuery(Connection conn, String query) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		BufferedWriter writer = null;
		try {
			stmt = conn.prepareStatement(query);
			if (stmt == null) {
				return false;
			}
			if (!hasGUI.equals("true")){
				System.out.println();
				System.out.println(Messages.getString("GUIBuilder.01")+" : " + query+"\n");
			}
			rs = stmt.executeQuery();
			if (rs == null) {
				return false;
			}
		  ResultSetMetaData rsmd = rs.getMetaData();
		  int columnsNumber = rsmd.getColumnCount();
		  if (hasGUI.equals("true")){
	       for (int i = 1; i <= columnsNumber; i++) {
	          columnNames.add(rsmd.getColumnName(i));
	       }
	       while (rs.next()) {
			     Vector<Object> vector = new Vector<Object>();
			     for (int i = 1; i <= columnsNumber; i++) {
			         vector.add(rs.getObject(i));
			     }
			     data.add(vector);
	       }
		  }else{
			   //prepare the table
			   HashMap<String,Integer> colList = new HashMap<String,Integer>();
			   for (int i = 1; i <= columnsNumber; i++){
			   	colList.put(rsmd.getColumnName(i), 6);
			   }
			   List<LinkedHashMap<String, String>> list = convertResultSetToList(rs);
			   for (HashMap<String, String> temp : list){
			   	 for(Entry<String, String> entry : temp.entrySet()){
			   	 	 int strLen = entry.getValue().equals(null)?6:entry.getValue().length();
			   	 	 int colLentgh = strLen>colList.get(entry.getKey())?strLen:colList.get(entry.getKey());
			   	 	 colList.put(entry.getKey(), colLentgh) ;
			   	 }
			   }
			    // Display the table
				String s = "";
			  for (LinkedHashMap<String, String> temp : list){
			  	for(Entry<String, String> entry : temp.entrySet()){
			   		String sHead = String.format("| %-"+(colList.get(entry.getKey())+1)+"s",entry.getKey());
			   		s += sHead;
			   	}
			   	break;
				}
			  s +="\n";
			  int sL = s.length();
		    for (int i = 0; i <= sL; i++){
		   		s+="-";
		   	}
		   	s +="\n";
			  for (LinkedHashMap<String, String> temp : list){
			   	for(Entry<String, String> entry : temp.entrySet()){
			   		String sBody = String.format("| %-"+(colList.get(entry.getKey())+1)+"s",entry.getValue());
			   		s += sBody;
			   	}
			    s +="\n";
				}
				if (!outputFile.equalsIgnoreCase("")){
		      File logFile = new File(outputFile);
		      System.out.println("Output will be writed in "+logFile.getCanonicalPath());
		      writer = new BufferedWriter(new FileWriter(logFile));
		      writer.write(s);
				}else{
					queryOut=s;
				}
		  }
			if (rs.next()) {
				// connection object is valid: we were able to
				// connect to the database and return something useful.
				return true;
			}
			// there is no hope any more for the validity
			// of the connection object
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			setError(e);
			return false;
		} finally {
			// close database resources
			try {
				rs.close();
				stmt.close();
				writer.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}
	public String getErrorMessage(){
		return errMsg;
	}
	public String getOutput(){
		return queryOut;
	}
	public String getStackTrace(){
		return stackTrace;
	}
	public String getFullStackTrace(){
		return errMsg +"\n"+stackTrace;
	}
	public Vector<Vector<Object>> getData() {
		return data;
	}
	public Vector<String> getColumnNames() {
		return columnNames;
	}
	/*
	* Test Validity of JDBC Installation
	*
	* @param conn
	*          a JDBC connection object
	* @return true if a given connection object is a valid one; otherwise return
	*         false.
	* @throws Exception
	*           Failed to determine if a given connection is valid.
	*/
	private boolean isValidConnection(Connection conn,String query) throws Exception {
		if (conn == null) {
			// null connection object is not valid
			return false;
		}
		if (conn.isClosed()) {
			// closed connection object is not valid
			return false;
		}
		if (versionDft.equals("true") && !hasGUI.equals("true")){
			  DatabaseMetaData dbmd = conn.getMetaData();
		    System.out.println("=====  Database info =====");
		    System.out.println(" DatabaseProductName: " + dbmd.getDatabaseProductName() );
		    System.out.println(" DatabaseProductVersion: " + dbmd.getDatabaseProductVersion() );
		    System.out.println("=====  Driver info =====");
		    System.out.println(" DriverName: " + dbmd.getDriverName() );
		    System.out.println(" DriverVersion: " + dbmd.getDriverVersion() );
		}
		// Check Query Content
		if (query != "" && query != null ){
			Boolean describeBool = query.toLowerCase().startsWith("desc");
			if (describeBool)
				return executeDescribe(conn,query);
			else
				return executeQuery(conn,query);
		}
		else
			return testConnection(conn);
	}
	private void setError(Exception e){
		setErrorMessage(e.getMessage());
		setStackTrace(e.getStackTrace());
		if (toConsole.equals("true") || !hasGUI.equals("true")){
			System.out.println(getErrorMessage());
			if (fullStackTrace.equals("true")) System.out.println(getStackTrace());
		}
	}
	private void setErrorMessage(String errMsg){
		this.errMsg=errMsg;
	}
	private void setStackTrace(StackTraceElement[] stackTraceElements){
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : stackTraceElements) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
		this.stackTrace = sb.toString();
	}
	/*
	* Test Validity of a Connection
	*
	* @param conn
	*          a JDBC connection object
	* @return true if a given connection object is a valid one; otherwise return
	*         false.
	*/
	public boolean testConnection(Connection conn) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("select 1 from dual");
			if (stmt == null) {
				return false;
			}
			System.out.println("Executing query : select 1 from dual");
			rs = stmt.executeQuery();
			if (rs == null) {
				return false;
			}
			if (rs.next()) {
				// connection object is valid: we were able to
				// connect to the database and return something useful.
				return true;
			}
			// there is no hope any more for the validity
			// of the connection object
			return false;

		} catch (Exception e) {
			setError(e);
			return false;
		} finally {
			// close database resources
			try {
				rs.close();
				stmt.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				// ignore
			}
		}
	}

}
