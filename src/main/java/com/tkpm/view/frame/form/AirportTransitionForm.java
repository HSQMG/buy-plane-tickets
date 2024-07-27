package com.tkpm.view.frame.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.tkpm.entities.Airport;
import com.tkpm.entities.Transition;

@SuppressWarnings("serial")
public class AirportTransitionForm extends JDialog implements FormBehaviour {

	private JPanel panelButton;
	private JPanel panelInfo;
	
	private JLabel jlbTransitionAir;
	private JLabel jlbAirportId;
	private JLabel jlbTransitionTime;
	private JLabel jlbNote;
	private JLabel warningText;
	
	private JComboBox<Airport> airportComboBox;
	private JTextField txtTransitionTime;
	private JTextField txtNote;
	
	private JButton cancelButton;
	private JButton okButton;
	
	private Transition model;
	
	public static final int NO_ERROR = 0;
	public static final int EMPTY_STAR_FIELD_ERROR = 1;
	public static final int NUMBER_FIELD_ERROR = 2;
	
	private static final String[] ERRORS = {
			"",
			"Có ít nhất một ô (*) bị trống",
			"Thời gian dừng phải là số nguyên"
	};
	
	//Ignore not an number value from an event
	//	And open the flag of nan error in number field
	private void ignoreNANValue(KeyEvent event) {
		char character = event.getKeyChar();
		if (('0' <= character) && (character <= '9') || (KeyEvent.VK_BACK_SPACE == character)) {
			setError(NO_ERROR);
		} else {
			event.consume();
			setError(NUMBER_FIELD_ERROR);
		}
	}
		
	private void initNumberFields() {
		txtTransitionTime.addKeyListener(new KeyAdapter() {
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
		jlbTransitionAir = new JLabel("Sân bay trung gian");
		jlbTransitionAir.setPreferredSize(new Dimension(91, 50));
		jlbTransitionAir.setFont(new Font("Segoe UI", Font.BOLD, 18));
		jlbTransitionAir.setHorizontalAlignment(JLabel.CENTER);
		
		panelInfo = new JPanel();
		panelInfo.setBackground(Color.WHITE);
		panelInfo.setBorder(new EmptyBorder(25, 50, 10, 50));
		
		jlbAirportId = new JLabel("Sân bay (*)");
		jlbAirportId.setPreferredSize(new Dimension(55, 30));
		
		airportComboBox = new JComboBox<>();
		airportComboBox.setBackground(Color.WHITE);
		airportComboBox.setPreferredSize(new Dimension(7, 30));
		
		jlbTransitionTime = new JLabel("Thời gian dừng (*)");
		
		txtTransitionTime = new JTextField();
		txtTransitionTime.setPreferredSize(new Dimension(7, 30));
		txtTransitionTime.setColumns(10);
		
		jlbNote = new JLabel("Ghi chú");
		
		txtNote = new JTextField();
		txtNote.setPreferredSize(new Dimension(7, 60));
		txtNote.setColumns(10);
		
		warningText = new JLabel("");
		warningText.setForeground(Color.RED);
		
		panelButton = new JPanel();
		panelButton.setBackground(Color.WHITE);
		panelButton.setPreferredSize(new Dimension(10, 60));
		panelButton.setBorder(new EmptyBorder(10, 75, 20, 75));
		panelButton.setLayout(new GridLayout(0, 2, 30, 0));

		cancelButton = new JButton("Huỷ");
		cancelButton.setBackground(Color.WHITE);
		
		okButton = new JButton("Thêm");
		okButton.setBackground(new Color(41, 97, 213));
		okButton.setForeground(Color.WHITE);
		
		initNumberFields();
		initButtons();
	}
	
	private void addComponents() {
		getContentPane().add(jlbTransitionAir, BorderLayout.NORTH);
		
		getContentPane().add(panelInfo, BorderLayout.CENTER);
		panelInfo.setLayout(new GridLayout(0, 1, 0, 10));
		panelInfo.add(jlbAirportId);
		panelInfo.add(airportComboBox);
		panelInfo.add(jlbTransitionTime);
		panelInfo.add(txtTransitionTime);
		panelInfo.add(jlbNote);
		panelInfo.add(txtNote);
		panelInfo.add(warningText);
		
		getContentPane().add(panelButton, BorderLayout.SOUTH);
		panelButton.add(okButton);
		panelButton.add(cancelButton);
	}
	
	public void init() {
		getContentPane().setBackground(Color.WHITE);
		setBounds(100, 100, 500, 480);
		setResizable(false);
		
		initComponents();
		addComponents();
		
		model = new Transition();
		
		setLocationRelativeTo(null);
	}

	/**
	 * Create the dialog.
	 */
	public AirportTransitionForm() {
		init();
	}
	
	public AirportTransitionForm(JFrame owner) {
		super(owner, true);
		init();
	}
	
	public AirportTransitionForm(JDialog owner) {
		super(owner, true);
		init();
	}
	
	
	public boolean areThereAnyEmptyStarField() {
		
		Airport airport = (Airport) airportComboBox.getSelectedItem();
		if (null == airport) {
			return true;
		}
		
		String transitionTime = txtTransitionTime.getText().trim();
		if (null == transitionTime || transitionTime.equals("")) {
			return true;
		}
		
		return false;
	}

	public void loadAirport(List<Airport> airports)  {
		airportComboBox.removeAllItems();
		airports.forEach(airport -> airportComboBox.addItem(airport));
	}
	
	public void loadModel(Transition model) {
		this.model= model;
		airportComboBox.setSelectedItem(model.getAirport());
		txtTransitionTime.setText(model.getTransitionTime().toString());
		txtNote.setText(model.getNote());
	}
	
	@Override
	public Transition submit() {
		model.setAirport((Airport) airportComboBox.getSelectedItem());
		model.setTransitionTime(Integer.parseInt(txtTransitionTime.getText()));
		model.setNote(txtNote.getText());
		return model;
	}

	@Override
	public JButton getSubmitButton() {
		return okButton;
	}

	@Override
	public FormBehaviour setError(int errorCode) {
		if (0 <= errorCode && errorCode < ERRORS.length) {
			warningText.setText(ERRORS[errorCode]);
		}
		return this;
	}

	@Override
	public void clear() {
		airportComboBox.setSelectedIndex(-1);
		txtTransitionTime.setText("");
		txtNote.setText("");
	}

	@Override
	public void close() {
		model = new Transition();
		setVisible(false);
	}
	
	public void open() {
		clear();
		setVisible(true);
	}

}
