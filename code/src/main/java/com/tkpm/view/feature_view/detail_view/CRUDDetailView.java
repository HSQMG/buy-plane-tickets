package com.tkpm.view.feature_view.detail_view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class CRUDDetailView extends BaseReadOnlyDetailView {
	
	protected List<JButton> buttons;
	
	public static final int CREATE_BUTTON_INDEX = 0;
	public static final int DELETE_BUTTON_INDEX = 1;
	
	protected void makeButtons() {
		
		//Create buttons
		buttons = new ArrayList<>();
		buttons.add(new JButton("Thêm"));
		buttons.add(new JButton("Xóa"));
		
		//Edit "Them" button
		editButton();
		
		//Add Buttons to detail panel
		for (JButton button: buttons) {
			paddingPanels.get(2).add(button);
		}
	}
	
	public CRUDDetailView(String titled, String[] attributeFieldNames) {
		super(titled, attributeFieldNames);
		makeButtons();
		
	}
	
	
	public List<JButton> getButtons() {
		return buttons;
	}
	
	public void editButton() {
		buttons.get(0).setBackground(new Color(41, 97, 213));
		buttons.get(0).setForeground(Color.WHITE);
	}
}