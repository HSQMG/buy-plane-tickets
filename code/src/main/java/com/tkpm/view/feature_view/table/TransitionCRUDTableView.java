package com.tkpm.view.feature_view.table;

import java.util.ArrayList;
import java.util.Arrays;
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

public class TransitionCRUDTableView extends JTable {

	protected DefaultTableModel tableModel;
	protected List<Transition> transitions;
	
	protected List<JButton> actionButtons;
	
	public static final String[] COLUMN_NAMES = {
		"Chọn", "STT", "Sân bay trung gian", "Thời gian dừng", "Ghi chú", "Chỉnh sửa"
	};
	
	public static final int SELECT_COLUMN_INDEX = 0;
	public static final int COLUMN_INDEX = 1;
	public static final int TRANSITION_COLUMN_INDEX = 2;
	public static final int TRANSITION_TIME_COLUMN_INDEX = 3;
	public static final int NOTE_COLUMN_INDEX = 4;
	public static final int UPDATE_COLUMN_INDEX = 5;
	
	public static final int UPDATE_BUTTON_INDEX = 0;
	
	protected void setupModelTable() {
		//Make uneditable table
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {			
					
			@Override
			public boolean isCellEditable(int row, int column) {				
						
				//Make Select and update button cell editable
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
				case SELECT_COLUMN_INDEX:
					clazz = Boolean.class;
					break;
				case COLUMN_INDEX:
					clazz = Integer.class;
					break;
				case TRANSITION_TIME_COLUMN_INDEX:
					clazz = Integer.class;
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
	
	protected void initDetailButton() {
		
		//Setup the action buttons for "Action" column
		actionButtons = new ArrayList<>(Arrays.asList(
				new JButton("Cập nhật")));
		
		int offset = UPDATE_COLUMN_INDEX;
		for (int i = 0; i < actionButtons.size(); ++i) {
			TableColumn column = this.getColumn(COLUMN_NAMES[offset + i]);
			column.setCellRenderer(new ButtonRenderer(actionButtons.get(i).getText()));
			column.setCellEditor(new ButtonEditor(actionButtons.get(i)));
		}
	}
		
	public TransitionCRUDTableView() {
		transitions = new ArrayList<>();
		setupModelTable();
		initDetailButton();
		setAlignmentContent();
		getTableHeader().setReorderingAllowed(false);
	}
	
	public TransitionCRUDTableView clearData() {
		
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
	public TransitionCRUDTableView update() {
		
		tableModel.setRowCount(0);
		int size = transitions.size();
		for (int index = 0; index < size; ++index) {
			
			Transition transition = transitions.get(index);
			Airport airport = transition.getAirport();
			String airportName = (null == airport?"":airport.getName());
			Object[] row = {
					false,
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
	
	public List<Transition> getSelectedTransitions() {
		
		//Full scan the table to get all the selected objects
		List<Transition> result = new ArrayList<>();
		int size = tableModel.getRowCount();
		for (int i = 0; i < size; ++i) {
			Boolean isSelected = (Boolean) tableModel.getValueAt(i, SELECT_COLUMN_INDEX);
			if (null != isSelected && isSelected) {
				result.add(transitions.get(i));
			}
			
		}
		
		return result;
	}
//	
//	public void deleteSelectedTransitions() {
//		int size = tableModel.getRowCount();
//		for (int i = 0; i < size; ++i) {
//			Boolean isSelected = (Boolean) tableModel.getValueAt(i, SELECT_COLUMN_INDEX);
//			if (null != isSelected && isSelected) {
//				tableModel.removeRow(i);
//			}
//			
//		}
//	}
//	
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public List<JButton> getActionButtons() {
		return actionButtons;
	}
	
	public void setAlignmentContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		
		setRowHeight(30);
	}
}
