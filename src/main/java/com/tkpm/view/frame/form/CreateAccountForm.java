package com.tkpm.view.frame.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.tkpm.entities.AdminAccount;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.CustomerAccount;
import com.tkpm.entities.ManagerAccount;
import com.tkpm.entities.User;
import com.tkpm.entities.User.USER_ROLE;
import com.tkpm.utils.HashingUtil;
import com.tkpm.view.feature_view.table.TransitionCRUDTableView;

public class CreateAccountForm extends JDialog implements FormBehaviour {

	final protected int HEIGHT = 340;
	final protected int WIDTH = 400;
	
	private JPanel centerPanel;
	private JPanel footerPanel;
	private JPanel contentPane;
	
	private List<JLabel> labels;
	private List<JTextField> textFields;
	private List<JPasswordField> passwordFields;
	private List<JTextField> numericTextFields;	
	private JComboBox<USER_ROLE> roleComboBox;
	
	private JCheckBox showPasswordCheckbox;
	private JButton okButton;
	private JButton cancelButton;
	
	private User model;
	private JLabel warningText;	
	
	private static final String[] ERRORS = {
			"",
			"Có ít nhất một ô không có thông tin",
			"Ô phải mang giá trị số",
			"Tên đăng nhập đã tồn tại",
			"<html><body>Nhập lại mật khẩu và<br>mật khẩu mới không trùng khớp</body></html>",
	};
	
	public static final int NO_ERROR = 0;
	public static final int EMPTY_FIELD_ERROR = 1;
	public static final int NUMBER_FIELD_ERROR = 2;
	public static final int EXISTED_USERNAME_ERROR = 3;
	public static final int PASSWORD_MISMATCH_ERROOR = 4;
	
	public CreateAccountForm setError(int errorCode) {
		if (0 <= errorCode && errorCode < ERRORS.length) {
			warningText.setText(ERRORS[errorCode]);
		}
		return this;
	}
	
	//Ignore not an number value from an event
	//	And open the flag of nan error in number field
	private void ignoreNANValue(KeyEvent event) {
		char character = event.getKeyChar();
		if ((('0' <= character) && (character <= '9')) || (KeyEvent.VK_BACK_SPACE  == character)) {
			setError(NO_ERROR);
		} else {
			event.consume();
			setError(NUMBER_FIELD_ERROR);
		}
	}
	
	private void initComponents() {
		warningText = new JLabel();					
		warningText.setForeground(Color.RED);		//Warning have red text
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		
		footerPanel = new JPanel();
		footerPanel.setBackground(Color.WHITE);
		okButton = new JButton("Tạo");
		okButton.setBackground(new Color(41, 97, 213));
		okButton.setForeground(Color.WHITE);
		cancelButton = new JButton("Huỷ");
		
		centerPanel = new JPanel();
    centerPanel.setBackground(Color.WHITE);
    
		labels = new ArrayList<>(Arrays.asList(
				new JLabel("Tên đăng nhập (*)"),
				new JLabel("Mật khẩu (*)"),
				new JLabel("Nhập lại mật khẩu (*)"),
				new JLabel("Chức vụ"),
				new JLabel("Họ và tên"),
				new JLabel("CMND/CCCD"),
				new JLabel("Số điện thoại")));
		
		//Init text fields
		textFields = new ArrayList<>();
		int numberOfTextField = 2;
		for (int i = 0; i < numberOfTextField; ++i) {
			textFields.add(new JTextField());
		}	
		
		//Init password fields
		passwordFields = new ArrayList<>();
		int numberOfPasswordField = 2;
		for (int i = 0; i < numberOfPasswordField; ++i) {
			passwordFields.add(new JPasswordField());
		}
			
		//Init checkbox
		showPasswordCheckbox = new JCheckBox("Hiện mật khẩu");
		
		//Init combo box
		roleComboBox = new JComboBox<>();
		for (USER_ROLE role: USER_ROLE.values()) {
			roleComboBox.addItem(role);
		}
	
		
		//Init numeric fields
		numericTextFields = new ArrayList<>();
		int numberOfNumericField = 2;
		for (int i = 0; i < numberOfNumericField; ++i) {
			numericTextFields.add(new JTextField());
		}
	
		initCheckbox();
		initButtons();
		initNumericFields();
	}
	
	private void initCheckbox() {
		showPasswordCheckbox.addActionListener((event) -> {
				
			//If the check box is selected
			//	=> Show password
			if (showPasswordCheckbox.isSelected()) {
				passwordFields.forEach(field -> {
					field.setEchoChar((char)0);
				});
			} else {
					
				//If the check box is not selected
				//	=> Hide password by setting echo character with (char)'\u2022'
				passwordFields.forEach(field -> {
					field.setEchoChar('\u2022');
				});
			}
				
		});

	}
	
	private void initNumericFields() {
		for (JTextField field: numericTextFields) {
			field.addKeyListener(new KeyAdapter() {
				   public void keyTyped(KeyEvent e) {
					   ignoreNANValue(e);
				   }
			});
		}
	}
	
