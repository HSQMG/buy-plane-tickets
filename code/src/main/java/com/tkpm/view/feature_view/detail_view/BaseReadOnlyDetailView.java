package com.tkpm.view.feature_view.detail_view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class BaseReadOnlyDetailView extends JPanel {
	
	protected final String title;
	protected List<JPanel> paddingPanels;
	protected List<JLabel> attributeFields;
	protected List<JLabel> attributeData;
	
	private void setLayout() {
		//Make 3 horizontal equal panel layout
		this.setLayout(new GridLayout(3, 1));
				
		//Make 3 padding panels
		for (int i = 0; i < 3; ++i) {
			paddingPanels.add(new JPanel());
			this.add(paddingPanels.get(i));	
		}	
		
		int paddingSize = (null == attributeFields?0:attributeFields.size());
		paddingPanels.get(1).setLayout(new GridLayout(paddingSize, 2));
		paddingPanels.get(2).setLayout(new FlowLayout());
	}
	
	private void addStuffsToPanel() {
		
		for (int i = 0; i < attributeFields.size(); ++i) {
			
			//Add JLabel
			paddingPanels.get(1).add(attributeFields.get(i));
			paddingPanels.get(1).add(attributeData.get(i));
			
		}
		
	}
	
	public void setDataToDetailPanel(Object data) {
		if (null == data) {
			for (JLabel field: attributeData) {
				field.setText("");
			}
		}
	};
	
	public BaseReadOnlyDetailView(String title, String[] attributeFieldNames) {
		
		//Add titled
		this.title = title;
		Border border = BorderFactory.createTitledBorder(title);
		this.setBorder(border);
		
		//Add padding panels and set layout
		paddingPanels = new ArrayList<>();
		setLayout();
		
		//Create attribute information field
		attributeFields = new ArrayList<>();
		attributeData = new ArrayList<>();
		
		if (null != attributeFieldNames) {
			for (String name: attributeFieldNames) {
				attributeFields.add(new JLabel(name));
				attributeData.add(new JLabel());
			}
		}
		
		//Add jlabel and jbuttons to display data
		addStuffsToPanel();
		
		//setBackground(Color.decode("#FDFBFF"));
		setBackground(Color.WHITE);
		
	}
		
	@Override
	public void setBackground(Color bg) {
		super.setBackground(Color.WHITE);
		try {
			for (JPanel i: paddingPanels) {
				i.setBackground(bg);
			}
		} catch (Exception ex) {
			//Ignore exception, because we always change background color
			//of _paddingPanels after initialize _paddingPanels
			//	=> This bracket will never happen
		}
	}
}





