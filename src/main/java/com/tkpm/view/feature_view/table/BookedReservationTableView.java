package com.tkpm.view.feature_view.table;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.tkpm.entities.Flight;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;
import com.tkpm.view.widget.ButtonEditor;
import com.tkpm.view.widget.ButtonRenderer;

public class BookedReservationTableView extends JTable {

	protected DefaultTableModel tableModel;
	protected List<Reservation> reservations;
	
	protected JButton cancelTicketButton;
	
	public static final String[] COLUMN_NAMES = {
		"STT", "Mã chuyến bay", "Sân bay đi", "Thời gian", "Họ và tên khách hàng" , "Thao tác"
	};
	
	//public static final int SELECT_COLUMN_INDEX = 0;
	public static final int COLUMN_INDEX = 0;
	public static final int FLIGHT_ID_COLUMN_INDEX = 1;
	public static final int DEPARTURE_AIRPORT_COLUMN_INDEX = 2;
	public static final int DATETIME_COLUMN_INDEX = 3;
	public static final int NAME_COLUMN_INDEX = 4;
	public static final int CANCEL_TICKET_COLUMN_INDEX = 5;
	
	protected void setupModelTable() {
		//Make uneditable table
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {			
					
			@Override
			public boolean isCellEditable(int row, int column) {				
						
				//Make Detail button cell editable
				if (CANCEL_TICKET_COLUMN_INDEX == column) {
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
				case CANCEL_TICKET_COLUMN_INDEX:
					clazz = Boolean.class;
					break;
		      }
		      return clazz;
		    }
			
		};
				
		//Enable table model
		this.setModel(tableModel);
		
		
	}
	
	protected void initCancelTicketButton() {
		
		//Setup for the Update button in cell
		String cancelTicketButtonName = "Hủy vé";
		cancelTicketButton = new JButton(cancelTicketButtonName);
		TableColumn cancelTicketColumn = this.getColumn(COLUMN_NAMES[CANCEL_TICKET_COLUMN_INDEX]);
		cancelTicketColumn.setCellRenderer(new ButtonRenderer(cancelTicketButtonName));
		cancelTicketColumn.setCellEditor(new ButtonEditor(cancelTicketButton));			
	
	}
		
	public BookedReservationTableView() {
		setupModelTable();
		initCancelTicketButton();
		setAlignmentContent();
	}
	
	public BookedReservationTableView clearData() {
		
		//Clear the model
		tableModel.setRowCount(0);
		
		return this;
	}
	
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	//Show all reservation + corresponding ticket's  data to the table
	public BookedReservationTableView update() {
		
		clearData();
		int size = reservations.size();
		for (int index = 0; index < size; ++index) {
			
			Reservation reservation = reservations.get(index);
			Ticket ticket = reservation.getTicket();
			Flight flight = ticket.getFlight();
			String departureAirport = 
					(null == flight.getDepartureAirport()?"":flight.getDepartureAirport().getName());
			
			Object[] row = {
					index + 1, 
					flight.getId(), 
					departureAirport,
					flight.getDateTime().toString(),
					ticket.getName(),
			};
			tableModel.addRow(row);		
		}
		return this;
	}
	
	public Reservation getSelectedReservation() {
		int index = this.getSelectedRow();
		if (-1 == index) return null;
		return reservations.get(index);
	}
	
	public JButton getCancelTicketButton() {
		return cancelTicketButton;
	}
	
	public void setAlignmentContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		
		setRowHeight(30);
	}
}
