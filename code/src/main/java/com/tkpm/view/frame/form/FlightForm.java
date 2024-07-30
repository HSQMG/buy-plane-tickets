package com.tkpm.view.frame.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.tkpm.entities.Airport;
import com.tkpm.entities.Flight;
import com.tkpm.entities.FlightDetail;
import com.tkpm.entities.Transition;
import com.tkpm.view.feature_view.table.TransitionCRUDTableView;

public class FlightForm extends JDialog implements FormBehaviour {

	final protected int HEIGHT = 500;
	final protected int WIDTH = 600;
	
	private JPanel centerPanel;
	private JPanel footerPanel;
	private JPanel contentPane;
	
	private List<JLabel> labels;
	private List<JComboBox<Airport>> airportComboBoxes;	//departure airport / arrival airport
	private DateTimePicker flightDateTimePicker;
	
	//flight time / first class seat size/ second class seat size/ first class seat price / second class seat price
	private List<JTextField> numericTextFields;	
	
	private JButton addTransitionButton;
	private JButton deleteTransitionButton;
	private TransitionCRUDTableView table;
	private AirportTransitionForm transitionForm;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private Flight model;
	private JLabel warningText;	
	
	private static final String[] ERRORS = {
			"",
			"Có ít nhất một ô không có thông tin",
			"Ô phải mang giá trị số nguyên dương",
			"Sân bay đi và sân bay đến không được giống nhau",
	};
	
	public static final int NO_ERROR = 0;
	public static final int EMPTY_FIELD_ERROR = 1;
	public static final int NUMBER_FIELD_ERROR = 2;
	public static final int AIRPORT_MATCH_ERROR = 3;
	
	
	@Override
	public FlightForm setError(int errorCode) {
		if (0 <= errorCode && errorCode < ERRORS.length) {
			warningText.setText(ERRORS[errorCode]);
		}
		return this;
	}
	
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
	
	private void initComponents() {
		warningText = new JLabel();					
		warningText.setForeground(Color.RED);		//Warning have red text
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		
		footerPanel = new JPanel();
		footerPanel.setBackground(Color.WHITE);
		okButton = new JButton("OK");
		cancelButton = new JButton("Huỷ");
		addTransitionButton = new JButton("Thêm");
		deleteTransitionButton = new JButton("Xóa");
		
		centerPanel = new JPanel();
		centerPanel.setBackground(Color.WHITE);
		labels = new ArrayList<>(Arrays.asList(
				new JLabel("Sân bay đi (*)"),
				new JLabel("Sân bay đến (*)"),
				new JLabel("Ngày - giờ (*)"),
				new JLabel("Thời gian bay (*)"),
				new JLabel("Số lượng ghế hạng 1 (*)"),
				new JLabel("Số lượng ghế hạng 2 (*)"),
				new JLabel("Giá vé hạng 1 (*)"),
				new JLabel("Giá vé hạng 2 (*)")));
		
		//Init datetime picker
		flightDateTimePicker = new DateTimePicker();
		flightDateTimePicker.setBackground(Color.WHITE);
		
		//Init airport combo boxes
		airportComboBoxes = new ArrayList<>();
		int numberOfAirportComboBoxes = 2;
		for (int i = 0; i < numberOfAirportComboBoxes; ++i) {
			airportComboBoxes.add(new JComboBox<>());
		}
			
		//Init numeric fields
		numericTextFields = new ArrayList<>();
		int numberOfNumericField = 5;
		for (int i = 0; i < numberOfNumericField; ++i) {
			numericTextFields.add(new JTextField());
		}
		
		table = new TransitionCRUDTableView();
		transitionForm = new AirportTransitionForm(this);
		
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
		
//		addTransitionButton.addActionListener(event -> {
//			transitionForm.clear();
//			transitionForm.open();
//		});
		
//		deleteTransitionButton.addActionListener(event -> {
//			int input = JOptionPane.showConfirmDialog(this,
//	        		"Bạn có chắc chắn muốn xóa ?",
//	        		"Xóa",
//	        		JOptionPane.YES_NO_OPTION);
//			
//			if (JOptionPane.YES_OPTION == input) {
//				List<Transition> original = table.getTransitions();
//				List<Transition> selected = table.getSelectedTransitions();
//				
//				//Delete the reference
//				for (Transition trans: selected) {
//					original.removeIf(iter -> iter == trans);
//				}
//				
//				System.out.println(original.size());
//				table.setTransitions(original);
//				table.update();
//				
//				//Success message
//				JOptionPane.showMessageDialog(null, "Đã xóa thành công.");
//			}
//		});
		
		transitionForm.getSubmitButton().addActionListener((event) -> {
			if (!transitionForm.areThereAnyEmptyStarField()) {
				Transition transition = transitionForm.submit();
				
				//There is no error
				if (null != transition) {
					if (!table.getTransitions().contains(transition)) {
						table.addTransition(transition);
					} else {
						int index = table.getTransitions().indexOf(transition);
						table.getTransitions().set(index, transition);
					}
					
					table.update();
					transitionForm.close();
				}
			} else {
				transitionForm.setError(AirportTransitionForm.EMPTY_STAR_FIELD_ERROR);
			}
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		
		for (int i = 0; i<labels.size(); ++i) {
			String metaLayout = "4, " + (i+2)*2 + ", right, default";
			centerPanel.add(labels.get(i), metaLayout);
		}
		
		centerPanel.add(warningText, "6, 2, center, default");
		int offset = 2;
		for (int i = 0; i < airportComboBoxes.size(); ++i) {
			centerPanel.add(airportComboBoxes.get(i), "6, " + (i + offset) * 2 + ", fill, default");
		}
		offset += airportComboBoxes.size();
		centerPanel.add(flightDateTimePicker, "6, " + offset * 2 + ", fill, default");
		++offset;
		for (int i = 0; i < numericTextFields.size(); ++i) {
			centerPanel.add(numericTextFields.get(i), "6, " + (i + offset) * 2 + ", fill, default");
		}
		
		
		//Footer setup
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new BorderLayout());
		JPanel controlButtonPanel = new JPanel();
		controlButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		controlButtonPanel.setBackground(Color.WHITE);
		controlButtonPanel.add(addTransitionButton);
		controlButtonPanel.add(deleteTransitionButton);
		JPanel confirmButtonPanel = new JPanel();
		confirmButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		confirmButtonPanel.setBackground(Color.WHITE);
		confirmButtonPanel.add(okButton);
		confirmButtonPanel.add(cancelButton);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(200, 100));
		footerPanel.add(controlButtonPanel, BorderLayout.NORTH);
		footerPanel.add(tableScroll, BorderLayout.CENTER);
		footerPanel.add(confirmButtonPanel, BorderLayout.SOUTH);
				
	}
	
