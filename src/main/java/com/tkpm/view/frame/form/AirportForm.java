package com.tkpm.view.frame.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.tkpm.entities.Airport;

@SuppressWarnings("serial")
public class AirportForm extends JDialog implements FormBehaviour {

	private JPanel panelButton;
	private JPanel panelInfo;
	
	private JLabel jlbAirportName;
	private JLabel warningText;
	private JTextField txtAirportName;
	
	private JButton cancelButton;
	private JButton okButton;
	
	private Airport model;
	
	public static final int NO_ERROR = 0;
	public static final int EMPTY_STAR_FIELD_ERROR = 1;
	public static final int NAME_EXISTED_FIELD_ERROR = 2;
	
	private static final String[] ERRORS = {
			"",
			"Tên sân bay bị trống",
			"Tên sân bay đã tồn tại"
	};
	
	private void initButtons() {
		cancelButton.addActionListener((event)->{
			close();
		});
	}
	
	private void initComponents() {

		panelInfo = new JPanel();
		panelInfo.setBackground(Color.WHITE);
		panelInfo.setBorder(new EmptyBorder(25, 50, 10, 50));
		
		jlbAirportName = new JLabel("Tên sân bay (*)");
		
		txtAirportName = new JTextField();
		txtAirportName.setPreferredSize(new Dimension(7, 30));
		txtAirportName.setColumns(10);
		
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
		
		initButtons();
	}
	
	private void addComponents() {
		
		getContentPane().add(panelInfo, BorderLayout.CENTER);
		panelInfo.setLayout(new GridLayout(0, 1, 0, 10));
		panelInfo.add(jlbAirportName);
		panelInfo.add(txtAirportName);
		panelInfo.add(warningText);
		
		getContentPane().add(panelButton, BorderLayout.SOUTH);
		panelButton.add(okButton);
		panelButton.add(cancelButton);
	}
	
	public void init() {
		getContentPane().setBackground(Color.WHITE);
		setBounds(100, 100, 400, 270);
		setResizable(false);
		
		initComponents();
		addComponents();
		
		model = new Airport();
		
		setLocationRelativeTo(null);
	}

	/**
	 * Create the dialog.
	 */
	public AirportForm() {
		init();
	}
	
	public AirportForm(JFrame owner) {
		super(owner, true);
		init();
	}
	
	public AirportForm(JDialog owner) {
		super(owner, true);
		init();
	}
	
	
	public boolean areThereAnyEmptyStarField() {
		
		String airportName = txtAirportName.getText().trim();
		if (null == airportName || airportName.equals("")) {
			return true;
		}
		
		return false;
	}


	public void setAirport(Airport model) {
		this.model= model;		
		txtAirportName.setText(model.getName());
	}
	
	@Override
	public Airport submit() {
		model.setName(txtAirportName.getText().trim());
		
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
		txtAirportName.setText("");
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