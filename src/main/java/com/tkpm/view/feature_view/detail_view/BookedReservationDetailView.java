package com.tkpm.view.feature_view.detail_view;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.tkpm.entities.Flight;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;

@SuppressWarnings("serial")
public class BookedReservationDetailView extends BaseReadOnlyDetailView {
	
	public static final int BOOK_BUTTON_INDEX = 0;
	
	private JButton bookButton;
	
	private static final String[] FIELD_NAMES = {
			"Mã chuyến bay",
			"Thời gian",	
			"Sân bay đi",
			"Sân bay đến",
			"Ngày đặt vé",
			"Họ và tên",
			"CMND/CCCD",
			"Số điện thoại"
	};

	
	public BookedReservationDetailView() {
		super("Vé đã đặt",FIELD_NAMES);
	}
	
	public JButton getBookButton() {
		return bookButton;
	}
	
	@Override
	public void setDataToDetailPanel(Object object) {
		
		//Set empty field
		if (null == object) {
			for (JLabel label: attributeData) {
				label.setText("");
			}
			return;
		}
		
		Reservation reservation = (Reservation) object;
		Ticket ticket = reservation.getTicket();
		Flight flight = ticket.getFlight();
		
		String departureAirport = (null == flight.getDepartureAirport()?"":flight.getDepartureAirport().getName());
		String arrivalAirport =  (null == flight.getArrivalAirport()?"":flight.getArrivalAirport().getName());
		
		attributeData.get(0).setText(ticket.getId().toString());
		attributeData.get(1).setText(flight.getDateTime().toString());		
		attributeData.get(2).setText(departureAirport);	
		attributeData.get(3).setText(arrivalAirport);	
		attributeData.get(4).setText(reservation.getBookDate().toString());
		attributeData.get(5).setText(ticket.getName());	
		attributeData.get(6).setText(ticket.getIdentityCode());	
		attributeData.get(7).setText(ticket.getPhoneNumber());
		
	}
	
}