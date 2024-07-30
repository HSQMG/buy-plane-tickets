package com.tkpm.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.lgooddatepicker.components.DatePicker;
import com.tkpm.entities.Airport;
import com.tkpm.entities.BaseAccount;
import com.tkpm.entities.Flight;
import com.tkpm.entities.FlightDetail;
import com.tkpm.entities.Reservation;
import com.tkpm.entities.Ticket;
import com.tkpm.entities.TicketClass;
import com.tkpm.entities.Transition;
import com.tkpm.service.AirportService;
import com.tkpm.service.FlightService;
import com.tkpm.service.PolicyService;
import com.tkpm.service.ReservationService;
import com.tkpm.service.TicketClassService;
import com.tkpm.service.TicketService;
import com.tkpm.service.TransitionAirportService;
import com.tkpm.view.feature_view.FlightFeatureView;
import com.tkpm.view.feature_view.detail_view.BookedReservationDetailView;
import com.tkpm.view.feature_view.detail_view.FlightListDetailView;
import com.tkpm.view.feature_view.header_view.BaseHeader;
import com.tkpm.view.feature_view.header_view.components.SearchFlightPanel;
import com.tkpm.view.feature_view.tabbed_controller_view.FlightTabbedControllerView;
import com.tkpm.view.feature_view.table.BookedReservationTableView;
import com.tkpm.view.feature_view.table.FlightListTableView;
import com.tkpm.view.frame.BaseMainFrame;
import com.tkpm.view.frame.CustomerMainFrame;
import com.tkpm.view.frame.form.FlightDetailForm;
import com.tkpm.view.frame.form.TicketForm;

public class CustomerController {

	protected BaseMainFrame mainFrame;
	protected FlightService flightService;
	protected TicketClassService ticketClassService;
	protected TicketService ticketService;
	protected ReservationService reservationService;
	protected PolicyService policyService;
	protected TransitionAirportService transitionService;
	protected AirportService airportService;
	
	protected BaseAccount account;
	
	protected TicketForm ticketForm;
	
	public BaseMainFrame getMainFrame() {
		return mainFrame;
	}
	
	public void setAccount(BaseAccount account) {
		this.account = account;
	}
	
	public CustomerController(BaseMainFrame mainFrame) { 
		this.mainFrame = mainFrame;
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		flightService = FlightService.INSTANCE;
		ticketClassService = TicketClassService.INSTANCE;
		reservationService = ReservationService.INSTANCE;
		ticketService = TicketService.INSTANCE;
		airportService = AirportService.INSTANCE;
		
		//resolve cycle dependency between ticketService and reservationService
		reservationService.setTicketService(ticketService);	
		ticketService.setReservationService(reservationService);
		
		policyService = PolicyService.INSTANCE;
		transitionService = TransitionAirportService.INSTANCE;
		
		this.ticketForm = new TicketForm(this.mainFrame);
		this.ticketForm.setTitle("Đặt vé");
		//initFeatures();
	}
	
	protected void initFeatures() {
		initFlightFeatures();
	}
	
	
	protected void initFlightFeatures() {
		initListFlightFeature();
		initBookedReservationFeature();
	}
	
	protected void initTicketForm(FlightTabbedControllerView controllerView) {
		ticketForm.getSubmitButton().addActionListener(event -> {
			String ticketClassName = ticketForm.getTicketClass();
			
			Flight flight = ticketForm.getFlight();
			
			//Check policy 
			if (policyService.isLateToBook(flight)) {
				ticketForm.setError(TicketForm.TIMEOUT_ERROR);
				return;
			}
			
			//Validate
			if (ticketForm.areThereAnyEmptyStarField()) {
				ticketForm.setError(TicketForm.EMPTY_FIELD_ERROR);
				return;
			}
			
			//Get the icket class to find available ticket
			TicketClass ticketClass = ticketClassService.findTicketClassByName(ticketClassName);
			
			//Check out of stock ticket
			Reservation reservation = reservationService.findAvailableReservationFromFlight(flight, ticketClass);
			if (null == reservation) {
				ticketForm.setError(TicketForm.OUT_OF_STOCK_ERROR);
				return;
			}
			
			//If there is a reservation avaialbe => book
			reservation.setBookDate(LocalDate.now());
			reservation.setAccount(account);
			Ticket ticket = reservation.getTicket();
			ticket.setIsBooked(true);
			ticket.setName(ticketForm.getSubmitName());
			ticket.setIdentityCode(ticketForm.getSubmitIdentityCode());
			ticket.setPhoneNumber(ticketForm.getSubmitPhone());
			
			//Update information for booking
			ticketService.updateTicket(ticket);
			//reservationService.updateReservation(reservation);
			
			//Update the data in booked reservation screen
			initBookedReservationRead(controllerView);
			
			//Close the form
			ticketForm.setError(TicketForm.NO_ERROR);
			ticketForm.close();
		});
	}
	
