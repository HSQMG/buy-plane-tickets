package com.tkpm.view.feature_view.header_view;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReportByYearHeader extends JPanel{

	private Integer year;
	private JLabel titleLabel;
	
	private void initComponents() {
		titleLabel = new JLabel("BÁO CÁO DOANH THU NĂM");
	}
	
	private void buildTitle() {
		titleLabel.setFont (titleLabel.getFont ().deriveFont (26.0f));
	}
	
	private void setLayout() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(Color.WHITE);
		add(Box.createHorizontalGlue());
		add(titleLabel);
		add(Box.createHorizontalGlue());
	}
	
	public ReportByYearHeader() {
		initComponents();
		setLayout();
		buildTitle();
	}
	
	public void updateTitle() {
		String title = "BÁO CÁO DOANH THU NĂM" + (null != year?" " + year:"");
		titleLabel.setText(title);
		this.validate();
	}
	
	public void setYear(Integer year) {
		this.year = year;
		updateTitle();
	}
	
}
