package com.tkpm.view.frame;

import com.tkpm.view.feature_view.BaseFeatureView;
import com.tkpm.view.feature_view.FlightFeatureView;
import com.tkpm.view.feature_view.FlightManagerFeatureView;
import com.tkpm.view.feature_view.ReportFeatureView;
import com.tkpm.view.feature_view.UserManagerFeatureView;
import com.tkpm.view.widget.Category;

public class AdminMainFrame extends BaseMainFrame {

	public static final int USER_MANAGER_FEATURE_INDEX = 3;
	
	protected BaseFeatureView featureView;
	protected BaseFeatureView featureView0;
	protected BaseFeatureView featureView1;
	protected BaseFeatureView featureView2;
	
	@Override
	protected void initValueForComponents() {
		
		featureView = new FlightFeatureView();
		featureView0 = new FlightManagerFeatureView();
		featureView1 = new ReportFeatureView();
		featureView2 = new UserManagerFeatureView();
		
		this.categories.add(new Category("Danh sách chuyến bay"));
		this.categories.add(new Category("Quản lý chuyến bay"));
		this.categories.add(new Category("Lập báo cáo chi tiết"));
		this.categories.add(new Category("Quản lý tài khoản"));
		
		this.featureViews.add(featureView);
		this.featureViews.add(featureView0);
		this.featureViews.add(featureView1);
		this.featureViews.add(featureView2);

	}
	
	public AdminMainFrame() {
		super();
		
	}
	
}