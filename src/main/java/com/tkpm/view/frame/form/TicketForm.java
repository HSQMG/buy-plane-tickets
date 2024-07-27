package com.tkpm.view.frame.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import com.tkpm.entities.ManagerAccount;
import com.tkpm.entities.Ticket;
import com.tkpm.entities.User;
import com.tkpm.entities.User.USER_ROLE;

public class TicketForm extends JDialog implements FormBehaviour {

	final protected int HEIGHT = 300;
	final protected int WIDTH = 400;
	
	private JPanel centerPanel;
	private JPanel footerPanel;
	private JPanel contentPane;
	
	private List<JLabel> labels;
	
	private List<JTextField> textFields;
	private List<JTextField> numericFields;	
	private JComboBox<String> ticketClassComboBox;
	private JTextField priceField;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private Flight model;
	private JLabel warningText;	
	
	private static final String[] ERRORS = {
			"",
			"Có ít nhất một trường không có thông tin",
			"Giá trị của trường phải là số",
			"Đã hết vé",
			"Đã quá thời gian đặt vé"
	};
	
	public static final int NO_ERROR = 0;
	public static final int EMPTY_FIELD_ERROR = 1;
	public static final int NUMBER_FIELD_ERROR = 2;
	public static final int OUT_OF_STOCK_ERROR = 3;
	public static final int TIMEOUT_ERROR = 4;
	
	public TicketForm setError(int errorCode) {
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
				new JLabel("Số hiệu chuyến bay"),
				new JLabel("Tên hành khách (*)"),
				new JLabel("CMND/CCCD (*)"),
				new JLabel("Số điện thoại (*)"),
				new JLabel("Hạng vé"),
				new JLabel("Giá tiền")));
		
		//Init text fields
		textFields = new ArrayList<>();
		int numberOfTextField = 2;
		for (int i = 0; i < numberOfTextField; ++i) {
			textFields.add(new JTextField());
		}	
		textFields.get(0).setEditable(false);	//make flight id field uneditable
		textFields.get(0).setBackground(Color.WHITE);
		
		//Init price field
		priceField = new JTextField();
		priceField.setBackground(Color.WHITE);
		priceField.setEditable(false);
		
		//Init ticket class combo box
		ticketClassComboBox = new JComboBox<>();
		ticketClassComboBox.addItem("1");
		ticketClassComboBox.addItem("2");
		
		
		//Init numeric fields
		numericFields = new ArrayList<>();
		int numberOfNumericField = 2;
		for (int i = 0; i < numberOfNumericField; ++i) {
			numericFields.add(new JTextField());
		}
	
		initButtons();
		initNumericFields();
		initTicketClassComboBox();
	}
	
	private void initTicketClassComboBox() {
		ticketClassComboBox.addActionListener(event -> {
			String item = (String) ticketClassComboBox.getSelectedItem();
			if (item.equals("1")) {
				priceField.setText(model.getDetail().getPriceOfFirstClassSeat().toString());
			} else if (item.equals("2")) {
				priceField.setText(model.getDetail().getPriceOfSecondClassSeat().toString());
			}
		});
	}
	
	private void initNumericFields() {
		for (JTextField field: numericFields) {
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
		for (int i = 0; i < textFields.size(); ++i) {
			centerPanel.add(textFields.get(i), "6, " + (i + offset) * 2 + ", fill, default");
		}
		offset += textFields.size();
		for (int i = 0; i < numericFields.size(); ++i) {
			centerPanel.add(numericFields.get(i), "6, " + (i + offset) * 2 + ", fill, default");
		}
		offset += numericFields.size();
		
		centerPanel.add(ticketClassComboBox, "6, " + offset * 2 + ", fill, default");	//add role
		++offset;
		centerPanel.add(priceField, "6, " + offset * 2 + ", fill, default");	//add name
		++offset;
		
		//Footer setup
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		footerPanel.add(okButton);
		footerPanel.add(cancelButton);
				
	}
	
	public void init() {
		initComponents();
		setLayout();
		setTitle("Đặt vé");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		setContentPane(contentPane);
		setLocationRelativeTo(null);
	}
	
	private void initModel() {
		model = new Flight();
	}
	

	public TicketForm(Flight model, JFrame owner) {
		super(owner, true);
		init();
		setModel(model);
		setSize(WIDTH, HEIGHT);
	}
	
	public TicketForm(JFrame owner) {
		super(owner, true);
		init();
		initModel();
		setSize(WIDTH, HEIGHT);
	}
	
	
	public boolean areThereAnyEmptyStarField() {
		
		for (JTextField field: textFields) {
			String value = field.getText().trim();
			if (null == value || value.equals("")) {
				return true;
			}
		}
		
		for (JTextField field: numericFields) {
			String value = field.getText().trim();
			if (null == value || value.equals("")) {
				return true;
			}
		}
		
		return false;
	}

	
	public JButton getOkButton() {return okButton;}
	public JDialog setModel(Flight model) {
		this.model = model;
		
		textFields.get(0).setText(model.getId().toString());
		textFields.get(1).setText("");
		
		
		for (JTextField field: numericFields) {
			field.setText("");
		}
		
		priceField.setText(model.getDetail().getPriceOfFirstClassSeat().toString());
		
		return this;
	}
	
	//Get information from the form
	public String getTicketClass() {return (String) ticketClassComboBox.getSelectedItem();}
	public Flight getFlight() {return model;}
	public String getSubmitName() {return textFields.get(1).getText().trim();}
	public String getSubmitIdentityCode() {return numericFields.get(0).getText().trim();}
	public String getSubmitPhone() {return numericFields.get(1).getText().trim();}
	
	public JButton getSubmitButton() {
		return okButton;
	}
//	
//	public static void main(String[] args) {
//		TicketForm form = new TicketForm(null);
//		form.setVisible(true);
//	}

	@Override
	public Object submit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		textFields.get(1).setText("");
		for (JTextField field: numericFields) {
			field.setText("");
		}
		
	}

	@Override
	public void close() {
		this.clear();
		this.setVisible(false);
	}
}
