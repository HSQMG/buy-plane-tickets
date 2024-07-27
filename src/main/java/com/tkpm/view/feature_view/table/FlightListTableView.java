package com.tkpm.view.feature_view.table;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.tkpm.entities.Airport;
import com.tkpm.entities.Flight;
import com.tkpm.view.widget.ButtonEditor;
import com.tkpm.view.widget.ButtonRenderer;

public class FlightListTableView extends JTable {

	protected DefaultTableModel tableModel;
	protected List<Flight> flights;
	
	protected JButton detailButton;
	
	public static final String[] COLUMN_NAMES = {
		"STT", "Mã chuyến bay", "Sân bay đi", "Sân bay đến", "Thời gian", "Thao tác"
	};
	
	//public static final int SELECT_COLUMN_INDEX = 0;
	public static final int COLUMN_INDEX = 0;
	public static final int FLIGHT_ID_COLUMN_INDEX = 1;
	public static final int DEPARTURE_AIRPORT_COLUMN_INDEX = 2;
	public static final int ARRIVAL_AIRPORT_COLUMN_INDEX = 3;
	public static final int DATETIME_COLUMN_INDEX = 4;
	public static final int DETAIL_COLUMN_INDEX = 5;
	
	protected void setupModelTable() {
		//Make uneditable table
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {			
					
			@Override
			public boolean isCellEditable(int row, int column) {				
						
				//Make Detail button cell editable
				if (DETAIL_COLUMN_INDEX == column) {
					return true;
				}
						
				//all another cells false
				return false;
			}
			
			@Override
		    public Class<?> getColumnClass(int columnIndex) {
				Class clazz = String.class;
				switch (columnIndex) {
				case COLUMN_INDEX:
					clazz = Integer.class;
					break;
				case FLIGHT_ID_COLUMN_INDEX:
					clazz = Integer.class;
					break;
				case DETAIL_COLUMN_INDEX:
					clazz = Boolean.class;
					break;
		      }
		      return clazz;
		    }
			
		};
				
		//Enable table model
		this.setModel(tableModel);
		
		
	}
	
	protected void initDetailButton() {
		
		//Setup for the Update button in cell
		String detailButtonName = "Chi tiết";
		detailButton = new JButton(detailButtonName);
		TableColumn detailColumn = this.getColumn(COLUMN_NAMES[DETAIL_COLUMN_INDEX]);
		detailColumn.setCellRenderer(new ButtonRenderer(detailButtonName));
		detailColumn.setCellEditor(new ButtonEditor(detailButton));			
	
	}
		
	public FlightListTableView() {
		setupModelTable();
		initDetailButton();
		
		setAlignmentContent();
		getTableHeader().setReorderingAllowed(false);
	}
	
	public FlightListTableView clearData() {
		
		//Clear the model
		tableModel.setRowCount(0);
		
		return this;
	}
	
	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
	
	//Show all flight in flights to the table
	public FlightListTableView update() {
		
		clearData();
		int size = flights.size();
		for (int index = 0; index < size; ++index) {
			
			
			Flight flight = flights.get(index);
			String departureAirport = (null == flight.getDepartureAirport()?"":flight.getDepartureAirport().getName());
			String arrivalAirport =  (null == flight.getArrivalAirport()?"":flight.getArrivalAirport().getName());
			
			Object[] row = {
					index + 1, 
					flight.getId(), 
					departureAirport,
					arrivalAirport,
					flight.getDateTime()};
			tableModel.addRow(row);		
		}
		return this;
	}
	
	public Flight getSelectedFlight() {
		int index = this.getSelectedRow();
		if (-1 == index) return null;
		return flights.get(index);
	}
	
	public JButton getDetailButton() {
		return detailButton;
	}
	
	public void setAlignmentContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		
		setRowHeight(30);
	}
}
