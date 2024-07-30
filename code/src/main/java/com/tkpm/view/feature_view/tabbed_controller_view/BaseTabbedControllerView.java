package com.tkpm.view.feature_view.tabbed_controller_view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import com.tkpm.view.BaseView;
import com.tkpm.view.feature_view.header_view.BaseHeader;

public class BaseTabbedControllerView extends JPanel {

	//parts from BaseFeatureView
	protected List<JPanel> backgroundParts;
	protected List<Component> centerViews;
	protected List<JPanel> detailViews;
	protected List<BaseHeader> headerViews;
	protected List<JPanel> footerViews;
	
	protected JTabbedPane tabbedPanel;
	protected Component centerView;
	protected JPanel detailView;
	protected JPanel headerView;
	protected JPanel footerView;
	
	//private final Color FOOTER_COLOR = new Color(230, 242, 255);
	private final Color FOOTER_COLOR = new Color(255, 255, 255);
	
	protected void setLayout() {
		double heightRatio = 0.95;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 1;
		gbc.weighty = heightRatio;
		this.add(tabbedPanel, gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 1 - heightRatio;
		//this.add(footerView, gbc);
		
	}
	
	//Select detail, header and footer when start program
	protected void initOption() {
		centerView = centerViews.get(0);
		detailView = detailViews.get(0);
		headerView = headerViews.get(0);
		//footerView = ...	is no need, cause we don't use footer to select
	}
	
	protected void initComponents() {
		
		tabbedPanel = new JTabbedPane();
		centerViews = new ArrayList<>();
		detailViews = new ArrayList<>();
		headerViews = new ArrayList<>();
		//footerViews = new ArrayList<>();	
		
		//Use basic footer view, cause we not implement anything here
		footerView = new BaseView();
		footerView.setBackground(FOOTER_COLOR);
		
		tabbedPanel.setBackground(Color.WHITE);
	}
	
	public BaseTabbedControllerView(List<JPanel> backgroundParts) {
		this.backgroundParts = backgroundParts;
		initComponents();
		setLayout();
		setBackground(Color.WHITE);
	}
	
	public void addCenter(Component centerView, String name) {
		centerViews.add(centerView);
		tabbedPanel.addTab(name, centerView);
	}
	
	public void addCenterAsTable(JTable table, String name) {
		centerViews.add(table);
		JScrollPane scroll = new JScrollPane(
				table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabbedPanel.addTab(name, scroll);
	}
	
	public void addHeader(BaseHeader headerView) {
		headerViews.add(headerView);
	}
	
	public void addDetail(JPanel detailView) {
		detailViews.add(detailView);
	}
	
	public JTabbedPane getTabbedPanel() {return tabbedPanel;}
	public List<Component> getCenterViews() {return centerViews; };
	public List<JPanel> getDetailViews() {return detailViews; }
	public List<BaseHeader> getHeaderViews() {return headerViews; }
	
	public Component getCurrentCenterView() {return centerView; }
	public JPanel getCurrentDetailView() {return detailView; }
	public JPanel getCurrentHeaderView() {return headerView; }
	
	public void setCurrentCenterView(Component centerView) {this.centerView = centerView; }
	public void setCurrentDetailView(JPanel detail) {detailView = detail; }
	public void setCurrentHeaderView(JPanel header) {headerView = header; }
}
