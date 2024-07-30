package com.tkpm.controller;

import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.tkpm.entities.Airport;
import com.tkpm.entities.Flight;
import com.tkpm.entities.FlightDetail;
import com.tkpm.entities.Transition;
import com.tkpm.service.AirportService;
import com.tkpm.service.ReportService;
import com.tkpm.service.TransitionAirportService;
import com.tkpm.view.feature_view.FlightFeatureView;
import com.tkpm.view.feature_view.FlightManagerFeatureView;
import com.tkpm.view.feature_view.ReportFeatureView;
import com.tkpm.view.feature_view.detail_view.AirportCRUDDetailView;
import com.tkpm.view.feature_view.detail_view.BaseReportDetailView;
import com.tkpm.view.feature_view.detail_view.CRUDDetailView;
import com.tkpm.view.feature_view.detail_view.FlightCRUDDetailView;
import com.tkpm.view.feature_view.detail_view.ReportByMonthDetailView;
import com.tkpm.view.feature_view.detail_view.ReportByYearDetailView;
import com.tkpm.view.feature_view.tabbed_controller_view.FlightManagerTabbedControllerView;
import com.tkpm.view.feature_view.tabbed_controller_view.FlightTabbedControllerView;
import com.tkpm.view.feature_view.tabbed_controller_view.ReportTabbedControllerView;
import com.tkpm.view.feature_view.table.AirportCRUDTableView;
import com.tkpm.view.feature_view.table.FlightCRUDTableView;
import com.tkpm.view.feature_view.table.ReportByMonthTableView;
import com.tkpm.view.feature_view.table.ReportByYearTableView;
import com.tkpm.view.feature_view.table.TransitionCRUDTableView;
import com.tkpm.view.frame.BaseMainFrame;
import com.tkpm.view.frame.CustomerMainFrame;
import com.tkpm.view.frame.ManagerMainFrame;
import com.tkpm.view.frame.form.AirportForm;
import com.tkpm.view.frame.form.FlightForm;
import com.tkpm.view.model.ReportInfoModel;
import com.tkpm.wrapper.report.BaseReport;

public class ManagerController extends CustomerController {

	//Forms
	protected AirportForm createAirportForm;
	protected AirportForm updateAirportForm;
	protected FlightForm createFlightForm;
	protected FlightForm updateFlightForm;
	
	//Services
	//protected AirportService airportService;
	protected ReportService reportService;
	
	//Utilities
	protected List<Transition> deleteTransitions;
	
	public ManagerController(BaseMainFrame mainFrame) {
		super(mainFrame);
		airportService = AirportService.INSTANCE;
		reportService = ReportService.INSTANCE;
		deleteTransitions = new LinkedList<>();
	}

	@Override
	protected void initFeatures() {
		
		//Features from Customer role
		super.initFeatures();
		
		initFlightManagerFeatures();
		initReportFeatures();
	}
	
	protected void initFlightManagerFeatures() {
		FlightManagerFeatureView featureView = (FlightManagerFeatureView) mainFrame
				.getFeatureViews()
				.get(ManagerMainFrame.FLIGHT_MANAGER_FEATURE_INDEX);
		
		FlightManagerTabbedControllerView controllerView = featureView.getTabbedControllerView();
		
		initAirportCRUDFeature(controllerView);
		initFlightCRUDFeature(controllerView);
	}
	
	protected void initAirportCRUDFeature(FlightManagerTabbedControllerView controllerView) {
		createAirportForm = new AirportForm(mainFrame);
		updateAirportForm = new AirportForm(mainFrame);
		createAirportForm.setTitle("Tạo sân bay");
		updateAirportForm.setTitle("Cập nhật sân bay");
		
		initAirportClickRowDisplayDetail(controllerView);
		initAirportCreate(controllerView);
		initAirportRead(controllerView);
		initAirportUpdate(controllerView);
		initAirportDelete(controllerView);
	};
	
	protected void initAirportClickRowDisplayDetail(FlightManagerTabbedControllerView controllerView) {
		AirportCRUDDetailView detail = controllerView.getAirporCRUDDetailView();
		AirportCRUDTableView table = controllerView.getAirportCRUDTableView();
		
		table.getSelectionModel().addListSelectionListener(event -> {
			if (table.getSelectedRow() > -1) {
				Airport airport = table.getSelectedAirport();
				if (null != airport) {
					detail.setDataToDetailPanel(airport);
				}
			}
		});
	}
	
