package com.tkpm.view.frame.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import com.tkpm.entities.Airport;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.CustomerAccount;
import com.tkpm.entities.Flight;
import com.tkpm.entities.FlightDetail;
import com.tkpm.entities.ManagerAccount;
import com.tkpm.entities.Transition;
import com.tkpm.entities.User;
import com.tkpm.entities.User.USER_ROLE;
import com.tkpm.utils.HashingUtil;
import com.tkpm.view.feature_view.table.TransitionCRUDTableView;

public class UpdateAccountForm extends JDialog implements FormBehaviour {

	final protected int HEIGHT = 300;
	final protected int WIDTH = 400;
	
	private JPanel centerPanel;
	private JPanel footerPanel;
	private JPanel contentPane;
	
	private List<JLabel> labels;
	private List<JTextField> textFields;
	private List<JTextField> numericTextFields;	
	private JComboBox<USER_ROLE> roleComboBox;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private User model;
	private JLabel warningText;	
	
	private BaseAccount newAccount;
	private boolean isChangeRole = false;
	
	private static final String[] ERRORS = {
			"",
			"Có ít nhất một ô (*) bị bỏ trống",
			"Ô phải có giá trị là số",
			"Tên đăng nhập đã tồn tại",

	};
	
	public static final int NO_ERROR = 0;
	public static final int EMPTY_FIELD_ERROR = 1;
	public static final int NUMBER_FIELD_ERROR = 2;
	public static final int EXISTED_USERNAME_ERROR = 3;
	
	public UpdateAccountForm setError(int errorCode) {
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
		okButton = new JButton("OK");
		cancelButton = new JButton("Huỷ");
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		labels = new ArrayList<>(Arrays.asList(
				new JLabel("Tên đăng nhập (*)"),
				new JLabel("Mật khẩu đã mã hóa"),
				new JLabel("Chức vụ"),
				new JLabel("Họ và tên"),
				new JLabel("CMND/CCCD"),
				new JLabel("Số điện thoại")));
		
		//Initiate text fields
		textFields = new ArrayList<>();
		int numberOfTextField = 3;
		for (int i = 0; i < numberOfTextField; ++i) {
			textFields.add(new JTextField());
		}	
		textFields.get(1).setEditable(false);	//make encrypted password field uneditable
		
		
		//Initiate combo box
		roleComboBox = new JComboBox<>();
		for (USER_ROLE role: USER_ROLE.values()) {
			roleComboBox.addItem(role);
		}
	
		
		//Initiate numeric fields
		numericTextFields = new ArrayList<>();
		int numberOfNumericField = 2;
		for (int i = 0; i < numberOfNumericField; ++i) {
			numericTextFields.add(new JTextField());
		}
	
		initButtons();
		initNumericFields();
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
		centerPanel.add(textFields.get(1), "6, " + offset * 2 + ", fill, default");	//add encrypted password
		++offset;
		centerPanel.add(roleComboBox, "6, " + offset * 2 + ", fill, default");	//add role
		++offset;
		centerPanel.add(textFields.get(2), "6, " + offset * 2 + ", fill, default");	//add name
		++offset;
		for (int i = 0; i < numericTextFields.size(); ++i) {
			centerPanel.add(numericTextFields.get(i), "6, " + (i + offset) * 2 + ", fill, default");
		}
		
		
		//Footer setup
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		footerPanel.add(okButton);
		footerPanel.add(cancelButton);
				
	}
	
	public void init() {
		initComponents();
		setLayout();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		setContentPane(contentPane);
	}
	
	private void initModel() {
		model = new User();
		model.setAccount(new CustomerAccount());
		
	}
	

	public UpdateAccountForm(User model, JFrame owner) {
		super(owner, true);
		init();
		setModel(model);
		setSize(WIDTH, HEIGHT);
	}
	
