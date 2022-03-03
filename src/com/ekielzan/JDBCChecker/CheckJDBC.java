package com.ekielzan.JDBCChecker;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.jar.Manifest;
import java.util.Enumeration;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import org.apache.commons.cli.*;
import java.io.IOException;
import java.util.Collections;
import java.util.TreeSet;

/**
 * @(#)CheckJDBC.java
 *
 * CheckJDBC application
 *
 * @author GDJ
 * @version 3.01 2015/11/10
 */


public class CheckJDBC {
	private Properties prop = new Properties();
	private Properties fromArgs = new Properties();
	private Options options = null;
	private Boolean specOpt = false, fileFound = false, fileCreate = false, toDisplayConfig = false;
	private String userSpec = "", pswdSpec = "", dabaSpec = "", querySpec = "", generatedFile = "";
	private String language = "en";
	/*
	* Default Constructor
	*
	* @param args
	*          The arguments passed by the command line interface
	*/
	public CheckJDBC(String[] args) {
		long startTime = System.currentTimeMillis();
		//Messages.setLanguage(language);
		buildMenu();
		settings2Dft();
		checkArgs(args);
		String fileC = prop.getProperty("apps.file");
		readConfigFile(fileC);
		Messages.setLanguage(language);
		prop.putAll(fromArgs);
		if (toDisplayConfig){
			displayConfig(prop);
			System.exit(0);
		}
		if (fileCreate == true){
			if (specOpt == true){
				new ConfigFileGenerator(generatedFile, prop);
				System.out.println(generatedFile+" "+Messages.getString("GUIBuilder.27"));
			}else{
				if (fileFound == true){
					new ConfigFileGenerator(generatedFile,prop);
					System.out.println(fileC+" "+Messages.getString("GUIBuilder.29")+" "+generatedFile+".");
				}else{
					new ConfigFileGenerator(generatedFile);
					System.out.println(generatedFile+" "+Messages.getString("GUIBuilder.28"));
					System.exit(0);
				}
			}
		}else{
			if (prop.getProperty("apps.gui").equals("true")){
				GUIBuilder window = new GUIBuilder(prop);
				window.frame.setVisible(true);
			}else{
				JDBCTestor jdbct = new JDBCTestor(prop);
				jdbct.getErrorMessage();
			}
		}
		if (!prop.getProperty("apps.gui").equals("true")){
			long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(Messages.getString("GUIBuilder.02")+" : " +elapsedTime+"ms");
		}
	}
	/*
	* Display Configuration
	*
	*
	*/
	private void displayConfig(Properties toDisplay){
		System.out.println(Messages.getString("GUIBuilder.30"));
		Properties tmp = new Properties() {
			private static final long serialVersionUID = 42l;
    	@Override
    	public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
    	}
		};
		tmp.putAll(toDisplay);
		Enumeration keys = tmp.keys();
    while (keys.hasMoreElements()) {
      String key = (String)keys.nextElement();
      String value = (String)tmp.get(key);
      System.out.println(key + ": " + value);
    }
	}
	/*
	* Build Menu
	*
	*
	*/

	private void buildMenu() {
		options = new Options();
		options.addOption("d","database", true,  "DB jdbc url. ");
		//
		OptionBuilder.withArgName("filename");
		OptionBuilder.withLongOpt("generate");
		OptionBuilder.withDescription("Config File Generation");
		OptionBuilder.hasOptionalArg();
		options.addOption(OptionBuilder.create("g"));
		//
		options.addOption("f","file",     true,  "Config File. ");
		options.addOption("G","gui",      false,  "Display the GUI. ");
		options.addOption("h","help",     false, "Usage Display. ");
		//
		OptionBuilder.withArgName("output");
		OptionBuilder.withLongOpt("output");
		OptionBuilder.withDescription("Push output to file <output>");
		OptionBuilder.hasArg();
		options.addOption(OptionBuilder.create("o"));
		//
		options.addOption("l","locale", true,  Messages.getString("GUIBuilder.31"));
		options.addOption("p","password", true,  Messages.getString("GUIBuilder.05"));
		options.addOption("q","query",    true,  "SQL Query.");
		options.addOption("s","silent",   false, "Be quiet.");
		options.addOption("c","config",   false, "Display Configuration.");
		options.addOption("u","user",     true,  "DB User Name. ");
		options.addOption("V","Oversion", false, "Display Driver and DB version info ");
		options.addOption("v","version",  false, "Display CheckJDBC Version");
	}
	/**
	* Load Default configuration
	*
	* @noparam
	*/
	private void settings2Dft() {
		prop.setProperty("apps.version", "false");
		prop.setProperty("apps.file", "JDBC.config");
		prop.setProperty("apps.verbose", "true");
		prop.setProperty("apps.outputFile", "");
		prop.setProperty("apps.gui", "false");
		prop.setProperty("apps.language", "en");
		prop.setProperty("apps.stack.full", "false");
		prop.setProperty("apps.stack.toConsole", "false");
	}
	/**
	* Read and load configuration file
	*
	* @param file
	*          the configuration filename to load
	*/
	private void readConfigFile(String file){
		try{
			InputStream fi = new FileInputStream(file);
			prop.load(fi);
			fi.close();
			fileFound = true;
		} catch (Exception e){
			System.out.println(e.getMessage());
			fileFound = false;
			if (fileCreate==false){
				System.out.println("File Not Found!");
				System.exit(10);
			}
		}
		if (specOpt == true){
			if (!"".equals(dabaSpec))  prop.setProperty("db.oracle.jdbc.url",dabaSpec);
			if (!"".equals(userSpec))  prop.setProperty("db.oracle.user",userSpec);
			if (!"".equals(pswdSpec))  prop.setProperty("db.oracle.pass",pswdSpec);
			if (!"".equals(querySpec)) prop.setProperty("db.query",querySpec);
		}
	}
	/**
	* Check Given Arguments
	*
	* @param args
	*          a array of Strings
	* @param logging
	*/
	private void checkArgs(String[] args){
		CommandLine cmd = null;
		if ( args.length != 0){
			HelpFormatter formatter = new HelpFormatter();
			CommandLineParser parser = new PosixParser();
			try {
				cmd = parser.parse(options, args);
			} catch (ParseException e) {
				formatter.printHelp("java -jar CheckJDBC.jar", options,true);
				System.exit(1);
			}
			// If option -d "Database" is called.
			if (cmd.hasOption("d")){
				dabaSpec=cmd.getOptionValue("d");
				fromArgs.setProperty("db.oracle.jdbc.url", dabaSpec);
			}
			// If option -f "Filename" is called.
			if (cmd.hasOption("f")){
				prop.setProperty("apps.file", cmd.getOptionValue("f"));
			}
			// If option -g "Generation" is called.
			if (cmd.hasOption("g")){
				fileCreate = true;
				generatedFile = "dummy.config";
				if (cmd.getOptionValue("g") != null ) prop.setProperty("apps.file", cmd.getOptionValue("g"));;
			}
			// If option -G the App is in Gui mode.
			if (cmd.hasOption("G")){
				fromArgs.setProperty("apps.gui","true");
				fromArgs.setProperty("apps.verbose", "false");
			}
			// If option -h "Help" is called.
			if (cmd.hasOption("h")){
				formatter.printHelp("java -jar CheckJDBC.jar", options);
				System.exit(0);
			}
			// If option -c The full config is display (Debug purpose).
			if (cmd.hasOption("c")){
				toDisplayConfig = true;
			}
			// If option -o "output" is called.
			if (cmd.hasOption("o")){
				fromArgs.setProperty("apps.outputFile",cmd.getOptionValue("o"));
			}
			// If option -l we change the language of the tools.
			if (cmd.hasOption("l")) {
				language = cmd.getOptionValue("l");
				fromArgs.setProperty("apps.language", language);
			}
			// If option -p "Password" is called.
			if (cmd.hasOption("p")) {
				pswdSpec = cmd.getOptionValue("p");
				fromArgs.setProperty("db.oracle.pass", pswdSpec);
			}
			if (cmd.hasOption("u")) {
				userSpec = cmd.getOptionValue("u");
				fromArgs.setProperty("db.oracle.user", userSpec);
			}
			// If option -q "Query" is called.
			if (cmd.hasOption("q")){
				querySpec=cmd.getOptionValue("q");
				fromArgs.setProperty("db.query", querySpec);
			}
			if (cmd.hasOption("s")){
				fromArgs.setProperty("apps.verbose", "false");
			}

			// If option -v "version" is called. (Oracle / JDBC Version)
			if (cmd.hasOption("v")){
				System.out.println("Version : "+getVersionFromManifest());
				System.exit(0);
			}
			// If option -V "Oversion" is called. (Oracle / JDBC Version)
			if (cmd.hasOption("V")){
				fromArgs.setProperty("apps.version", "true");
			}
		}
	}
	/**
	 * Return a {@link String}} containing version of the build
	 * extract from the info packaged in the jar and
	 * in the MANIFEST.MF
	 *
	 * @return the version as string
	 *
	 */
	private String getVersionFromManifest(){
		String verString = "";
		URLClassLoader cl = (URLClassLoader)  CheckJDBC.class.getClassLoader();
		try {
			URL url = cl.findResource("META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(url.openStream());
			verString = manifest.getMainAttributes().getValue("Specification-Version");
			verString +="-"+manifest.getMainAttributes().getValue("Implementation-Version");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return verString;
	}
	/**
	* Main call to launch the applications
	*
	* @param args
	*          a Array of Strings
	*/
	public static void main(String[] args) {
		new CheckJDBC(args);
	}
}
