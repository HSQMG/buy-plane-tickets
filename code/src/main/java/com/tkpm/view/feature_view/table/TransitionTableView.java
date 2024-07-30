package com.tkpm.view.feature_view.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.tkpm.entities.Airport;
import com.tkpm.entities.Transition;
import com.tkpm.view.widget.ButtonEditor;
import com.tkpm.view.widget.ButtonRenderer;

public class TransitionTableView extends JTable {

	protected DefaultTableModel tableModel;
	protected List<Transition> transitions;
	
	//protected List<JButton> actionButtons;
	
	public static final String[] COLUMN_NAMES = {
		"STT", "Sân bay trung gian", "Thời gian dừng", "Ghi chú"
	};
	
	//public static final int SELECT_COLUMN_INDEX = 0;
	public static final int COLUMN_INDEX = 1;
	public static final int TRANSITION_COLUMN_INDEX = 2;
	public static final int TRANSITION_TIME_COLUMN_INDEX = 3;
	public static final int NOTE_COLUMN_INDEX = 4;
	//public static final int UPDATE_COLUMN_INDEX = 5;
	
	//public static final int UPDATE_BUTTON_INDEX = 0;
	
	protected void setupModelTable() {
		//Make uneditable table
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {			
					
			@Override
			public boolean isCellEditable(int row, int column) {				
						
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
				case TRANSITION_TIME_COLUMN_INDEX:
					clazz = Integer.class;
					break;
		      }
		      return clazz;
		    }
			
		};
				
		//Enable table model
		this.setModel(tableModel);
		
		
	}
	
		
	public TransitionTableView() {
		transitions = new ArrayList<>();
		setupModelTable();
		setAlignmentContent();
	}
	
	public TransitionTableView clearData() {
		
		//Clear the model
		tableModel.setRowCount(0);
		transitions.clear();
		
		return this;
	}
	
	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}
	
	public void addTransition(Transition transition) {
		this.transitions.add(transition);
	}
	
	//Show all object to the table
	public TransitionTableView update() {
		
		tableModel.setRowCount(0);
		int size = transitions.size();
		for (int index = 0; index < size; ++index) {
			
			Transition transition = transitions.get(index);
			Airport airport = transition.getAirport();
			String airportName = (null == airport?"":airport.getName());
			Object[] row = {
					index + 1, 
					airportName,
					transition.getTransitionTime(), 
					transition.getNote()};
			tableModel.addRow(row);		
		}
		
		return this;
	}
	
	public Transition getSelectedTransition() {
		int index = this.getSelectedRow();
		if (-1 == index) return null;
		return transitions.get(index);
	}
	
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public void setAlignmentContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		
		setRowHeight(30);
	}
	
}
