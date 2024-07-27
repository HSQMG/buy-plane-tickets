package com.tkpm.view.feature_view.header_view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.tkpm.entities.Airport;

public class SearchFlightPanel extends JPanel{

	private List<JLabel> labels;
	private List<JComboBox<Airport>> airportComboBoxes;
	private List<DatePicker> datePickers;
	
	private JButton searchButton;
	
	private void initComponents() {
		labels = new ArrayList<>(Arrays.asList(
					new JLabel("Sân bay đi"),
					new JLabel("Sân bay đến"),
					new JLabel("Ngày bắt đầu"),
					new JLabel("Ngày kết thúc")
				));
		
		airportComboBoxes = new ArrayList<>(Arrays.asList(
					new JComboBox<>(),
					new JComboBox<>()
				));
		
		datePickers = new ArrayList<>(Arrays.asList(
				new DatePicker(),
				new DatePicker()
			));
	
		searchButton = new JButton("Tìm kiếm");
	}
		
	public void setLayout() {
		
		setLayout(new BorderLayout());
		
		//Search button panel
		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new BoxLayout(searchButtonPanel, BoxLayout.X_AXIS));
		searchButtonPanel.setBackground(Color.WHITE);
		searchButtonPanel.add(searchButton);
		searchButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		//Search criteria panel
		JPanel searchCriteriaPanel = new JPanel();
		searchCriteriaPanel.setLayout(new GridLayout(2, 2, 5, 5));
		searchCriteriaPanel.setBackground(Color.WHITE);
		int size = labels.size();
		List<JPanel> panels = new ArrayList<>();
		for (int i = 0; i < size; ++i) {
			panels.add(new JPanel());
		}
		
		panels.forEach(panel -> {
			//panel.setLayout(new FlowLayout());
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			panel.setBorder(new EmptyBorder(10, 30, 10, 30));
			panel.setBackground(Color.WHITE);
			searchCriteriaPanel.add(panel);
		});
		
		//Add labels
		for (int i = 0; i < size; ++i) {
			panels.get(i).add(labels.get(i));
		}
		
		//Add combo boxes
		int offset = 0;
		for (int i = 0; i < airportComboBoxes.size(); ++i) {
			airportComboBoxes.get(i).setPreferredSize(datePickers.get(i).getPreferredSize());
			panels.get(i).add(airportComboBoxes.get(i));
		}
		
		//Add date pickers
		offset += airportComboBoxes.size();
		for (int i = 0; i < datePickers.size(); ++i) {
			panels.get(offset + i).add(datePickers.get(i));
		}
		offset += datePickers.size();
		
		//Add 2 panels together
		add(searchCriteriaPanel, BorderLayout.CENTER);
		add(searchButtonPanel, BorderLayout.EAST);
		
	}
	
	public SearchFlightPanel() {
		initComponents();
		setLayout();
	}

	//Getters
	public List<JComboBox<Airport>> getAirportComboBoxes() {return airportComboBoxes;}
	public List<DatePicker> getDatePickers() {return datePickers;}
	public JButton getSearchButton() {return searchButton;}
}
