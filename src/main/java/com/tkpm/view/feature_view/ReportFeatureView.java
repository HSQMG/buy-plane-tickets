package com.tkpm.view.feature_view;

import com.tkpm.view.feature_view.tabbed_controller_view.ReportTabbedControllerView;

public class ReportFeatureView extends BaseFeatureView {

	private ReportTabbedControllerView tabbedControllerView;
	
	public ReportFeatureView() {
		super();
		tabbedControllerView = new ReportTabbedControllerView(parts);
	
		this.setCenterView(tabbedControllerView)
			.setDetailView(tabbedControllerView.getCurrentDetailView())
			.setHeaderView(tabbedControllerView.getCurrentHeaderView());
		
		this.setupParts();
	}
	
	public ReportTabbedControllerView getTabbedControllerView() {
		return tabbedControllerView;
	}
}
