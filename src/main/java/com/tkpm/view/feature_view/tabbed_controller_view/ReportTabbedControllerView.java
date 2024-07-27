package com.tkpm.view.feature_view.tabbed_controller_view;

import java.util.List;

import javax.swing.JPanel;

import com.tkpm.view.action.ChangeTabListener;
import com.tkpm.view.feature_view.detail_view.ReportByMonthDetailView;
import com.tkpm.view.feature_view.detail_view.ReportByYearDetailView;
import com.tkpm.view.feature_view.header_view.BaseHeader;
import com.tkpm.view.feature_view.header_view.ReportByMonthHeader;
import com.tkpm.view.feature_view.header_view.ReportByYearHeader;
import com.tkpm.view.feature_view.table.ReportByMonthTableView;
import com.tkpm.view.feature_view.table.ReportByYearTableView;


public class ReportTabbedControllerView extends BaseTabbedControllerView {

	//Header
//	private ReportByYearHeader reportByYearHeader;
//	private ReportByMonthHeader reportByMonthHeader;
	private BaseHeader reportByYearHeader;
	private BaseHeader reportByMonthHeader;
	
	//Tables
	private ReportByYearTableView reportByYearTable;
	private ReportByMonthTableView reportByMonthTable;
	
	//Detail views
	private ReportByYearDetailView reportByYearDetailView;
	private ReportByMonthDetailView reportByMonthDetailView;
	
	public ReportTabbedControllerView(List<JPanel> backgroundParts) {
		super(backgroundParts);
		
		//Init for report by year feature
		reportByYearHeader = new BaseHeader(new ReportByYearHeader());
		reportByYearTable = new ReportByYearTableView();
		reportByYearDetailView = new ReportByYearDetailView();
		
		//Init for report by month feature
		reportByMonthHeader = new BaseHeader(new ReportByMonthHeader());
		reportByMonthTable = new ReportByMonthTableView();
		reportByMonthDetailView = new ReportByMonthDetailView();
		
		//Add screen for report by month tab
		this.addCenterAsTable(reportByYearTable, "Báo cáo theo năm");
		this.addDetail(reportByYearDetailView);
		this.addHeader(reportByYearHeader);
		
		//Add screen for report by year tab
		this.addCenterAsTable(reportByMonthTable, "Báo cáo theo tháng");
		this.addDetail(reportByMonthDetailView);
		this.addHeader(reportByMonthHeader);
		
		//Change detail panel and header panel while change tab
		tabbedPanel.addMouseListener(new ChangeTabListener(this, backgroundParts));
			
		initOption();
	}
	
	public BaseHeader getReportByYearHeader() {return reportByYearHeader;}
	public ReportByYearTableView getReportByYearTable() {return reportByYearTable;}
	public ReportByYearDetailView getReportByYearDetailView() {return reportByYearDetailView;}

	public BaseHeader getReportByMonthHeader() {return reportByMonthHeader;}
	public ReportByMonthTableView getReportByMonthTable() {return reportByMonthTable;}
	public ReportByMonthDetailView getReportByMonthDetailView() {return reportByMonthDetailView;}
	
}
