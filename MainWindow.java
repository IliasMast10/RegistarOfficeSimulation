package src;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/* Ilias Mastoras
 * 
 * This class demonstrates a look Up/Search application 
 * for students and their courses.
 */
public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTable table;
	private JTextField txtID;
	private JTextField txtLast;
	private JTextField txtDOB;
	public  ConnectOracle co;
	public int columns = 0;
	public ResultSetMetaData metaData = null;
	private JTable tblCourses;
	private JTextField txtFirst;
	public String lookUpID;
	public DefaultTableCellRenderer centerRenderer; 
	public DefaultTableModel myModel;

	
	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	 // Create the frame.
	public MainWindow() {
		co = new ConnectOracle();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 845, 423);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		myModel = new DefaultTableModel();
		
		JLabel lblNewLabel = new JLabel("Search Student by:");
		lblNewLabel.setBounds(10, 43, 132, 17);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		contentPane.add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedItem() == "Show All") {
					txtSearch.setText("");
					txtID.setText("");
					txtLast.setText("");
					txtFirst.setText("");
					txtDOB.setText("");
					((DefaultTableModel) tblCourses.getModel()).setRowCount(0);
					setTable();
				} 
			}
		});
		comboBox.setFont(new Font("Arial", Font.BOLD, 16));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Student ID", "Last Name", "First Name", "Show All"}));
		comboBox.setBounds(10, 63, 145, 28);
		contentPane.add(comboBox);
		
		txtSearch = new JTextField();
		txtSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				((DefaultTableModel) tblCourses.getModel()).setRowCount(0);	
				txtID.setText("");
				txtLast.setText("");
				txtFirst.setText("");
				txtDOB.setText("");
			}
		});
		txtSearch.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSearch.setFont(new Font("Arial", Font.BOLD, 16));
		txtSearch.setHorizontalAlignment(SwingConstants.CENTER);
		txtSearch.setBounds(165, 63, 140, 29);
		contentPane.add(txtSearch);
		txtSearch.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 137, 421, 217);
		contentPane.add(scrollPane);
		
		table = new JTable() {
			 public boolean isCellEditable(int row, int column)
			 {
			     return false;
			 }
		};
		 
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				  int row = table.getSelectedRow();
				  txtID.setText(table.getValueAt(row, 0).toString());
				  txtLast.setText(table.getValueAt(row, 1).toString());
				  txtFirst.setText(table.getValueAt(row, 2).toString());
				  txtDOB.setText(table.getValueAt(row, 3).toString());
				  lookUpID = txtID.getText();
				  courseLookup(lookUpID);
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int row = table.getSelectedRow();
		 		int key = arg0.getKeyCode();
				if( (key != KeyEvent.VK_RIGHT) || (key != KeyEvent.VK_LEFT)) {
		 			if((key == KeyEvent.VK_UP) && (row != 0)) {
		 				row = table.getSelectedRow()-1;
		 		    }
		 		    if((key == KeyEvent.VK_DOWN) && (row != table.getRowCount()-1)) {
		 			    row = table.getSelectedRow()+1;
		 		    }
				}
				txtID.setText(table.getValueAt(row, 0).toString());
	 		    txtLast.setText(table.getValueAt(row, 1).toString());
	 		    txtFirst.setText(table.getValueAt(row, 2).toString());
	 		    txtDOB.setText(table.getValueAt(row, 3).toString()); 
	 		    lookUpID = txtID.getText();
	 		    courseLookup(lookUpID);
			}
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);
		table.setFont(new Font("Arial", Font.PLAIN, 16));
		table.getTableHeader().setFont(new Font("Times New Roman", Font.PLAIN, 14));
		String[] tableColumnsName = {"Student ID","Last Name","First Name","Date of Birth"}; 
		
		myModel.setColumnIdentifiers(tableColumnsName);
		table.setModel(myModel);
		for (int i=0; i<table.getColumnCount();i++){
			table.setDefaultRenderer(table.getColumnClass(i),centerRenderer);
		}
		setTable(); // This function populates the table with all the students.
		scrollPane.setViewportView(table);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String selection ="studentID";
				String text = txtSearch.getText();
				boolean pass = false;
				
				if (txtSearch.getText().length() >= 1) { // if there is a valid input in the search text.
					
					if((comboBox.getSelectedItem() == "Last Name") || (comboBox.getSelectedItem() == "First Name")){
						if (comboBox.getSelectedItem() == "Last Name") {
							selection = "Last";
						} else {
							selection = "First";							
						}	
						text = txtSearch.getText().substring(0,1).toUpperCase() + txtSearch.getText().substring(1).toLowerCase();
						pass = true; 
					} 
					
				    // If it is not First or Last name in dropbox, then  is StudentID.
				    // Check if the input is digit. If yes create the sql statement.
				    if(!pass) { 
				    	boolean internalPass = true;
				    	for(int j=0; j<text.length(); j++) {
				    		if(!Character.isDigit(text.charAt(j))) {
				    			internalPass = false;
				    			break;
				    		}
				    	 } 
				    	 if(internalPass) {
				    		 pass = true;
				    	 }	 
				    }
				    if(pass) { 	
				    	 ((DefaultTableModel) table.getModel()).setRowCount(0);
				    	try {
				    		co.statement = co.connection.createStatement();
				    		String sql = "Select * from students where "+selection +"='" +text +"'";
				    		co.resultSet = co.statement.executeQuery(sql);
				    		
				    		// Loop through the ResultSet and transfer in the Model
				    		metaData = co.resultSet.getMetaData();
				    		columns = metaData.getColumnCount();
				    		
				    		while(co.resultSet.next()){
				    			 Object[] objects = new Object[columns];
				    			 int i=0;
				    			 objects[i]=co.resultSet.getString(1);
				    			 objects[i+1]=co.resultSet.getString(2);
				    			 objects[i+2]=co.resultSet.getString(3);
				    			 objects[i+3]=co.resultSet.getDate(4);
				    			 myModel.addRow(objects);
				    			 i++;
				    		}
				    		table.setModel(myModel);
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    }//end if (pass)	
				}//end if
			}
		});
		
		btnSearch.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnSearch.setFont(new Font("Arial", Font.BOLD, 16));
		btnSearch.setBounds(315, 68, 116, 23);
		contentPane.add(btnSearch);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(25, 20));
		separator.setSize(new Dimension(50, 20));
		separator.setBounds(441, 11, 2, 362);
		contentPane.add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Student ID:");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_1.setBounds(455, 40, 77, 17);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setFont(new Font("Arial", Font.BOLD, 14));
		lblLastName.setBounds(456, 74, 76, 17);
		contentPane.add(lblLastName);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setFont(new Font("Arial", Font.BOLD, 14));
		lblFirstName.setBounds(453, 106, 79, 17);
		contentPane.add(lblFirstName);
		
		JLabel lblDob = new JLabel("DOB:");
		lblDob.setFont(new Font("Arial", Font.BOLD, 14));
		lblDob.setBounds(496, 140, 35, 17);
		contentPane.add(lblDob);
		
		txtID = new JTextField();
		txtID.setHorizontalAlignment(SwingConstants.CENTER);
		txtID.setEditable(false);
		txtID.setDisabledTextColor(Color.GRAY);
		txtID.setFont(new Font("Arial", Font.BOLD, 16));
		txtID.setBounds(543, 36, 111, 24);
		contentPane.add(txtID);
		txtID.setColumns(10);
		
		txtLast = new JTextField();
		txtLast.setHorizontalAlignment(SwingConstants.CENTER);
		txtLast.setEditable(false);
		txtLast.setFont(new Font("Arial", Font.BOLD, 16));
		txtLast.setColumns(10);
		txtLast.setBounds(543, 67, 111, 24);
		contentPane.add(txtLast);
		
		txtFirst = new JTextField();
		txtFirst.setHorizontalAlignment(SwingConstants.CENTER);
		txtFirst.setFont(new Font("Arial", Font.BOLD, 16));
		txtFirst.setEditable(false);
		txtFirst.setColumns(10);
		txtFirst.setBounds(542, 101, 112, 24);
		contentPane.add(txtFirst);
		
		txtDOB = new JTextField();
		txtDOB.setHorizontalAlignment(SwingConstants.CENTER);
		txtDOB.setFont(new Font("Arial", Font.BOLD, 16));
		txtDOB.setEditable(false);
		txtDOB.setColumns(10);
		txtDOB.setBounds(543, 136, 111, 24);
		contentPane.add(txtDOB);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(452, 167, 369, 187);
		contentPane.add(scrollPane_1);
		
		tblCourses = new JTable() {	 
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
				
		tblCourses.setFillsViewportHeight(true);
		tblCourses.setFont(new Font("Arial", Font.PLAIN, 16));
		tblCourses.getTableHeader().setFont(new Font("Times New Roman", Font.PLAIN, 14));
		String[] ColumnNames = {"Course","Semester","Grade"}; 
		DefaultTableModel myM = new DefaultTableModel();
		myM.setColumnIdentifiers(ColumnNames);
		tblCourses.setModel(myM);
		scrollPane_1.setViewportView(tblCourses);
	} 
	
	//This method returns the courses the selected student has
	public void courseLookup(String id) {
		((DefaultTableModel) tblCourses.getModel()).setRowCount(0);
		String[] ColumnNames = {"Course","Semester","Grade"}; 
		DefaultTableModel myM = new DefaultTableModel();
		myM.setColumnIdentifiers(ColumnNames);
		try {
    		co.statement = co.connection.createStatement();
    		String sql = "Select c.courseType, c.courseNum, sc.Semester, sc.grade "
    				   + "FROM Courses c, Students_courses sc"
    				   + " WHERE studentID = " +id +" AND c.CourseID = sc.CourseID";
    		co.resultSet = co.statement.executeQuery(sql);
    		
    		// Loop through the ResultSet and transfer in the Model
    		metaData = co.resultSet.getMetaData();
    		columns = metaData.getColumnCount();
    		
    		while(co.resultSet.next()){
    			 Object[] objects = new Object[columns];
    			 int i=0;
    			 objects[i] = co.resultSet.getString(1) +" " +co.resultSet.getString(2);
    			 objects[i+1] = co.resultSet.getString(3);
    			 objects[i+2] = co.resultSet.getString(4);
    			 myM.addRow(objects);
    			 i++;
    		}
    	 	tblCourses.setModel(myM);
    	 	for (int i=0; i<tblCourses.getColumnCount();i++){
    			tblCourses.setDefaultRenderer(tblCourses.getColumnClass(i),centerRenderer);
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setTable() { // This function populates the table with all the students.
		((DefaultTableModel) table.getModel()).setRowCount(0);
		try {
    		co.statement = co.connection.createStatement();
    		String sql = "Select * from students ";
    		co.resultSet = co.statement.executeQuery(sql);
    		
      		// Loop through the ResultSet and transfer in the Model
    		metaData = co.resultSet.getMetaData();
    		columns = metaData.getColumnCount();
    		
    		while(co.resultSet.next()){
    			 Object[] objects = new Object[columns];
    			 int i=0;
    			 objects[i]=co.resultSet.getString(1);
    			 objects[i+1]=co.resultSet.getString(2);
    			 objects[i+2]=co.resultSet.getString(3);
    			 objects[i+3]=co.resultSet.getDate(4);
    			
    			 i++;
    			 myModel.addRow(objects);
    		}
    		table.setModel(myModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
