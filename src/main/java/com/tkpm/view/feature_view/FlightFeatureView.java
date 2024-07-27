package com.tkpm.view.feature_view;

import com.tkpm.view.feature_view.tabbed_controller_view.FlightTabbedControllerView;

public class FlightFeatureView extends BaseFeatureView {

	private FlightTabbedControllerView tabbedControllerView;
	
	public FlightFeatureView() {
		super();
		tabbedControllerView = new FlightTabbedControllerView(parts);
		
		this.setCenterView(tabbedControllerView)
			.setDetailView(tabbedControllerView.getCurrentDetailView())
			.setHeaderView(tabbedControllerView.getCurrentHeaderView());
		
		this.setupParts();
	}
	
	public FlightTabbedControllerView getTabbedControllerView() {
		return tabbedControllerView;
	}
}
