package com.tkpm.view.feature_view.tabbed_controller_view;

import java.util.List;

import javax.swing.JPanel;

import com.tkpm.view.action.ChangeTabListener;
import com.tkpm.view.feature_view.detail_view.AirportCRUDDetailView;
import com.tkpm.view.feature_view.detail_view.FlightCRUDDetailView;
import com.tkpm.view.feature_view.header_view.BaseHeader;
import com.tkpm.view.feature_view.table.AirportCRUDTableView;
import com.tkpm.view.feature_view.table.FlightCRUDTableView;


public class FlightManagerTabbedControllerView extends BaseTabbedControllerView {

	//Tables
	private AirportCRUDTableView airportTableView;
	private FlightCRUDTableView flightCRUDTableView;
	
	//Detail views
	private AirportCRUDDetailView airportDetailView;
	private FlightCRUDDetailView flightCRUDDetailView;
	
	public FlightManagerTabbedControllerView(List<JPanel> backgroundParts) {
		super(backgroundParts);
		
		//Init for airport CRUD feature
		airportTableView = new AirportCRUDTableView();
		airportDetailView = new AirportCRUDDetailView();
		
		//Init for flight CRUD feature
		flightCRUDTableView = new FlightCRUDTableView();
		flightCRUDDetailView = new FlightCRUDDetailView();
		
		//Add screen for airport CRUD tab
		this.addCenterAsTable(airportTableView, "Sân bay");
		this.addDetail(airportDetailView);
		this.addHeader(new BaseHeader());	//dummy header
		
		//Add screen for flight CRUD tab
		this.addCenterAsTable(flightCRUDTableView, "Chuyến bay");
		this.addDetail(flightCRUDDetailView);
		this.addHeader(new BaseHeader());	//dummy header
		
		//Change detail panel and header panel while change tab
		tabbedPanel.addMouseListener(new ChangeTabListener(this, backgroundParts));
			
		initOption();
	}

	public AirportCRUDTableView getAirportCRUDTableView() {return airportTableView;}
	public AirportCRUDDetailView getAirporCRUDDetailView() {return airportDetailView;}

	public FlightCRUDTableView getFlightCRUDTableView() {return flightCRUDTableView;}
	public FlightCRUDDetailView getFlightCRUDDetailView() {return flightCRUDDetailView;}
	
}