	protected void initAirportCreate(FlightManagerTabbedControllerView controllerView) {
		AirportCRUDDetailView detail = controllerView.getAirporCRUDDetailView();
		
		//Init "Create airport" button
		detail.getButtons().get(CRUDDetailView.CREATE_BUTTON_INDEX).addActionListener(event -> {
			createAirportForm.open();
		});
		
		//Init submit button of the airport form
		createAirportForm.getSubmitButton().addActionListener(event -> {
			
			//Validate the form
			if (createAirportForm.areThereAnyEmptyStarField()) {
				createAirportForm.setError(AirportForm.EMPTY_STAR_FIELD_ERROR);
				return;
			}
			
			//Check if there is an airport with the same name existed
			Airport airport = createAirportForm.submit();
			Airport testAirport = airportService.findAirportByName(airport.getName());
			if (null != testAirport) {
				createAirportForm.setError(AirportForm.NAME_EXISTED_FIELD_ERROR);
				return;
			}
			
			//validate success case
			airport = airportService.createAirport(airport);
			
			//Update the table view
			initAirportRead(controllerView);
			
			//Update the airport criterias in flight list view
			updateAirportSearchCriteria();
			
			//Close the form
			createAirportForm.setError(AirportForm.NO_ERROR);
			createAirportForm.close();
			
		});
	}
	
	protected void initAirportRead(FlightManagerTabbedControllerView controllerView) {
		List<Airport> airports = airportService.findAllAirports();
		AirportCRUDTableView table = controllerView.getAirportCRUDTableView();
		table.setAirports(airports);
		table.update();
	}
	
	protected void initAirportUpdate(FlightManagerTabbedControllerView controllerView) {
		AirportCRUDTableView table = controllerView.getAirportCRUDTableView();
		table.getUpdateButton().addActionListener(event -> {
			Airport airport = table.getSelectedAirport();
			if (null == airport) {
				return;
			}
			updateAirportForm.setAirport(airport);
			updateAirportForm.setVisible(true);
		});
		
		updateAirportForm.getSubmitButton().addActionListener(event -> {
			
			//Validate the form
			if (updateAirportForm.areThereAnyEmptyStarField()) {
				updateAirportForm.setError(AirportForm.EMPTY_STAR_FIELD_ERROR);
				return;
			}
			
			//Check if there is an airport with the same name existed 
			//	(if same name => check if the id is the same or not)
			Airport airport = updateAirportForm.submit();
			Airport testAirport = airportService.findAirportByName(airport.getName());
			if (null != testAirport && !airport.getId().equals(testAirport.getId())) {
				updateAirportForm.setError(AirportForm.NAME_EXISTED_FIELD_ERROR);
				return;
			}
			
			//validate success case
			airport = airportService.updateAirport(airport);
			
			//Update the table view
			initAirportRead(controllerView);
			initFlightRead(controllerView);
			
			//Update the flight view for customer
			FlightFeatureView featureView = (FlightFeatureView) mainFrame
					.getFeatureViews()
					.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
					
			FlightTabbedControllerView customerControllerView = featureView.getTabbedControllerView();
			initFlightListRead(customerControllerView);
			initBookedReservationRead(customerControllerView);	
			
			//Update the airport criterias in flight list view
			updateAirportSearchCriteria();
			
			//Close the form
			updateAirportForm.setError(AirportForm.NO_ERROR);
			updateAirportForm.close();
		});
		
	}
	
	protected void initAirportDelete(FlightManagerTabbedControllerView controllerView) {
		AirportCRUDDetailView detail = controllerView.getAirporCRUDDetailView();
		AirportCRUDTableView table = controllerView.getAirportCRUDTableView();
		
		//Init "Delete airport" button
		detail.getButtons().get(CRUDDetailView.DELETE_BUTTON_INDEX).addActionListener(event -> {
			
			int input = JOptionPane.showConfirmDialog(mainFrame,
	        		"Bạn có chắc chắn muốn xóa ?\nDữ liệu bị xóa sẽ không thể khôi phục lại được.",
	        		"Xóa",
	        		JOptionPane.YES_NO_OPTION);
			
			if (JOptionPane.YES_OPTION == input) {
				List<Airport> airports = table.getSelectedAirports();
				
				//Get the id from the airport
				List<Integer> ids = airports
						.stream()
						.map(airport -> airport.getId())
						.collect(Collectors.toList());
				
				//Delete thoose airports
				airportService.deleteAirports(ids);
				
				//Update the table view
				initAirportRead(controllerView);
				initFlightRead(controllerView);
				
				//Clear the detail view
				detail.setDataToDetailPanel(null);
				
				//Update the flight view for customer
				FlightFeatureView featureView = (FlightFeatureView) mainFrame
						.getFeatureViews()
						.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
						
				FlightTabbedControllerView customerControllerView = featureView.getTabbedControllerView();
				initFlightListRead(customerControllerView);
				initBookedReservationRead(customerControllerView);	
				
				//Update the airport criterias in flight list view
				updateAirportSearchCriteria();
				
				//Success message
				JOptionPane.showMessageDialog(null, "Đã xóa thành công.");
			}
			
			
			
		});
	}
	
