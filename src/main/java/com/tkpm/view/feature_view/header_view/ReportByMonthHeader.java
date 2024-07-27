package com.tkpm.view.feature_view.header_view;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tkpm.view.model.ReportInfoModel;
import java.awt.Component;

public class ReportByMonthHeader extends JPanel{

	private ReportInfoModel timeModel;
	private JLabel titleLabel;
	
	private void initComponents() {
		titleLabel = new JLabel("BÁO CÁO DOANH THU THÁNG");
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
	
	public ReportByMonthHeader() {
		initComponents();
		setLayout();
		buildTitle();
	}
	
	public void updateTitle() {
		int month = timeModel.getMonth();
		int year = timeModel.getYear();
		String title = "BÁO CÁO DOANH THU THÁNG";
		if (null != timeModel) {
			title = "BÁO CÁO DOANH THU THÁNG " + month +  " NĂM " + year;
		}
		
		titleLabel.setText(title);
		this.validate();
	}
	
	public void setTimeMode(ReportInfoModel timeModel) {
		this.timeModel = timeModel;
		updateTitle();
	}
	
}
