package com.tkpm.view.feature_view;

import com.tkpm.view.feature_view.tabbed_controller_view.FlightManagerTabbedControllerView;

public class FlightManagerFeatureView extends BaseFeatureView {

	private FlightManagerTabbedControllerView tabbedControllerView;
	
	public FlightManagerFeatureView() {
		super();
		tabbedControllerView = new FlightManagerTabbedControllerView(parts);
	
		this.setCenterView(tabbedControllerView)
			.setDetailView(tabbedControllerView.getCurrentDetailView())
			.setHeaderView(tabbedControllerView.getCurrentHeaderView());
		
		this.setupParts();
	}
	
	public FlightManagerTabbedControllerView getTabbedControllerView() {
		return tabbedControllerView;
	}
}
