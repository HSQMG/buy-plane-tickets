package com.tkpm.view.feature_view.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.tkpm.entities.Airport;
import com.tkpm.entities.Flight;
import com.tkpm.view.widget.ButtonEditor;
import com.tkpm.view.widget.ButtonRenderer;

public class FlightCRUDTableView extends JTable {

	protected DefaultTableModel tableModel;
	protected List<Flight> flights;
	
	protected List<JButton> actionButtons;
	
//	public static final String[] COLUMN_NAMES = {
//		"Chọn", "STT", "Mã chuyến bay", "Thời gian", "Chi tiết", "Chỉnh sửa"
//	};
	
	public static final String[] COLUMN_NAMES = {
			"Chọn", "STT", "Mã chuyến bay", "Thời gian", "Chỉnh sửa"
	};
	
	public static final int SELECT_COLUMN_INDEX = 0;
	public static final int COLUMN_INDEX = 1;
	public static final int FLIGHT_ID_COLUMN_INDEX = 2;
	public static final int DATETIME_COLUMN_INDEX = 3;
	//public static final int DETAIL_COLUMN_INDEX = 4;
	public static final int UPDATE_COLUMN_INDEX = 4;
	
	//public static final int DETAIL_BUTTON_INDEX = 0;
	public static final int UPDATE_BUTTON_INDEX = 0;
	
	protected void setupModelTable() {
		//Make uneditable table
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {			
					
			@Override
			public boolean isCellEditable(int row, int column) {				
						
				//Make Detail button cell editable
				if (
					SELECT_COLUMN_INDEX == column ||
					UPDATE_COLUMN_INDEX == column) {
					return true;
				}
						
				//all another cells false
				return false;
			}
			
			@Override
		    public Class<?> getColumnClass(int columnIndex) {
				Class clazz = String.class;
				switch (columnIndex) {
				case SELECT_COLUMN_INDEX:
					clazz = Boolean.class;
					break;
				case COLUMN_INDEX:
					clazz = Integer.class;
					break;
				case FLIGHT_ID_COLUMN_INDEX:
					clazz = Integer.class;
					break;
//				case DETAIL_COLUMN_INDEX:
//					clazz = Boolean.class;
//					break;
				case UPDATE_COLUMN_INDEX:
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
		
		//Setup the action buttons for "Action" column
		actionButtons = new ArrayList<>(Arrays.asList(
				//new JButton("Chi tiết"),
				new JButton("Cập nhật")));
		
		//int offset = DETAIL_COLUMN_INDEX;
		int offset = UPDATE_COLUMN_INDEX;
		for (int i = 0; i < actionButtons.size(); ++i) {
			TableColumn column = this.getColumn(COLUMN_NAMES[offset + i]);
			column.setCellRenderer(new ButtonRenderer(actionButtons.get(i).getText()));
			column.setCellEditor(new ButtonEditor(actionButtons.get(i)));
		}
	}
		
	public FlightCRUDTableView() {
		setupModelTable();
		initDetailButton();
		
		setAlignmentContent();
		getTableHeader().setReorderingAllowed(false);
	}
	
	public FlightCRUDTableView clearData() {
		
		//Clear the model
		tableModel.setRowCount(0);
		
		return this;
	}
	
	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
	
	//Show all flight in flights to the table
	public FlightCRUDTableView update() {
		
		clearData();
		int size = flights.size();
		for (int index = 0; index < size; ++index) {
			
			Flight flight = flights.get(index);
			Object[] row = {
					false,
					index + 1, 
					flight.getId(), 
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
	
	public List<Flight> getSelectedFlights() {
		
		//Full scan the table to get all the selected objects
		List<Flight> result = new ArrayList<>();
		int size = tableModel.getRowCount();
		for (int i = 0; i < size; ++i) {
			Boolean isSelected = (Boolean) tableModel.getValueAt(i, SELECT_COLUMN_INDEX);
			if (null != isSelected && isSelected) {
				result.add(flights.get(i));
			}
			
		}
		
		return result;
	}
	
	public List<JButton> getActionButtons() {
		return actionButtons;
	}
	
	public void setAlignmentContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		
		setRowHeight(30);
	}
	
}
