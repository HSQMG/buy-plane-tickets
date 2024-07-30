package com.tkpm.view.frame.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.tkpm.entities.CustomerAccount;
import com.tkpm.entities.User;
import com.tkpm.entities.User.USER_ROLE;
import com.tkpm.utils.HashingUtil;
import javax.swing.SwingConstants;
import java.awt.Insets;

public class Registration extends JDialog implements FormBehaviour{

	private JPanel contentPane;
	private JPanel panelRegistrate;
	private JPanel panelInfo;
	private JPanel panelButton;
	
	private JLabel background;
	private JLabel jlbUsername;
	private JLabel jlbPassword;
	private JLabel jlbRetypePassword;
	private JLabel jlbName;
	private JLabel jlbIdentity;
	private JLabel jlbMobileNumber;
	private JCheckBox showPasswordCheckbox;
	
	private JTextField txtUsername;
	private JPasswordField passtxtPassword;
	private JPasswordField passtxtRetypePassword;
	private JTextField txtName;
	private JTextField txtIdentity;
	private JTextField txtMobileNumber;
	
	private JButton cancelButton;
	private JButton okButton;
	
	public static final int NO_ERROR = 0;
	public static final int EMPTY_STAR_FIELD_ERROR = 1;
	public static final int EXISTED_USERNAME_ERROR = 2;
	public static final int PASSWORD_MISMATCH_ERROOR = 3;
	public static final int NUMBER_FIELD_ERROR = 4;
	
	private static final String[] ERRORS = {
			"",
			"Có ít nhất một ô (*) bị trống",
			"Tên đăng nhập đã tồn tại",
			"<html><body>Nhập lại mật khẩu và<br>mật khẩu mới không trùng khớp</body></html>",
			"CMND/CCCD và SĐT phải là số"
	};
	private JLabel warningText;
	
	private void initCheckbox() {
		showPasswordCheckbox.addActionListener((event) -> {
				
			//If the check box is selected
			//	=> Show password
			if (showPasswordCheckbox.isSelected()) {
				passtxtPassword.setEchoChar((char)0);
				passtxtRetypePassword.setEchoChar((char)0);
			} else {
					
				//If the check box is not selected
				//	=> Hide password by setting echo character with (char)'\u2022'
				passtxtPassword.setEchoChar('\u2022');
				passtxtRetypePassword.setEchoChar('\u2022');
			}
				
		});

	}
	
	//Ignore not an number value from an event
	//	And open the flag of nan error in number field
	private void ignoreNANValue(KeyEvent event) {
		char character = event.getKeyChar();
		if (('0' <= character) && (character <= '9') || (KeyEvent.VK_BACK_SPACE == character )) {
			setError(NO_ERROR);
		} else {
			event.consume();
			setError(NUMBER_FIELD_ERROR);
		}
	}
	