	public UpdateAccountForm(JFrame owner) {
		super(owner, true);
		init();
		initModel();
		setSize(WIDTH, HEIGHT);
	}
	
	
	public JButton getOkButton() {return okButton;}
	public JDialog setModel(User model) {
		this.model = model;
		
		textFields.get(0).setText(model.getUsername());
		textFields.get(1).setText(model.getEncryptedPassword());
		
		USER_ROLE role = USER_ROLE.convertStringToUSER_ROLE(model.getRole());
		roleComboBox.setSelectedItem(role);
		
		String name, identity_code, phone;
		name = identity_code = phone = null;
		
		if (role.equals(USER_ROLE.Admin)) {
			AdminAccount adminAccount = (AdminAccount) model.getAccount();
			name = adminAccount.getName();
			identity_code = adminAccount.getIdentityCode();
			phone = adminAccount.getPhoneNumber();
			
		} else if (role.equals(USER_ROLE.Customer)) {
			CustomerAccount customerAccount = (CustomerAccount) model.getAccount();
			name = customerAccount.getName();
			identity_code = customerAccount.getIdentityCode();
			phone = customerAccount.getPhoneNumber();
			
		} else if (role.equals(USER_ROLE.Manager)) {
			ManagerAccount  managerAccount = (ManagerAccount) model.getAccount();
			name = managerAccount.getName();
			identity_code = managerAccount.getIdentityCode();
			phone = managerAccount.getPhoneNumber();
			
		}
		
		textFields.get(2).setText(name);
		numericTextFields.get(0).setText(identity_code);
		numericTextFields.get(1).setText(phone);
		
		return this;
	}
	
	public User submit() {
		
		model.setUsername(textFields.get(0).getText().trim());
		String encryptedPassword = textFields.get(1).getText().trim();
		model.setEncryptedPassword(encryptedPassword);
		USER_ROLE role = (USER_ROLE) roleComboBox.getSelectedItem();
		model.setRole(role.name());
		
		String name = textFields.get(2).getText().trim();
		String identityCode = numericTextFields.get(0).getText().trim();
		String phone = numericTextFields.get(1).getText().trim();
		
		newAccount = null;
		
		if (role.equals(USER_ROLE.Admin)) {
			AdminAccount acc = new AdminAccount();
			acc.setId(model.getId());
			acc.setUser(model);
			acc.setReservations(model.getAccount().getReservations());
			acc.setName(name);
			acc.setIdentityCode(identityCode);
			acc.setPhoneNumber(phone);
			newAccount = acc;
			
		} else if (role.equals(USER_ROLE.Customer)) {
			CustomerAccount acc = new CustomerAccount();
			acc.setId(model.getId());
			acc.setUser(model);
			acc.setReservations(model.getAccount().getReservations());
			acc.setName(name);
			acc.setIdentityCode(identityCode);
			acc.setPhoneNumber(phone);
			newAccount = acc;
			
		} else if (role.equals(USER_ROLE.Manager)) {
			ManagerAccount acc = new ManagerAccount();
			acc.setId(model.getId());
			acc.setUser(model);
			acc.setReservations(model.getAccount().getReservations());
			acc.setName(name);
			acc.setIdentityCode(identityCode);
			acc.setPhoneNumber(phone);
			newAccount = acc;
		}
		
		model.setAccount(newAccount);
		
		//Check if is change role
		if (!role.equals(USER_ROLE.convertStringToUSER_ROLE(model.getRole()))) {
			isChangeRole = true;
		} else {
			isChangeRole = false;
		}
		
		return model;
	}
	
	public JButton getSubmitButton() {
		return okButton;
	}
	
	public User getModel() {return model;}
	public BaseAccount getNewAccount() {return newAccount;}
	public boolean isChangeRole() {return isChangeRole;}

	public boolean areThereAnyEmptyStarField() {
		
		String username = textFields.get(0).getText().trim();
		if (null == username || username.equals("")) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void clear() {
		for (JTextField field: textFields) {field.setText("");}
		for (JTextField field: numericTextFields) {field.setText("");}	
	}
	
	@Override
	public void close() {
		clear();
		setVisible(false);
	}
}