	protected void initFlightCRUDFeature(FlightManagerTabbedControllerView controllerView) {
		createFlightForm = new FlightForm(mainFrame);
		updateFlightForm = new FlightForm(mainFrame);
		createFlightForm.setTitle("Tạo chuyến bay");
		updateFlightForm.setTitle("Cập nhật chuyến bay");
		
		initFlightClickRowDisplayDetail(controllerView);
		initFlightCreate(controllerView);
		initFlightRead(controllerView);
		initFlightUpdate(controllerView);
		initFlightDelete(controllerView);
	}
	
	protected void initFlightClickRowDisplayDetail(FlightManagerTabbedControllerView controllerView) {
		FlightCRUDDetailView detail = controllerView.getFlightCRUDDetailView();
		FlightCRUDTableView table = controllerView.getFlightCRUDTableView();
		
		table.getSelectionModel().addListSelectionListener(event -> {
			if (table.getSelectedRow() > -1) {
				Flight flight = table.getSelectedFlight();
				if (null != flight) {
					detail.setDataToDetailPanel(flight);
				}
			}
		});
	}
	
	protected void initFlightCreate(FlightManagerTabbedControllerView controllerView) {
		FlightCRUDDetailView detail = controllerView.getFlightCRUDDetailView();
		
		//Init "Create flight" button
		detail.getButtons().get(CRUDDetailView.CREATE_BUTTON_INDEX).addActionListener(event -> {
			
			//Load the airport to choose for the form
			List<Airport>  airports = airportService.findAllAirports();
			createFlightForm.setAirports(airports);
			
			//Open the form
			createFlightForm.open();
		});
		
		//Init add transition button
		createFlightForm.getAddTransitionButton().addActionListener(event -> {
			List<Airport> airports = airportService.findAllAirports();
			createFlightForm.getTransitionForm().loadAirport(airports);
			createFlightForm.getTransitionForm().open();
		});
		
		//Init delete transition button
		createFlightForm.getDeleteTransitionButton().addActionListener(event -> {
			int input = JOptionPane.showConfirmDialog(createFlightForm,
	        		"Bạn có chắc chắn muốn xóa ?",
	        		"Xóa",
	        		JOptionPane.YES_NO_OPTION);
			
			
			TransitionCRUDTableView table = createFlightForm.getTable();
			
			if (JOptionPane.YES_OPTION == input) {
				List<Transition> original = table.getTransitions();
				List<Transition> selected = table.getSelectedTransitions();
				
				//Delete the reference
				for (Transition trans: selected) {
					original.removeIf(iter -> iter == trans);
				}
				
				table.setTransitions(original);
				table.update();
				
				//Success message
				JOptionPane.showMessageDialog(createFlightForm, "Đã xóa thành công.");
			}
		});
		
		//Init update button of the transition
		createFlightForm
			.getTable()
			.getActionButtons()
			.get(TransitionCRUDTableView.UPDATE_BUTTON_INDEX)
			.addActionListener(event -> {
				
				Transition model = createFlightForm.getTable().getSelectedTransition();
		
				if (null != model) {
					List<Airport> airports = airportService.findAllAirports();
					createFlightForm.getTransitionForm().loadAirport(airports);
					createFlightForm.getTransitionForm().loadModel(model);
					createFlightForm.getTransitionForm().setVisible(true);
				}
		});
		
		//Init submit button of the flight form
		createFlightForm.getSubmitButton().addActionListener(event -> {
			
			//Validate the form
			if (createFlightForm.areThereAnyEmptyStarField()) {
				createFlightForm.setError(FlightForm.EMPTY_FIELD_ERROR);
				return;
			}
			
			if (createFlightForm.areTheseAirportMatch()) {
				createFlightForm.setError(FlightForm.AIRPORT_MATCH_ERROR);
				return;
			}
			
			//Create the flight
			Flight flight = createFlightForm.submit();
			flight = flightService.createFlight(flight);
			
			//Update the table view
			initFlightRead(controllerView);
			
			//Update the flight view for customer
			FlightFeatureView featureView = (FlightFeatureView) mainFrame
					.getFeatureViews()
					.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
			
			FlightTabbedControllerView customerControllerView = featureView.getTabbedControllerView();
			initFlightListRead(customerControllerView);
			
			//Close the form
			createFlightForm.setError(AirportForm.NO_ERROR);
			createFlightForm.close();
			
		});
	}
	