	public void init() {
		initComponents();
		setLayout();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		setContentPane(contentPane);
		
	}
	
	private void initModel() {
		model = new Flight();
		model.setTransitions(new ArrayList<>());
		model.setTickets(new TreeSet<>());
		model.setDetail(new FlightDetail());
		model.setDateTime(null);
		model.setDepartureAirport(new Airport());
		model.setArrivalAirport(new Airport());
		
	}
	

	public FlightForm(Flight model, JFrame owner) {
		super(owner, true);
		init();
		setModel(model);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
	}
	
	public FlightForm(JFrame owner) {
		super(owner, true);
		init();
		initModel();
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
	}
	
	
	public JButton getOkButton() {return okButton;}
	public JDialog setModel(Flight model) {
		this.model = model;
		
		airportComboBoxes.get(0).setSelectedItem(model.getDepartureAirport());
		airportComboBoxes.get(1).setSelectedItem(model.getArrivalAirport());
		
		flightDateTimePicker.setDateTimePermissive(model.getDateTime());
		
		FlightDetail detail = model.getDetail();
		numericTextFields.get(0).setText(detail.getFlightTime().toString());
		numericTextFields.get(1).setText(detail.getNumberOfFirstClassSeat().toString());
		numericTextFields.get(2).setText(detail.getNumberOfSecondClassSeat().toString());
		numericTextFields.get(3).setText(detail.getPriceOfFirstClassSeat().toString());
		numericTextFields.get(4).setText(detail.getPriceOfSecondClassSeat().toString());
	
		table.setTransitions(new ArrayList<>(model.getTransitions()));
		table.update();
		
		return this;
	}
	public FlightForm setAirports(List<Airport> airports) {
		for (JComboBox<Airport> cb: airportComboBoxes) {
			cb.removeAllItems();
			airports.forEach(airport -> cb.addItem(airport));
		}
		
		return this;
	}
	
	public Flight submit() {
		
		model.setDepartureAirport((Airport) airportComboBoxes.get(0).getSelectedItem());
		model.setArrivalAirport((Airport) airportComboBoxes.get(1).getSelectedItem());
		
		model.setDateTime(flightDateTimePicker.getDateTimePermissive());
		
		FlightDetail detail = model.getDetail();
		detail.setFlightTime(Integer.parseInt(numericTextFields.get(0).getText()));
		detail.setNumberOfFirstClassSeat(Integer.parseInt(numericTextFields.get(1).getText()));
		detail.setNumberOfSecondClassSeat(Integer.parseInt(numericTextFields.get(2).getText()));
		detail.setPriceOfFirstClassSeat(Integer.parseInt(numericTextFields.get(3).getText()));
		detail.setPriceOfSecondClassSeat(Integer.parseInt(numericTextFields.get(4).getText()));
		
		List<Transition> transitions = table.getTransitions();
		model.setTransitions(transitions);
		
		return model;
	}
	
	public JButton getCancelButton() {return cancelButton;}
	public AirportTransitionForm getTransitionForm() {return transitionForm;}
	public JButton getAddTransitionButton() {return addTransitionButton;}
	public JButton getDeleteTransitionButton() {return deleteTransitionButton;}
	public TransitionCRUDTableView getTable() {return table;}
	
	public JButton getSubmitButton() {
		return okButton;
	}
	
	public boolean areThereAnyEmptyStarField() {
		
		for (JComboBox<Airport> cb: airportComboBoxes) {
			Airport airport = (Airport) cb.getSelectedItem();
			if (null == airport) {
				return true;
			}
		}
		
		LocalDateTime datetime = flightDateTimePicker.getDateTimePermissive();
		if (null == datetime) {
			return true;
		}
		
		for (JTextField field: numericTextFields) {
			String value = field.getText().trim();
			if (null == value || value.equals("")) {
				return true;
			}
		}
		
		return false;
	}

	public boolean areTheseAirportMatch() {
		
		Airport departure = (Airport) airportComboBoxes.get(0).getSelectedItem();
		Airport arrival = (Airport) airportComboBoxes.get(1).getSelectedItem();
		
		if (departure.getId().equals(arrival.getId())) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void clear() {
		airportComboBoxes.get(0).setSelectedIndex(-1);
		airportComboBoxes.get(1).setSelectedIndex(-1);
		flightDateTimePicker.setDateTimePermissive(LocalDateTime.now());
		for (JTextField field: numericTextFields) {
			field.setText("0");
		}
		
		table.clearData();
		transitionForm.clear();
	}

	@Override
	public void close() {
		initModel();
		setVisible(false);
	}
	
	public void open() {
		clear();
		setVisible(true);
	}
}
