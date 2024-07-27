package com.tkpm.view.feature_view.detail_view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class FlightListDetailView extends BaseReadOnlyDetailView {
	
	public static final int BOOK_BUTTON_INDEX = 0;
	
	private JButton bookButton;
	

//	private static final String[] FIELD_NAMES = {
//			"Tình trạng ghế hạng 1",	//Còn/Hết
//			"Tình trạng ghế hạng 2",	//Còn/Hết
//	};
	
	protected void makeButtons() {
		
		
		bookButton = new JButton("ĐẶT VÉ");
		bookButton.setBackground(new Color(41, 97, 213));
		bookButton.setForeground(Color.WHITE);
		bookButton.setFont(new Font("Noto Sans", Font.BOLD, 26));

		//Add Buttons to detail panel
		paddingPanels.get(2).add(bookButton);
		
		//Setup button panel + add Buttons to detail panel
		paddingPanels.get(1).setLayout(new BorderLayout());
		paddingPanels.get(1).add(bookButton, BorderLayout.CENTER);
	}
	
	public FlightListDetailView() {
		super("Vé chuyến bay", null);
		makeButtons();	
	}
	
	public JButton getBookButton() {
		return bookButton;
	}

}