	protected void initFlightRead(FlightManagerTabbedControllerView controllerView) {
		List<Flight> flights = new ArrayList<>(flightService.findAllFlights());
		FlightCRUDTableView table = controllerView.getFlightCRUDTableView();
		table.setFlights(flights);
		table.update();
	}
	
	protected void initFlightUpdate(FlightManagerTabbedControllerView controllerView) {
		FlightCRUDTableView table = controllerView.getFlightCRUDTableView();
		
		//Init update button
		table.getActionButtons().get(FlightCRUDTableView.UPDATE_BUTTON_INDEX).addActionListener(event -> {
			

			Flight flight = table.getSelectedFlight();
			if (null == flight) {
				return;
			}
			
			//Load data to popout detail information
			FlightDetail detail = flightService.findFlightDetailByFlight(flight);
			List<Transition> transitions = transitionService.findTransitionForFlight(flight);
			flight.setDetail(detail);
			flight.setTransitions(transitions);
			
			//Load data for the form
			List<Airport>  airports = airportService.findAllAirports();
			updateFlightForm.setAirports(airports);
			updateFlightForm.setModel(flight);
			
			updateFlightForm.setVisible(true);
			
		});
		
		//Init add transition button
		updateFlightForm.getAddTransitionButton().addActionListener(event -> {
			List<Airport> airports = airportService.findAllAirports();
			updateFlightForm.getTransitionForm().loadAirport(airports);
			updateFlightForm.getTransitionForm().open();
		});
		
		//Init update button
		updateFlightForm
		.getTable()
		.getActionButtons()
		.get(TransitionCRUDTableView.UPDATE_BUTTON_INDEX)
		.addActionListener(event -> {
			Transition model = updateFlightForm.getTable().getSelectedTransition();
	
			if (null != model) {
				List<Airport> airports = airportService.findAllAirports();
				updateFlightForm.getTransitionForm().loadAirport(airports);
				updateFlightForm.getTransitionForm().loadModel(model);
				updateFlightForm.getTransitionForm().setVisible(true);
			}
		});
		
		//Init delete transition button
		updateFlightForm.getDeleteTransitionButton().addActionListener(event -> {
			int input = JOptionPane.showConfirmDialog(updateFlightForm,
			        "Bạn có chắc chắn muốn xóa ?",
			        "Xóa",
			        JOptionPane.YES_NO_OPTION);
					
					
			TransitionCRUDTableView formTable = updateFlightForm.getTable();
					
			if (JOptionPane.YES_OPTION == input) {
				List<Transition> original = formTable.getTransitions();
				List<Transition> selected = formTable.getSelectedTransitions();
				deleteTransitions.addAll(selected);
				
				//Delete the reference
				for (Transition trans: selected) {
					original.removeIf(iter -> iter == trans);
				}
				
				formTable.setTransitions(original);
				formTable.update();
						
				//Success message
				JOptionPane.showMessageDialog(updateFlightForm, "Đã xóa thành công.");
			}
		});
		
		//Init cancel button
		updateFlightForm.getCancelButton().addActionListener(event -> {
			deleteTransitions.clear();
			updateFlightForm.dispose();
		});
		
		//Init submit button of the flight form
		updateFlightForm.getSubmitButton().addActionListener(event -> {
					
			//Validate the form
			if (updateFlightForm.areThereAnyEmptyStarField()) {
				updateFlightForm.setError(FlightForm.EMPTY_FIELD_ERROR);
				return;
			}
					
			if (updateFlightForm.areTheseAirportMatch()) {
				updateFlightForm.setError(FlightForm.AIRPORT_MATCH_ERROR);
				return;
			}
			
			int input = JOptionPane.showConfirmDialog(updateFlightForm,
			        "Việc nhấn OK đồng nghĩa với việc toàn bộ thông tin của vé máy bay đều sẽ được reset lại, bạn có muốn tiếp tục ?",
			        "Xóa",
			        JOptionPane.YES_NO_OPTION);
			
			if (JOptionPane.NO_OPTION == input) {
				return;
			}
			//Hard delete the deleted transitions
			List<Integer> ids = deleteTransitions
					.stream()
					.filter(trans -> null != trans.getId())
					.map(trans -> trans.getId())
					.collect(Collectors.toList());
			transitionService.deleteTransitions(ids);
			deleteTransitions.clear();
			
			//Create the flight
			Flight flight = updateFlightForm.submit();
			flight = flightService.updateFlight(flight);
					
			//Update the table view
			initFlightRead(controllerView);
					
			//Update the flight view for customer
			FlightFeatureView featureView = (FlightFeatureView) mainFrame
					.getFeatureViews()
					.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
					
			FlightTabbedControllerView customerControllerView = featureView.getTabbedControllerView();
			initFlightListRead(customerControllerView);
			initBookedReservationRead(customerControllerView);		
			
			//Close the form
			updateFlightForm.setError(AirportForm.NO_ERROR);
			updateFlightForm.close();
					
		});
	}
	