	private void initButtons() {
		cancelButton.addActionListener((event)->{
			this.dispose();
		});
		
	}
	
	private void setLayout() {
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//Center setup
		//JScrollPane centerScroll = new JScrollPane(centerPanel);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC}));
		
		
		for (int i = 0; i<labels.size(); ++i) {
			String metaLayout = "4, " + (i+2)*2 + ", right, default";
			centerPanel.add(labels.get(i), metaLayout);
		}
		
		int offset = 1;
		centerPanel.add(warningText, "6, " + offset * 2 + ", center, default");
		++offset;
		centerPanel.add(textFields.get(0), "6, " + offset * 2 + ", fill, default");	//add username
		++offset;
		for (int i = 0; i < passwordFields.size(); ++i) {
			centerPanel.add(passwordFields.get(i), "6, " + (i + offset) * 2 + ", fill, default"); // add password1 and password 2
		}
		offset += passwordFields.size();
		centerPanel.add(roleComboBox, "6, " + offset * 2 + ", fill, default");	//add role
		++offset;
		centerPanel.add(textFields.get(1), "6, " + offset * 2 + ", fill, default");	//add name
		++offset;
		for (int i = 0; i < numericTextFields.size(); ++i) {
			centerPanel.add(numericTextFields.get(i), "6, " + (i + offset) * 2 + ", fill, default");
		}
		
		
		//Footer setup
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		footerPanel.add(showPasswordCheckbox);
		footerPanel.add(okButton);
		footerPanel.add(cancelButton);
	}
	
	public void init() {
		initComponents();
		setLayout();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		setContentPane(contentPane);
		
		setLocationRelativeTo(null);
		
	}
	
	private void initModel() {
		model = new User();
		model.setAccount(new CustomerAccount());
		
	}
	
	
	public CreateAccountForm(JFrame owner) {
		super(owner, true);
		init();
		initModel();
		setSize(WIDTH, HEIGHT);
	}
	
	
	public JButton getOkButton() {return okButton;}
	
	public boolean areThereAnyEmptyStarField() {
		
		String username = textFields.get(0).getText().trim();
		if (null == username || username.equals("")) {
			return true;
		}
		
		String emptyStringEncryption = HashingUtil.passwordEncryption("");
		for (JPasswordField field: passwordFields) {
			if (null == new String(field.getPassword()).trim()) {
				return true;
			}
			String hashing = HashingUtil.passwordEncryption(new String(field.getPassword()).trim());
			if (emptyStringEncryption.equals(hashing)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isPasswordMismatch() {
		
		String hash0 = HashingUtil.passwordEncryption(
				new String(passwordFields.get(0).getPassword()).trim());
		
		String hash1 = HashingUtil.passwordEncryption(
				new String(passwordFields.get(1).getPassword()).trim());
		
		return !hash0.equals(hash1);
	}
	
	public User submit() {
	
		model.setUsername(textFields.get(0).getText().trim());
		String encryptedPassword = HashingUtil.passwordEncryption(
				new String(passwordFields.get(0).getPassword()).trim());
		model.setEncryptedPassword(encryptedPassword);
		USER_ROLE role = (USER_ROLE) roleComboBox.getSelectedItem();
		model.setRole(role.name());
		
		String name = textFields.get(1).getText().trim();
		String identityCode = numericTextFields.get(0).getText().trim();
		String phone = numericTextFields.get(1).getText().trim();
		
		BaseAccount account = null;
		
		if (role.equals(USER_ROLE.Admin)) {
			AdminAccount acc = new AdminAccount();
			acc.setName(name);
			acc.setIdentityCode(identityCode);
			acc.setPhoneNumber(phone);
			account = acc;
		} else if (role.equals(USER_ROLE.Customer)) {
			CustomerAccount acc = new CustomerAccount();
			acc.setName(name);
			acc.setIdentityCode(identityCode);
			acc.setPhoneNumber(phone);
			account = acc;
			
		} else if (role.equals(USER_ROLE.Manager)) {
			ManagerAccount acc = new ManagerAccount();
			acc.setName(name);
			acc.setIdentityCode(identityCode);
			acc.setPhoneNumber(phone);
			account = acc;
		}
		model.setAccount(account);
		
		return model;
	}
	
	public JButton getSubmitButton() {
		return okButton;
	}
//	
//	public static void main(String[] args) {
//		CreateAccountForm form = new CreateAccountForm(null);
//		form.setVisible(true);
//	}

	@Override
	public void clear() {
		for (JTextField field: textFields) {
			field.setText("");
		}
		
		for (JPasswordField field: passwordFields) {
			field.setText("");
		}
		
		for (JTextField field: numericTextFields) {
			field.setText("");
		}
	}
	
	public void open() {
		clear();
		setVisible(true);
	}

	@Override
	public void close() {
		setVisible(false);
	}
}
