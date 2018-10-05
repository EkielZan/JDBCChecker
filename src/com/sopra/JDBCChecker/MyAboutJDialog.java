package com.sopra.JDBCChecker;

import javax.swing.JDialog;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Manifest;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class MyAboutJDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	String verString,bildString,dateString = "";
	/**
	 *
	 */
	public MyAboutJDialog() {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(155, 0, 365, 268);
		getContentPane().add(scrollPane);

		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		String aboutText = new String();

		getInfoFromManifest();

		aboutText = "<b>CheckJDBC for Oracle</b><br><br>";
		aboutText = aboutText.concat("Version&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: <i>"+verString +"</i><br>");
		aboutText = aboutText.concat("Build num  : <i>"+bildString+"</i><br>");
		aboutText = aboutText.concat("Build date : <i>"+dateString+"</i><br>");
		aboutText = aboutText.concat("<br>(c) Copyright Gilles Dejeneffe 2018.  All rights reserved. Oracle and Java are trademarks or registered trademarks of Oracle and/or its affiliates. Other names may be trademarks of their respective owners.");
		aboutText = aboutText.concat("<br><br>This product includes software developed by other open source projects including the Apache Software Foundation, https://www.apache.org/.\")");
		textPane.setText(aboutText);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(new ImageIcon(MyAboutJDialog.class.getResource("/com/sopra/JDBCChecker/img/Gilles_Dejeneffe_4.jpg")).getImage().getScaledInstance(155, 268, Image.SCALE_SMOOTH)));
		lblNewLabel.setBounds(0, 0, 155, 268);
		getContentPane().add(lblNewLabel);
		this.setSize(526,296);
	}
	private void getInfoFromManifest(){

		URLClassLoader cl = (URLClassLoader)  CheckJDBC.class.getClassLoader();
		try {
			URL url = cl.findResource("META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(url.openStream());
			verString = manifest.getMainAttributes().getValue("Specification-Version");
			bildString = manifest.getMainAttributes().getValue("Implementation-Version");
			dateString = manifest.getMainAttributes().getValue("Built-Date");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
