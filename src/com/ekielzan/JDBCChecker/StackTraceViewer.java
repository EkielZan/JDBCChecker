package com.ekielzan.JDBCChecker;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;

public class StackTraceViewer extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextPane txtrStackviewer = new JTextPane();
	private final JScrollPane scrollPane = new JScrollPane();
	/**
	 * Create the dialog.
	 */
	public StackTraceViewer() {
		setBounds(550, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(txtrStackviewer);
		txtrStackviewer.setEditable(false);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StackTraceViewer.this.dispose();
			}
		});
	}
	public void setTrace(String trace){
		txtrStackviewer.setText(trace);
	}
	public boolean getStack() {
		if(txtrStackviewer.getText().length()>13){
			return true;
		}
		return false;
	}
}