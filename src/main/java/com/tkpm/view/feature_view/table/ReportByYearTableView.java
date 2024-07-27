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

import com.tkpm.entities.Flight;
import com.tkpm.view.widget.ButtonEditor;
import com.tkpm.view.widget.ButtonRenderer;
import com.tkpm.wrapper.report.BaseReport;

public class ReportByYearTableView extends JTable {

	protected DefaultTableModel tableModel;
	protected List<BaseReport> reportModels;
	
	public static final String[] COLUMN_NAMES = {
		"Tháng", "Số chuyến bay", "Doanh thu", "Tỉ lệ"
	};
	
	//public static final int SELECT_COLUMN_INDEX = 0;
	public static final int MONTH_COLUMN_INDEX = 0;
	public static final int NUMBER_OF_FLIGHTS_COLUMN_INDEX = 1;
	public static final int TURNOVER_COLUMN_INDEX = 2;
	public static final int RATIO_COLUMN_INDEX = 3;
	
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
				Class clazz = Integer.class;
				switch (columnIndex) {
				
				case TURNOVER_COLUMN_INDEX:
					clazz = String.class;
					break;
				case RATIO_COLUMN_INDEX:
					clazz = String.class;
					break;
				
		      }
		      return clazz;
		    }
			
		};
				
		//Enable table model
		this.setModel(tableModel);
		
	}
		
	public ReportByYearTableView() {
		setupModelTable();
		setAlignmentContent();
		getTableHeader().setReorderingAllowed(false);
	}
	
	public ReportByYearTableView clearData() {
		
		//Clear the model
		tableModel.setRowCount(0);
		
		return this;
	}
	
	public void setReportModels(List<BaseReport> reportModels) {
		this.reportModels = reportModels;
	}
	
	//Show all flight in flights to the table
	public ReportByYearTableView update() {
		
		clearData();
		
		//precalculate turnover and total turnover
		int[] turnovers = new int[13];
		int totalTurnover = 0;
		int month = 0;
		for (BaseReport report: reportModels) {
			++month;
			turnovers[month] = report.getTurnover();
			totalTurnover += turnovers[month];
		}
		
		//Print data
		int size = reportModels.size();
		for (int index = 0; index < size; ++index) {
			
			BaseReport reportByMonth = reportModels.get(index);
			double ratio = 1;
			if (0 != totalTurnover) {
				ratio = (double)(turnovers[index + 1]/totalTurnover);
			}
			Object[] row = {
					index + 1, 
					reportByMonth.getWrappers().size(),
					new Integer(turnovers[index + 1]).toString() + " VND",
					new Double(ratio * 100).toString() + "%"};
			
			tableModel.addRow(row);		
		}
		return this;
	}

	public void setAlignmentContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		
		setRowHeight(30);
	}
}