	protected void initListFlightFeature() {
		FlightFeatureView featureView = (FlightFeatureView) mainFrame
				.getFeatureViews()
				.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
		
		FlightTabbedControllerView controllerView = featureView.getTabbedControllerView();
		
		//Init table
		initFlightListRead(controllerView);
		initFlightListSearch(controllerView);
		
		//Open detail flight view if clicked
		FlightListTableView table = controllerView.getFlightListTableView();
		table.getDetailButton().addActionListener(event -> {
			
			//Lazy query for the flight detail
			Flight flight = table.getSelectedFlight();
			List<Transition> transitions = transitionService.findTransitionForFlight(flight);
			FlightDetail flightDetail = flightService.findFlightDetailByFlight(flight);
			flight.setDetail(flightDetail);
			flight.setTransitions(transitions);
			
			//Popout the detail form
			FlightDetailForm detailView = new FlightDetailForm(flight, mainFrame);
			detailView.setTitle("Chi tiết chuyến bay");
			detailView.setVisible(true);
			
		});
		
		//Init ticket form for book button
		initTicketForm(controllerView);
		
		//Get detail view first
		FlightListDetailView detailView = controllerView.getFlightListDetailView();
		detailView.getBookButton().addActionListener(event -> {
			
			//Lazy query for the flight detail
			Flight flight = table.getSelectedFlight();
			
			//Do nothing if nothing is selected
			if (null == flight) {
				return;
			}
			
			//Get flight detail for the flight
			FlightDetail flightDetail = flightService.findFlightDetailByFlight(flight);
			flight.setDetail(flightDetail);
			
			//Build the ticketForm
//			TicketForm ticketForm = new TicketForm(flight, mainFrame);
//			initTicketForm(ticketForm);
			ticketForm.setModel(flight);
			
			//Open the ticket form
			ticketForm.setVisible(true);
			
		});		
		
	}
	
