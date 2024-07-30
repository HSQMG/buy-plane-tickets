package com.tkpm.view.feature_view.detail_view;

import com.tkpm.entities.Airport;

@SuppressWarnings("serial")
public class AirportCRUDDetailView extends CRUDDetailView {
	
	public static final String TITLE = "Sân bay";
	
	private static final String[] FIELD_NAMES = {
			"Mã sân bay",
			"Tên sân bay",
	};
	
	public AirportCRUDDetailView() {
		super(TITLE, FIELD_NAMES);
	}
	
	@Override
	public void setDataToDetailPanel(Object object) {
		super.setDataToDetailPanel(object);
		if (null == object) {return;}
		Airport airport = (Airport) object;
		attributeData.get(0).setText(airport.getId().toString());
		attributeData.get(1).setText(airport.getName());		
	}
}