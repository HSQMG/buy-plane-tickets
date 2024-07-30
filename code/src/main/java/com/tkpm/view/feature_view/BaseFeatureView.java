package com.tkpm.view.feature_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.tkpm.view.BaseView;
import com.tkpm.view.feature_view.detail_view.BaseReadOnlyDetailView;

public class BaseFeatureView extends BaseView {
	
	//protected attribute
	
	/*
	_part[0] : The center panel, which contain: 
		+ Tabs for subservices
		+ Table for showing data
	_part[1] : The top panel, which give us button to manipulate data
	_part[2] : The most-right panel, which give us detail information of data in table
	*/
	protected List<JPanel> parts;
	
	//Views for 3 parts
	protected JPanel headerView;
	protected JPanel detailView;
	protected JPanel centerView;
	
	//Index for parts (index in List<JPanel> parts
	public static final int CENTER_INDEX = 0;
	public static final int HEADER_INDEX = 1;
	public static final int DETAIL_INDEX = 2;
	
	//protected method
	protected void setupLayout() {
		double widthRatio = 0.7;
		double heightRatio = 0.1;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weighty = heightRatio;
		gbc.weightx = widthRatio;
		gbc.gridx = 0;
		this.add(parts.get(HEADER_INDEX), gbc);
		

		gbc.weighty = 1 - heightRatio;
		gbc.weightx = widthRatio;
		gbc.gridy = 1;
		this.add(parts.get(CENTER_INDEX), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.weighty = 1;
		gbc.weightx = 1 - widthRatio;
		
		
		this.add(parts.get(DETAIL_INDEX), gbc);
		
	}
	
	protected void setupComponents() {

		parts = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			parts.add(new BaseView());
		}
		
	}
	
	//Using in concrete class, after injecting detail, center and header
	protected void setupParts() {
		
		//Set layout for parts
		parts.get(CENTER_INDEX).setLayout(new BorderLayout());
		parts.get(HEADER_INDEX).setLayout(new BorderLayout());
		parts.get(DETAIL_INDEX).setLayout(new BorderLayout());
		parts.get(CENTER_INDEX).add(centerView, BorderLayout.CENTER);
		parts.get(HEADER_INDEX).add(headerView, BorderLayout.CENTER);
		parts.get(DETAIL_INDEX).add(detailView, BorderLayout.CENTER);
	}
	
	
	public BaseFeatureView setHeaderView(JPanel headerView) {
		this.headerView = headerView;
		return this;
	}

	public BaseFeatureView setDetailView(JPanel detailView) {
		this.detailView = detailView;
		return this;
	}

	public BaseFeatureView setCenterView(JPanel centerView) {
		this.centerView = centerView;
		return this;
	}

	//Constructor
	public BaseFeatureView() {
		
		setupComponents();
		setupLayout();
			
	}
	
	
}


