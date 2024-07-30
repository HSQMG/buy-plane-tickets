package com.tkpm.view.feature_view;

import com.tkpm.view.feature_view.tabbed_controller_view.UserManagerTabbedControllerView;

public class UserManagerFeatureView extends BaseFeatureView {

	private UserManagerTabbedControllerView tabbedControllerView;
	
	public UserManagerFeatureView() {
		super();
		tabbedControllerView = new UserManagerTabbedControllerView(parts);
	
		this.setCenterView(tabbedControllerView)
			.setDetailView(tabbedControllerView.getCurrentDetailView())
			.setHeaderView(tabbedControllerView.getCurrentHeaderView());
		
		this.setupParts();
	}
	
	public UserManagerTabbedControllerView getTabbedControllerView() {
		return tabbedControllerView;
	}
}