	private void initNumberFields() {
		txtIdentity.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ignoreNANValue(e);
			}
		});
		
		txtMobileNumber.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ignoreNANValue(e);
			}
		});
	}
	
	private void initButtons() {
		cancelButton.addActionListener((event)->{
			close();
		});
		
	}
	
	private void initComponents() {
		
		background = new JLabel("");
		background.setHorizontalAlignment(SwingConstants.CENTER);
		background.setBackground(Color.WHITE);
		background.setIcon(new ImageIcon(Registration.class.getResource("/com/tkpm/view/frame/form/rafiki.jpg")));
		
		panelRegistrate = new JPanel();
		panelRegistrate.setBackground(Color.WHITE);
		
		panelInfo = new JPanel();
		panelInfo.setBorder(new EmptyBorder(20, 80, 10, 40));
		panelInfo.setBackground(Color.WHITE);
		
		panelButton = new JPanel();
		panelButton.setBorder(new EmptyBorder(0, 20, 0, 20));
		panelButton.setPreferredSize(new Dimension(260, 30));
		panelButton.setBackground(Color.WHITE);

		jlbUsername = new JLabel("Tên đăng nhập (*)");
		jlbUsername.setPreferredSize(new Dimension(260, 30));
		
		txtUsername = new JTextField();
		txtUsername.setPreferredSize(new Dimension(260, 30));
		txtUsername.setColumns(10);
		
		jlbPassword = new JLabel("Mật khẩu (*)");
		jlbPassword.setPreferredSize(new Dimension(260, 30));
		
		passtxtPassword = new JPasswordField();
		passtxtPassword.setPreferredSize(new Dimension(260, 30));
		
		jlbRetypePassword = new JLabel("Nhập lại mật khẩu (*)");
		jlbRetypePassword.setPreferredSize(new Dimension(260, 30));
		
		passtxtRetypePassword = new JPasswordField();
		passtxtRetypePassword.setPreferredSize(new Dimension(260, 30));
		
		jlbName = new JLabel("Họ và tên");
		jlbName.setPreferredSize(new Dimension(260, 30));
		
		txtName = new JTextField();
		txtName.setPreferredSize(new Dimension(260, 30));
		txtName.setColumns(10);
		
		jlbIdentity = new JLabel("CMND/CCCD");
		jlbIdentity.setPreferredSize(new Dimension(260, 30));
		
		txtIdentity = new JTextField();
		txtIdentity.setPreferredSize(new Dimension(260, 30));
		txtIdentity.setColumns(10);
		
		jlbMobileNumber = new JLabel("Số điện thoại");
		jlbMobileNumber.setPreferredSize(new Dimension(260, 30));
		
		txtMobileNumber = new JTextField();
		txtMobileNumber.setPreferredSize(new Dimension(260, 30));
		txtMobileNumber.setColumns(10);
		
		showPasswordCheckbox = new JCheckBox("Hiện mật khẩu");
		showPasswordCheckbox.setPreferredSize(new Dimension(260, 30));
		showPasswordCheckbox.setBackground(Color.WHITE);
		
		cancelButton = new JButton("Huỷ");
		cancelButton.setBackground(Color.WHITE);
		
		okButton = new JButton("Đăng ký");
		okButton.setForeground(Color.WHITE);
		okButton.setBackground(new Color(41, 97, 213));
		
		warningText = new JLabel("");
		warningText.setForeground(Color.RED);
		
		panelRegistrate.setLayout(new BorderLayout(0, 0));
		panelInfo.setLayout(new GridLayout(15, 1, 0, 10));
		panelButton.setLayout(new GridLayout(0, 2, 20, 0));
		
		initCheckbox();
		initButtons();
		initNumberFields();
	}
	
	private void setLayout() {
		contentPane.setLayout(new GridLayout(0, 2, 10, 0));
		
		contentPane.add(panelRegistrate);
		panelRegistrate.add(panelInfo, BorderLayout.CENTER);
		panelInfo.add(jlbUsername);
		panelInfo.add(txtUsername);
		panelInfo.add(jlbPassword);
		panelInfo.add(passtxtPassword);
		panelInfo.add(jlbRetypePassword);
		panelInfo.add(passtxtRetypePassword);
		panelInfo.add(jlbName);
		panelInfo.add(txtName);
		panelInfo.add(jlbIdentity);
		panelInfo.add(txtIdentity);
		panelInfo.add(jlbMobileNumber);
		panelInfo.add(txtMobileNumber);
		panelInfo.add(showPasswordCheckbox);
		panelInfo.add(panelButton);
		panelButton.add(cancelButton);
		panelButton.add(okButton);
		panelInfo.add(warningText);
		
		contentPane.add(background);
	}

	
	/**
	 * Create the frame.
	 */
	public Registration() {
		setTitle("Đăng ký");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 770, 512);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(50, 75, 25, 75));
		setContentPane(contentPane);
		
		initComponents();
		setLayout();
		
		this.pack();
		
	}
	
	public Registration(JFrame owner) {
		super(owner, true);
		setTitle("Đăng ký");
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.setBounds(100, 100, 770, 512);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(50, 75, 25, 75));
		contentPane.setLayout(new BorderLayout(50, 0));
		setContentPane(contentPane);
		
		initComponents();
		setLayout();
		
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
	}
	
	public boolean areThereAnyEmptyStarField() {
		
		String username = txtUsername.getText().trim();
		if (null == username || username.equals("")) {
			return true;
		}
		
		String emptyStringEncryption = HashingUtil.passwordEncryption("");
		if (null == new String(passtxtPassword.getPassword()).trim()) {
			return true;
		}
		String hashing = HashingUtil.passwordEncryption(new String(passtxtPassword.getPassword()).trim());
		if (emptyStringEncryption.equals(hashing)) {
			return true;
		}
		
		if (null == new String(passtxtRetypePassword.getPassword()).trim()) {
			return true;
		}
		hashing = HashingUtil.passwordEncryption(new String(passtxtRetypePassword.getPassword()).trim());
		if (emptyStringEncryption.equals(hashing)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isPasswordMismatch() {
		
		String hash0 = HashingUtil.passwordEncryption(
				new String(passtxtPassword.getPassword()).trim());
		
		String hash1 = HashingUtil.passwordEncryption(
				new String(passtxtRetypePassword.getPassword()).trim());
		
		return !hash0.equals(hash1);
	}

	@Override
	public User submit() {
		User user = new User();
		user.setUsername(txtUsername.getText().trim());
		String encryptedPassword = HashingUtil.passwordEncryption(
				new String(passtxtPassword.getPassword()).trim());
		user.setEncryptedPassword(encryptedPassword);
		user.setRole(USER_ROLE.Customer.name());
		
		CustomerAccount account = new CustomerAccount();
		account.setName(txtName.getText());
		account.setIdentityCode(txtIdentity.getText());
		account.setPhoneNumber(txtMobileNumber.getText());
		
		user.setAccount(account);
		
		return user;
	}

	@Override
	public JButton getSubmitButton() { return okButton;	}
	public JButton getCancelButton() { return cancelButton; }

	@Override
	public FormBehaviour setError(int errorCode) {
		if (0 <= errorCode && errorCode < ERRORS.length) {
			warningText.setText(ERRORS[errorCode]);
		}
		return this;
	}

	@Override
	public void clear() {
		txtUsername.setText("");
		passtxtPassword.setText("");
		passtxtRetypePassword.setText("");
		txtName.setText("");
		txtIdentity.setText("");
		txtMobileNumber.setText("");
	}

	@Override
	public void close() {
		setVisible(false);
	}
	
	public void open() {
		clear();
		setVisible(true);
	}
}