	protected void initFlightDelete(FlightManagerTabbedControllerView controllerView) {
		FlightCRUDDetailView detail = controllerView.getFlightCRUDDetailView();
		FlightCRUDTableView table = controllerView.getFlightCRUDTableView();
		
		//Init "Delete airport" button
		detail.getButtons().get(CRUDDetailView.DELETE_BUTTON_INDEX).addActionListener(event -> {
			
			int input = JOptionPane.showConfirmDialog(mainFrame,
	        		"Bạn có chắc chắn muốn xóa ?\nDữ liệu bị xóa sẽ không thể khôi phục lại được.",
	        		"Xóa",
	        		JOptionPane.YES_NO_OPTION);
			
			if (JOptionPane.YES_OPTION == input) {
				List<Flight> flights = table.getSelectedFlights();
				
				//Get the id from the airport
				List<Integer> ids = flights
						.stream()
						.map(flight -> flight.getId())
						.collect(Collectors.toList());
				
				//Delete thoose airports
				flightService.deleteFlights(ids);
				
				//Clear the detail view
				detail.setDataToDetailPanel(null);
				
				//Update the table view
				initFlightRead(controllerView);
				
				//Update the flight view for customer
				FlightFeatureView featureView = (FlightFeatureView) mainFrame
						.getFeatureViews()
						.get(CustomerMainFrame.FLIGHT_FEATURE_INDEX);
				FlightTabbedControllerView customerControllerView = featureView.getTabbedControllerView();
				initFlightListRead(customerControllerView);
				initBookedReservationRead(customerControllerView);	
				
				//Success message
				JOptionPane.showMessageDialog(null, "Đã xóa thành công.");
			}
			
		});
	}
	
	protected void initReportFeatures() {
		ReportFeatureView featureView = (ReportFeatureView) mainFrame
				.getFeatureViews()
				.get(ManagerMainFrame.REPORT_FEATURE_INDEX);
		
		ReportTabbedControllerView controllerView = featureView.getTabbedControllerView();
		
		initReportByMonthFeature(controllerView);
		initReportByYearFeature(controllerView);
	}
	
	protected void initReportByMonthFeature(ReportTabbedControllerView controllerView) {
		ReportByMonthDetailView detail = controllerView.getReportByMonthDetailView();
		ReportByMonthTableView table = controllerView.getReportByMonthTable();
		
		detail.getSubmitButton().addActionListener(event -> {
			
			//Validate
			if (detail.isYearFieldEmpty()) {
				detail.setError(BaseReportDetailView.EMPTY_FIELD_ERROR);
			}
			
			//Get the report information
			ReportInfoModel metadata = detail.submit();
			Month month = Month.of(metadata.getMonth());
			int year = metadata.getYear();
			BaseReport report = reportService.getReportByMonth(month, year);
			
			//Popout the data
			table.setReport(report);
			table.update();
			
			//Clear the error if contained
			detail.setError(BaseReportDetailView.NO_ERROR);
		});
	}
	
	protected void initReportByYearFeature(ReportTabbedControllerView controllerView) {
		ReportByYearDetailView detail = controllerView.getReportByYearDetailView();
		ReportByYearTableView table = controllerView.getReportByYearTable();
		
		detail.getSubmitButton().addActionListener(event -> {
			
			//Validate
			if (detail.isYearFieldEmpty()) {
				detail.setError(BaseReportDetailView.EMPTY_FIELD_ERROR);
			}
			
			//Get the report information
			ReportInfoModel metadata = detail.submit();
			int year = metadata.getYear();
			List<BaseReport> reports = reportService.getReportByYear(year);
			
			//Popout the data
			table.setReportModels(reports);
			table.update();
			
			//Clear the error if contained
			detail.setError(BaseReportDetailView.NO_ERROR);
		});
	}
	
}
