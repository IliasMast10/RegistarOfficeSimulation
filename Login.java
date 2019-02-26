package src;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

/* Ilias Mastoras
 * 
 * This class creates a frame 
 * and accepts valid inputs for username and password.
 */

public class Login extends JFrame {	
	
    public static ConnectOracle conOracle;
	private JPanel contentPane;
	private JPasswordField txtPass;
	private JTextField txtUser;
	private static Login frame;
	boolean  found = false;
	public  MainWindow mainWin;
	private static ArrayList<String> user =  new ArrayList<String>();
	private static ArrayList<String> pass = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					conOracle = new ConnectOracle();
					// Retrieve usernames and passwords
					conOracle.statement = conOracle.connection.createStatement();
					String sql = "Select * from Users";
		    		conOracle.resultSet = conOracle.statement.executeQuery(sql);
		    	
		    		while(conOracle.resultSet.next()) {
		    			user.add(conOracle.resultSet.getString("username"));
		    			pass.add(conOracle.resultSet.getString("password"));
		    		}
					frame = new Login();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 258);
		contentPane = new JPanel();
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(49, 47, 90, 22);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(lblNewLabel);
		
		txtUser = new JTextField();
		txtUser.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtUser.setBounds(145, 44, 190, 28);
		txtUser.setHorizontalAlignment(SwingConstants.CENTER);
		txtUser.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(49, 106, 86, 22);
		lblPassword.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(lblPassword);
		
		txtPass = new JPasswordField();
		txtPass.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtPass.setBounds(145, 103, 190, 28);
		txtPass.setEchoChar('#');
		txtPass.setHorizontalAlignment(SwingConstants.CENTER);
		txtPass.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(txtPass);
		
		JButton btnEnter = new JButton("Login");
		btnEnter.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnEnter.setBounds(109, 146, 125, 50);
		btnEnter.setName("btnLogin");
		btnEnter.addMouseListener(new MouseAdapter() {
			// I will check if username and password are valid
			@Override 
			public void mouseClicked(MouseEvent arg0) {
				for (int i=0; i<user.size(); i++) {
					if((txtUser.getText().equals(user.get(i)) && (txtPass.getText().equals(pass.get(i))))){
						found = true;
					    break;
					}
				}
				if(found) {
					mainWin = new MainWindow();
					mainWin.setVisible(true);
					frame.dispose();
				}
			}
		});
		btnEnter.setFont(new Font("Arial", Font.BOLD, 18));
		contentPane.add(btnEnter);
		
		JLabel lblUsernamesAndPasswords = new JLabel("Usernames and Passwords are case sensitive.");
		lblUsernamesAndPasswords.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblUsernamesAndPasswords.setBounds(53, 202, 246, 16);
		contentPane.add(lblUsernamesAndPasswords);
	}
}
