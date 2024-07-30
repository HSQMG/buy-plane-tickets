package com.tkpm.view.feature_view.detail_view;

import com.tkpm.entities.Flight;

@SuppressWarnings("serial")
public class FlightCRUDDetailView extends CRUDDetailView {
	
	private static final String[] FIELD_NAMES = {
			"Mã chuyến bay",	
			"Sân bay đi",
			"Sân bay đến",
			"Thời gian",
	};
	
	public static final String TITLE = "Chuyến bay";
	
	public FlightCRUDDetailView() {
		super(TITLE, FIELD_NAMES);
	}
	
	@Override
	public void setDataToDetailPanel(Object object) {
		super.setDataToDetailPanel(object);
		if (null == object) {return;}
		Flight flight = (Flight) object;
		String departureAirport = (null == flight.getDepartureAirport()?"":flight.getDepartureAirport().getName());
		String arrivalAirport =  (null == flight.getArrivalAirport()?"":flight.getArrivalAirport().getName());
		
		attributeData.get(0).setText(flight.getId().toString());
		attributeData.get(1).setText(departureAirport);		
		attributeData.get(2).setText(arrivalAirport);	
		attributeData.get(3).setText(flight.getDateTime().toString());	
	}

}