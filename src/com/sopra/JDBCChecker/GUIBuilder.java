package com.sopra.JDBCChecker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class GUIBuilder {
	JFrame frame;
	private JLabel lblNewLabel;
	private JTextField txtURLField;
	private JTextField txtUsernamefield;
	private JTextField txtPasswordfield;
	private JTextField txtQueryfield;
	private Properties full = null;
	private JPanel backPane = null;
	private JPanel statusPanel = null;
	private JLabel statusLabel1 = null;
	private final Action action = new SwingAction();
	private String frameTitle = "CheckJDBC"; //$NON-NLS-1$
	private JButton btnTrace = null;
	private StackTraceViewer dialog =null;
	private JTable table;
  private DefaultTableModel tableModel = null;
	protected int column;
	private GroupLayout groupLayout = null;
	private JScrollPane scrollPane = null;
	private JButton btnNewButton;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnAbout;
	private JMenuItem deleteItem;
	private JMenuItem mntmAbout;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmExport;
	private JMenu mnOptions;
	private JFileChooser jfc;
	private JMenuItem mntmExit;
	private MyAboutJDialog aboutDialog = null;

	/**
	 * Create the application.
	 */
	public GUIBuilder(Properties merged) {
		full = merged;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 * @return
	 */

	private void initialize() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) { //$NON-NLS-1$
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (UnsupportedLookAndFeelException e) {
		    // handle exception
		} catch (ClassNotFoundException e) {
		    // handle exception
		} catch (InstantiationException e) {
		    // handle exception
		} catch (IllegalAccessException e) {
		    // handle exception
		}
		//Param
		String queryS = full.getProperty("db.query").length()!=0?full.getProperty("db.query"):"select * from dual"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String userS = full.getProperty("db.oracle.user").length()!=0?full.getProperty("db.oracle.user"):""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String paswdS = full.getProperty("db.oracle.pass").length()!=0?full.getProperty("db.oracle.pass"):""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String jURLS = full.getProperty("db.oracle.jdbc.url").length()!=0?full.getProperty("db.oracle.jdbc.url"):""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//Frame
		frame = new JFrame();
		backPane = new JPanel();
		dialog = new StackTraceViewer();
		aboutDialog = new MyAboutJDialog();
		frame.setTitle(frameTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(550, 400));
		lblNewLabel = new JLabel(Messages.getString("GUIBuilder.03")); //$NON-NLS-1$
		JLabel lblUsername = new JLabel(Messages.getString("GUIBuilder.04")); //$NON-NLS-1$
		JLabel lblPassword = new JLabel(Messages.getString("GUIBuilder.05")); //$NON-NLS-1$
		JLabel lblQuery = new JLabel(Messages.getString("GUIBuilder.06")); //$NON-NLS-1$
		btnNewButton = new JButton(""); //$NON-NLS-1$
		btnNewButton.setVerticalAlignment(SwingConstants.TOP);
		scrollPane = new JScrollPane();
		txtURLField = new JTextField();
		txtUsernamefield = new JTextField();
		txtPasswordfield = new JTextField();
		txtQueryfield = new JTextField();

		txtURLField.setText(jURLS);
		txtURLField.setColumns(10);
		txtUsernamefield.setText(userS);
		txtUsernamefield.setColumns(10);
		txtPasswordfield.setText(paswdS);
		txtPasswordfield.setColumns(10);
		txtQueryfield.setText(queryS);
		txtQueryfield.setColumns(10);

		txtPasswordfield.setAction(action);
		txtQueryfield.setAction(action);
		txtURLField.setAction(action);
		txtUsernamefield.setAction(action);
		btnNewButton.setAction(action);

		btnNewButton.setIcon(new ImageIcon(GUIBuilder.class.getResource("/com/sopra/JDBCChecker/img/button-play_green.png"))); //$NON-NLS-1$
		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
		frame.getContentPane().add(backPane);
		groupLayout = new GroupLayout(backPane);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblUsername, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblQuery)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addGroup(groupLayout.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(txtUsernamefield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(lblPassword)
											.addGap(18)
											.addComponent(txtPasswordfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addComponent(txtURLField))
									.addGap(18)
									.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
								.addComponent(txtQueryfield, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
					.addContainerGap())
				.addComponent(statusPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtURLField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUsername)
								.addComponent(lblPassword)
								.addComponent(txtPasswordfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtUsernamefield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblQuery)
						.addComponent(txtQueryfield, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(statusPanel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
		);

		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		final JPopupMenu popupMenu = new JPopupMenu();
		deleteItem = new JMenuItem(Messages.getString("GUIBuilder.07")); //$NON-NLS-1$
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	removeColumn(column, table);
    	    	deleteItem.setEnabled((table.getColumnCount()<=1)?false:true);
            }
        });
        popupMenu.add(deleteItem);
        final JTableHeader tableHeader = table.getTableHeader();
        tableHeader.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)){
                    popupMenu.show(tableHeader, me.getX(), me.getY());
                	column = table.columnAtPoint(new Point(me.getX(),me.getY()));
                }
            }
        });
        table.setTableHeader(tableHeader);
		scrollPane.setViewportView(table);
		btnTrace = new JButton();
		btnTrace.setText(Messages.getString("GUIBuilder.11")); //$NON-NLS-1$
		btnTrace.setFont(new Font("Tahoma", Font.PLAIN, 9)); //$NON-NLS-1$
		btnTrace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				if(!dialog.isVisible()){
					dialog.setVisible(true);
				}else{
					dialog.setVisible(false);
				}
			}
		});
		JPanel panel = new JPanel();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{360, 0};
		gbl_panel.rowHeights = new int[]{18, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		statusLabel1 = new JLabel(Messages.getString("GUIBuilder.13")); //$NON-NLS-1$
		statusLabel1.setEnabled(false);
		GridBagConstraints gbc_statusLabel1 = new GridBagConstraints();
		gbc_statusLabel1.insets = new Insets(0, 13, 0, 0);
		gbc_statusLabel1.anchor = GridBagConstraints.NORTHWEST;
		gbc_statusLabel1.gridx = 0;
		gbc_statusLabel1.gridy = 0;
		panel.add(statusLabel1, gbc_statusLabel1);
		statusLabel1.setHorizontalAlignment(SwingConstants.LEFT);
		GroupLayout gl_statusPanel = new GroupLayout(statusPanel);
		gl_statusPanel.setHorizontalGroup(
			gl_statusPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statusPanel.createSequentialGroup()
					.addComponent(btnTrace)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 483, GroupLayout.PREFERRED_SIZE))
		);
		gl_statusPanel.setVerticalGroup(
			gl_statusPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(btnTrace, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
				.addComponent(panel, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
		);
		statusPanel.setLayout(gl_statusPanel);
		backPane.setLayout(groupLayout);
		backPane.add(statusPanel, BorderLayout.SOUTH);
		frame.pack();

		jfc = new JFileChooser(){
		    /**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public void approveSelection(){
		        File f = getSelectedFile();
		        if(f.exists() && getDialogType() == SAVE_DIALOG){
		            int result = JOptionPane.showConfirmDialog(this,Messages.getString("GUIBuilder.14"),Messages.getString("GUIBuilder.15"),JOptionPane.YES_NO_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
		            switch(result){
		                case JOptionPane.YES_OPTION:
		                    super.approveSelection();
		                    return;
		                case JOptionPane.NO_OPTION:
		                    return;
		                case JOptionPane.CLOSED_OPTION:
		                    return;
		                case JOptionPane.CANCEL_OPTION:
		                    cancelSelection();
		                    return;
		            }
		        }
		        super.approveSelection();
		    }
		};

		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnFile = new JMenu(Messages.getString("GUIBuilder.16")); //$NON-NLS-1$
		menuBar.add(mnFile);

		mntmOpen = new JMenuItem(Messages.getString("GUIBuilder.17")); //$NON-NLS-1$
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jfc.setCurrentDirectory(new File("."));
				FileFilter ft = new FileNameExtensionFilter(Messages.getString("GUIBuilder.18"), "config"); //$NON-NLS-1$ //$NON-NLS-2$
				jfc.addChoosableFileFilter( ft );
				jfc.setFileFilter(ft);
				int returnVal = jfc.showOpenDialog(frame);
				if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
					java.io.File file = jfc.getSelectedFile( );
					loadConfigFiles(file);
				}
			}
		});
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem(Messages.getString("GUIBuilder.12")); //$NON-NLS-1$
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jfc.setCurrentDirectory(new File(".")); //$NON-NLS-1$
				FileFilter ft = new FileNameExtensionFilter(Messages.getString("GUIBuilder.18"), "config"); //$NON-NLS-1$ //$NON-NLS-2$
				jfc.addChoosableFileFilter( ft );
				jfc.setFileFilter(ft);
				int returnVal = jfc.showSaveDialog(frame);
				if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
					java.io.File file = jfc.getSelectedFile( );
					saveConfigFiles(file);
				}
			}
		});
		mnFile.add(mntmSave);

		mntmExit = new JMenuItem(Messages.getString("GUIBuilder.19")); //$NON-NLS-1$
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		mnOptions = new JMenu(Messages.getString("GUIBuilder.20")); //$NON-NLS-1$
		menuBar.add(mnOptions);

		mntmExport = new JMenuItem(Messages.getString("GUIBuilder.21")); //$NON-NLS-1$
		mntmExport.setEnabled(false);
		mntmExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(".")); //$NON-NLS-1$
				jfc.setDialogTitle(Messages.getString("GUIBuilder.22")); //$NON-NLS-1$
				FileFilter ft = new FileNameExtensionFilter(Messages.getString("GUIBuilder.23"), "xls"); //$NON-NLS-1$ //$NON-NLS-2$
				jfc.addChoosableFileFilter( ft );
				jfc.setFileFilter(ft);
				int userSelection = jfc.showSaveDialog(frame);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = jfc.getSelectedFile();
					String filePath = fileToSave.getAbsolutePath();
					if(!filePath.endsWith(".xls")) { //$NON-NLS-1$
						fileToSave = new File(filePath + ".xls"); //$NON-NLS-1$
					}
				    toExcel(table,fileToSave);
				}
			}
		});
		mnOptions.add(mntmExport);

		mnAbout = new JMenu(Messages.getString("GUIBuilder.24")); //$NON-NLS-1$
		menuBar.add(mnAbout);

		mntmAbout = new JMenuItem(Messages.getString("GUIBuilder.25")); //$NON-NLS-1$
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutDialog.setLocationRelativeTo(frame);
				aboutDialog.setVisible(true);
			}
		});
		mnAbout.add(mntmAbout);

	}

	public void toExcel(JTable table, File file){
	    try{
	        TableModel model = table.getModel();
	        FileWriter excel = new FileWriter(file);

	        for(int i = 0; i < model.getColumnCount(); i++){
	            excel.write(model.getColumnName(i) + "\t"); //$NON-NLS-1$
	        }
	        excel.write("\n"); //$NON-NLS-1$
	        for(int i=0; i< model.getRowCount(); i++) {
	            for(int j=0; j < model.getColumnCount(); j++) {
	                excel.write(model.getValueAt(i,j).toString()+"\t"); //$NON-NLS-1$
	            }
	            excel.write("\n"); //$NON-NLS-1$
	        }
	        excel.close();
	    }catch(IOException e){
	    	System.out.println(e);
	    }finally {
	    	JOptionPane.showMessageDialog(frame,
	    		    Messages.getString("GUIBuilder.26")); //$NON-NLS-1$
		}
	}

	private void removeColumn(int index, JTable myTable){
	    int nRow= myTable.getRowCount();
	    int nCol= myTable.getColumnCount()-1;
	    Object[][] cells= new Object[nRow][nCol];
	    String[] names= new String[nCol];

	    for(int j=0; j<nCol; j++){
	        if(j<index){
	            names[j]= myTable.getColumnName(j);
	            for(int i=0; i<nRow; i++){
	                cells[i][j]= myTable.getValueAt(i, j);
	            }
	        }else{
	            names[j]= myTable.getColumnName(j+1);
	            for(int i=0; i<nRow; i++){
	                cells[i][j]= myTable.getValueAt(i, j+1);
	            }
	        }
	    }
	    DefaultTableModel newModel= new DefaultTableModel(cells, names);
	    myTable.setModel(newModel);
	}

	private Boolean loadConfigFiles(File file){
		Properties prop = new Properties();
		try{
			InputStream fi = new FileInputStream(file);
			prop.load(fi);
			fi.close();
			//System.out.println(" - "+fileFound);
		} catch (Exception e){
			System.out.println(e.getMessage());
			return false;
		}
		String queryS = prop.getProperty("db.query").length()!=0?prop.getProperty("db.query"):"select * from dual"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String userS  = prop.getProperty("db.oracle.user").length()!=0?prop.getProperty("db.oracle.user"):""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String paswdS = prop.getProperty("db.oracle.pass").length()!=0?prop.getProperty("db.oracle.pass"):""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String jURLS  = prop.getProperty("db.oracle.jdbc.url").length()!=0?prop.getProperty("db.oracle.jdbc.url"):""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		txtQueryfield.setText(queryS);
		txtUsernamefield.setText(userS);
		txtPasswordfield.setText(paswdS);
		txtURLField.setText(jURLS);
		return true;
	}

	private Boolean saveConfigFiles(File file){
		Properties prop = new Properties();
		String queryS = txtQueryfield.getText();
		String userS  = txtUsernamefield.getText();
		String paswdS = txtPasswordfield.getText();
		String jURLS  = txtURLField.getText();

		prop.setProperty("db.query", queryS); //$NON-NLS-1$
		prop.setProperty("db.oracle.user", userS); //$NON-NLS-1$
		prop.setProperty("db.oracle.pass", paswdS); //$NON-NLS-1$
		prop.setProperty("db.oracle.jdbc.url", jURLS); //$NON-NLS-1$

		new ConfigFileGenerator(file.getAbsolutePath(), prop);

		return true;
	}

	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			tableModel = new DefaultTableModel();
			table.setModel(tableModel);
			btnTrace.setForeground(new Color(0,0,0));
			long startTime = System.currentTimeMillis();
			full.setProperty("db.oracle.jdbc.url",txtURLField.getText()); //$NON-NLS-1$
			full.setProperty("db.oracle.user",txtUsernamefield.getText()); //$NON-NLS-1$
			full.setProperty("db.oracle.pass",txtPasswordfield.getText()); //$NON-NLS-1$
			full.setProperty("db.query",txtQueryfield.getText());	 //$NON-NLS-1$
			JDBCTestor JDBC = new JDBCTestor(full);
			//txtpnOutput.setText(JDBC.getOutput());
			statusLabel1.setText(JDBC.getErrorMessage());
			//
			Vector<Vector<Object>> data = JDBC.getData();
			Vector<String> columnNames = JDBC.getColumnNames();
			tableModel.setDataVector(data, columnNames);
			//
			long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    frame.setTitle(frameTitle+" - "+elapsedTime+"ms"); //$NON-NLS-1$ //$NON-NLS-2$
	    dialog.setTrace(JDBC.getFullStackTrace());
	    mntmExport.setEnabled(true);
	    if (dialog.getStack()){
				dialog.setVisible(true);
	    	mntmExport.setEnabled(false);
	    	btnTrace.setForeground(new Color(255, 0, 0));
	    }
	    deleteItem.setEnabled((table.getColumnCount()<=1)?false:true);
		}
	}
}