	protected void initFlightListRead(FlightTabbedControllerView controllerView) {
		FlightListTableView table = controllerView.getFlightListTableView();
		List<Flight> flights = new ArrayList<>(flightService.findAllFlights());
		
		List<Flight> availableFlights = 
				flights
				.stream()
				.filter(flight -> flight.getDateTime().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
		table.setFlights(availableFlights);
		table.update();
	}
	
	protected void updateAirportSearchCriteria() {
		FlightFeatureView featureView = (FlightFeatureView) mainFrame
				.getFeatureViews()
				.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
		
		FlightTabbedControllerView controllerView = featureView.getTabbedControllerView();
		BaseHeader header = controllerView.getHeaders().get(FlightTabbedControllerView.FLIGHT_LIST_HEADER_INDEX);
		List<Airport> airports = airportService.findAllAirports();
		SearchFlightPanel flightSearchPanel = (SearchFlightPanel) header.getPanels().get(1);
		
		List<JComboBox<Airport>> comboBoxes = flightSearchPanel.getAirportComboBoxes();
		for (JComboBox<Airport> cb: comboBoxes) {
			cb.removeAllItems();
			for (Airport airport: airports) {
				cb.addItem(airport);
			}
			cb.setSelectedIndex(-1);
		}
	}
	
	protected void initFlightListSearch(FlightTabbedControllerView controllerView) {
		BaseHeader header = controllerView.getHeaders().get(FlightTabbedControllerView.FLIGHT_LIST_HEADER_INDEX);
		FlightListTableView table = controllerView.getFlightListTableView();
		SearchFlightPanel flightSearchPanel = (SearchFlightPanel) header.getPanels().get(1);
		List<JComboBox<Airport>> comboBoxes = flightSearchPanel.getAirportComboBoxes();
		List<DatePicker> datePickers = flightSearchPanel.getDatePickers();
		
		updateAirportSearchCriteria();
		
		flightSearchPanel.getSearchButton().addActionListener(event -> {
			Airport departureAirport = (Airport) comboBoxes.get(0).getSelectedItem();
			Airport arrivalAirport =  (Airport) comboBoxes.get(1).getSelectedItem();
			LocalDate startDate = datePickers.get(0).getDate();
			LocalDate endDate = datePickers.get(1).getDate();
			
			List<Flight> flights = flightService.findFlightByCriterias(
					departureAirport, 
					arrivalAirport, 
					startDate, 
					endDate);
			
			table.setFlights(flights);
			table.update();
			
			for (JComboBox<Airport> cb: comboBoxes) {
				cb.setSelectedIndex(-1);
			}
			
			for (DatePicker datePicker: datePickers) {
				datePicker.setDate(null);
			}
			
		});
		
	}
	
	protected void initBookedReservationFeature() {
		FlightFeatureView featureView = (FlightFeatureView) mainFrame
				.getFeatureViews()
				.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
		
		FlightTabbedControllerView controllerView = featureView.getTabbedControllerView();
		
		initBookedReservationRead(controllerView);
		initBookReservationClickRowDisplayDetail(controllerView);
		initCancelBooking(controllerView);

	}
	
	protected void initBookReservationClickRowDisplayDetail(FlightTabbedControllerView controllerView) {
		BookedReservationDetailView detail = controllerView.getBookedReservationDetailView();
		BookedReservationTableView table = controllerView.getBookedReservationTableView();
		
		table.getSelectionModel().addListSelectionListener(event -> {
			if (table.getSelectedRow() > -1) {
				Reservation reservation = table.getSelectedReservation();
				if (null != reservation) {
					detail.setDataToDetailPanel(reservation);
				}
			}
		});
	}
	
	protected void initCancelBooking(FlightTabbedControllerView controllerView) {
		BookedReservationTableView table = controllerView.getBookedReservationTableView();
		
		table.getCancelTicketButton().addActionListener(event -> {
			
			if (table.isEditing()) {
				table.getCellEditor().stopCellEditing();
			}
			
			Reservation reservation = table.getSelectedReservation();
			if (policyService.isLateToCancel(reservation)) {
				JOptionPane.showMessageDialog(mainFrame, "Đã quá hạn hủy vé");
				return;
			}
			
			int input = JOptionPane.showConfirmDialog(mainFrame,
	        		"Bạn có chắc chắn muốn hủy vé ?",
	        		"Hủy vé",
	        		JOptionPane.YES_NO_OPTION);
			
			//Do nothing if click close
			if (JOptionPane.YES_OPTION == input) {
				
				//Cancel the selected ticket
				List<Integer> ids = new ArrayList<>(Arrays.asList(reservation.getId()));
				reservationService.cancelReservations(ids);
				
				//Update the current table
				initBookedReservationRead(controllerView);
				
				JOptionPane.showMessageDialog(mainFrame, "Đã hủy vé thành công");
			}
			
		});
		
	}
	
	protected void initBookedReservationRead(FlightTabbedControllerView controllerView) {
		BookedReservationTableView table = controllerView.getBookedReservationTableView();
		BookedReservationDetailView detail = controllerView.getBookedReservationDetailView();
		List<Reservation> reservations = reservationService.findReservationsForAccount(account);
		
		if (null == reservations || reservations.isEmpty()) {
			detail.setDataToDetailPanel(null);
		}
		
		table.setReservations(reservations);
		table.update();
		
	}
	
	public void run() {
		initFeatures();
		mainFrame.open();
	}
}
