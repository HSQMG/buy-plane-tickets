package com.tkpm.view.feature_view.tabbed_controller_view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.tkpm.view.action.ChangeTabListener;
import com.tkpm.view.feature_view.detail_view.BookedReservationDetailView;
import com.tkpm.view.feature_view.detail_view.FlightListDetailView;
import com.tkpm.view.feature_view.header_view.BaseHeader;
import com.tkpm.view.feature_view.header_view.components.SearchFlightPanel;
import com.tkpm.view.feature_view.table.BookedReservationTableView;
import com.tkpm.view.feature_view.table.FlightListTableView;


public class FlightTabbedControllerView extends BaseTabbedControllerView {

	//constants
	public static final int FLIGHT_LIST_HEADER_INDEX = 0;
	
	//Headers;
	private List<BaseHeader> headers;
	
	//Tables
	private FlightListTableView flightListTableView;
	private BookedReservationTableView bookedReservationTableView;
	
	//Detail views
	private FlightListDetailView flightListDetailView;
	private BookedReservationDetailView bookedReservationDetailView;
	
	public FlightTabbedControllerView(List<JPanel> backgroundParts) {
		super(backgroundParts);
		
		//Init headers
		headers = new ArrayList<>();
		
		//Init for flight list tabbed
		flightListTableView = new FlightListTableView();
		flightListDetailView = new FlightListDetailView();
		SearchFlightPanel searchPanel = new SearchFlightPanel();
		BaseHeader flightListHeader = new BaseHeader(searchPanel);
		headers.add(flightListHeader);
		
		//Init for booked reservation list tabbed
		bookedReservationTableView = new BookedReservationTableView();
		bookedReservationDetailView = new BookedReservationDetailView();
		BaseHeader bookedReservationHeader = new BaseHeader();
		headers.add(bookedReservationHeader);
		
		//Add screen for flight list
		this.addCenterAsTable(flightListTableView, "Danh sách chuyến bay");
		this.addDetail(flightListDetailView);
		this.addHeader(flightListHeader);	//dummy header
		
		//Change detail panel and header panel while change tab
		this.addCenterAsTable(bookedReservationTableView , "Vé đã đặt");
		this.addDetail(bookedReservationDetailView);
		this.addHeader(bookedReservationHeader);	//dummy header
		
		//Change detail panel and header panel while change tab
		tabbedPanel.addMouseListener(new ChangeTabListener(this, backgroundParts));			
		
		initOption();
	}

	public FlightListTableView getFlightListTableView() {return flightListTableView;}
	public FlightListDetailView getFlightListDetailView() {return flightListDetailView;}
	public BookedReservationTableView getBookedReservationTableView() {return bookedReservationTableView;};
	public BookedReservationDetailView getBookedReservationDetailView() {return bookedReservationDetailView;}
	public List<BaseHeader> getHeaders() {return headers;}
}
