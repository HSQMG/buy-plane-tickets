package com.tkpm.view.frame.form;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.tkpm.entities.User;
import com.tkpm.utils.HashingUtil;
import com.tkpm.view.frame.BaseFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Canvas;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

public class Login extends BaseFrame implements FormBehaviour {
	
	private JLabel jlbUsername;					//Label for username field
	private JTextField txtUsername;				//Username text field
	private JLabel jlbPassword;					//Label for password field
	private JPasswordField passtxtPassword;		//Password text field
	private JCheckBox chckbxShowPassword;		//Checkbox to show/hide password
	private JButton btnLogin;					//Login button
	private JButton btnRegistrate;				//Registrate button
	
	private JPanel contentPanel;
	private JLabel Background;
	private JPanel panelLogin;
	private JPanel panelButton;
	
	//Display when: 
	//	1.) Wrong password or username: Type = 0
	private JLabel jlbWarningText;		
	
	public static final int NO_ERROR = 0;
	public static final int WRONG_ACCOUNT_ERROR = 1;
	
	private static final String[] ERRORS = {
		"",
		"Sai tên đăng nhập hoặc mật khẩu!",
	};
	private JPanel panelInfo;
		
	//Create and add Show/Hide feature for chckbxShowPassword
	private void initCheckbox() {
		chckbxShowPassword = new JCheckBox("Hiện mật khẩu");
		chckbxShowPassword.setBackground(Color.WHITE);
		chckbxShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		chckbxShowPassword.addActionListener((event) -> {
				
			//If the check box is selected
			//	=> Show password of passtxtPassword
			if (chckbxShowPassword.isSelected()) {
				passtxtPassword.setEchoChar((char)0);
			} else {
					
				//If the check box is not selected
				//	=> Hide password of passtxtPassword
				// by setting echo character with (char)'\u2022'
				passtxtPassword.setEchoChar('\u2022');
			}
				
		});

	}
	
	//Create all components;
	private void initComponents() {
		contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(750, 465));
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		Background = new JLabel("");
		ImageIcon icon = new ImageIcon(getClass().getResource("/path/to/image.png"));
		Background.setIcon(new ImageIcon(Login.class.getResource("/com/tkpm/view/frame/form/rafiki.jpg")));
		Background.setHorizontalAlignment(SwingConstants.CENTER);
		
		panelInfo = new JPanel();
		panelInfo.setBackground(Color.WHITE);
		panelInfo.setLayout(new GridLayout(8, 1, 0, 10));
		
		panelLogin = new JPanel();
		panelLogin.setBorder(new EmptyBorder(80, 15, 0, 15));
		panelLogin.setBackground(Color.WHITE);
		panelLogin.setLayout(new BorderLayout(0, 0));
		
		panelButton = new JPanel();
		panelButton.setBorder(new EmptyBorder(5, 30, 5, 30));
		panelButton.setBackground(Color.WHITE);
		panelButton.setLayout(new GridLayout(0, 2, 30, 0));
		
		btnLogin = new JButton("Đăng nhập");
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(new Color(41, 97, 213));
		
		btnRegistrate = new JButton("Đăng ký");
		
		jlbUsername = new JLabel("Tên đăng nhập");
		jlbUsername.setBackground(Color.WHITE);

		jlbPassword = new JLabel("Mật khẩu");
		jlbPassword.setBackground(Color.WHITE);
		
		jlbWarningText = new JLabel();					//No warning when start login form
		jlbWarningText.setBackground(Color.WHITE);
		jlbWarningText.setHorizontalAlignment(SwingConstants.CENTER);
		jlbWarningText.setForeground(Color.RED);		//Warning have red text
		
		txtUsername = new JTextField();
		txtUsername.setBackground(Color.WHITE);
		
		passtxtPassword = new JPasswordField();
		initCheckbox();									//Create and add Show/Hide feature for chckbxShowPassword
	}
	
	//Set size and location of each component
	private void setComponentSizeAndLocation() {
	}
	
	//Connect all components into getContentPane()
	private void addComponents() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(contentPanel);
		contentPanel.add(panelLogin);
		contentPanel.add(Background);
		panelLogin.add(panelInfo, BorderLayout.CENTER);
		
		panelInfo.add(jlbUsername);
		panelInfo.add(txtUsername);
		panelInfo.add(jlbPassword);
		panelInfo.add(passtxtPassword);
		panelInfo.add(chckbxShowPassword);
		panelInfo.add(panelButton);
		panelButton.add(btnRegistrate);
		panelButton.add(btnLogin);
		panelInfo.add(jlbWarningText);
		
	}
	
	public Login() {
		this.setTitle("Đăng nhập");
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initComponents();
		setComponentSizeAndLocation();
		addComponents();
		
		this.pack();
		setLocationRelativeTo(null);
		
	}

	@Override
	public User submit() {
		
		String username = txtUsername.getText().trim();
		String encryptedPassword = (null == passtxtPassword.getPassword()?
				HashingUtil.passwordEncryption(""):
				HashingUtil.passwordEncryption(new String(passtxtPassword.getPassword())));
		
		User user = new User();
		user.setUsername(username);
		user.setEncryptedPassword(encryptedPassword);
		
		return user;
	}

	@Override
	public JButton getSubmitButton() {return btnLogin;}
	public JButton getRegistrateButton() {return btnRegistrate;}

	@Override
	public FormBehaviour setError(int errorCode) {
		if (0 <= errorCode && errorCode < ERRORS.length) {
			jlbWarningText.setText(ERRORS[errorCode]);
		}
		
		return this;
	}

	@Override
	public void clear() {
		txtUsername.setText("");
		passtxtPassword.setText("");
		jlbWarningText.setText("");
	}
	
	@Override
	public void close() {
		clear();
		super.close();
	}
}
