package com.tkpm.view.feature_view.table;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.tkpm.entities.Airport;
import com.tkpm.entities.Flight;
import com.tkpm.view.widget.ButtonEditor;
import com.tkpm.view.widget.ButtonRenderer;

public class AirportCRUDTableView extends JTable {

	protected DefaultTableModel tableModel;
	protected List<Airport> airports;
	
	protected JButton updateButton;
	
	public static final String[] COLUMN_NAMES = {
		"Chọn", "STT", "Mã sân bay", "Tên sân bay", "Thao tác"
	};
	
	public static final int SELECT_COLUMN_INDEX = 0;
	public static final int COLUMN_INDEX = 1;
	public static final int AIRPORT_ID_INDEX = 2;
	public static final int NAME_COLUMN_INDEX = 3;
	public static final int UPDATE_COLUMN_INDEX = 4;
	
	protected void setupModelTable() {
		//Make uneditable table
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {			
					
			@Override
			public boolean isCellEditable(int row, int column) {				
						
				//Make Select checkbox and Update button cell editable
				if (SELECT_COLUMN_INDEX == column ||
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
				case COLUMN_INDEX:
					clazz = Integer.class;
					break;
				case AIRPORT_ID_INDEX:
					clazz = Integer.class;
					break;
				case SELECT_COLUMN_INDEX:
					clazz = Boolean.class;
					break;
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
	
	protected void initUpdateButton() {
		
		//Setup for the Update button in cell
		String updateButtonName = "Cập nhật";
		updateButton = new JButton(updateButtonName);
		TableColumn updateColumn = this.getColumn(COLUMN_NAMES[UPDATE_COLUMN_INDEX]);
		updateColumn.setCellRenderer(new ButtonRenderer(updateButtonName));
		updateColumn.setCellEditor(new ButtonEditor(updateButton));		
	
	}
		
	public AirportCRUDTableView() {
		setupModelTable();
		initUpdateButton();
	
		setAlignmentContent();
		getTableHeader().setReorderingAllowed(false);
	}
	
	public AirportCRUDTableView clearData() {
		
		//Clear the model
		tableModel.setRowCount(0);
		
		return this;
	}
	
	public void setAirports(List<Airport> airports) {
		this.airports = airports;
	}
	
	//Show all airport in airports to the table
	public AirportCRUDTableView update() {
		
		clearData();
		int size = airports.size();
		for (int index = 0; index < size; ++index) {
			
			Airport airport = airports.get(index);
			Object[] row = {
					false,
					index + 1,
					airport.getId(),
					airport.getName()};
			
			tableModel.addRow(row);		
		}
		return this;
	}
	
	public Airport getSelectedAirport() {
		int index = this.getSelectedRow();
		if (-1 == index) return null;
		return airports.get(index);
	}
	
	public List<Airport> getSelectedAirports() {
		
		//Full scan the table to get all the selected objects
		List<Airport> result = new ArrayList<>();
		int size = tableModel.getRowCount();
		for (int i = 0; i < size; ++i) {
			Boolean isSelected = (Boolean) tableModel.getValueAt(i, SELECT_COLUMN_INDEX);
			if (null != isSelected && isSelected) {
				result.add(airports.get(i));
			}
			
		}
		
		return result;
	}
	
	public JButton getUpdateButton() {
		return updateButton;
	}
	
	public void setAlignmentContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		
		setRowHeight(30);
	}
}
