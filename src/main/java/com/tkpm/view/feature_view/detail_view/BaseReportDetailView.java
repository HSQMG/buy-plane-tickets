package com.tkpm.view.feature_view.detail_view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.tkpm.view.frame.form.FormBehaviour;

@SuppressWarnings("serial")
public class BaseReportDetailView extends JPanel implements FormBehaviour {
	
	protected String title;
	protected List<JPanel> paddingPanels;
	protected List<JLabel> attributeFields;
	protected String[] attributeFieldNames;
	
	protected JLabel warningField;
	protected JButton createButton;
	
	private static final String[] ERRORS = {
		"",
		"Giá trị của ô phải là số ",
		"Có một ô không có giá trị"
	};
	
	public static final int NO_ERROR = 0;
	public static final int NUMBER_FIELD_ERROR = 1;
	public static final int EMPTY_FIELD_ERROR = 2;
	
	//Ignore not an number value from an event
	//	And open the flag of nan error in number field
	protected void ignoreNANValue(KeyEvent event) {
		char character = event.getKeyChar();
		if (('0' <= character) && (character <= '9') || (KeyEvent.VK_BACK_SPACE == character)) {
			setError(NO_ERROR);
		} else {
			event.consume();
			setError(NUMBER_FIELD_ERROR);
		}
	}
		
	@Override
	public FormBehaviour setError(int errorCode) {
		if (0 <= errorCode && errorCode < ERRORS.length) {
			warningField.setText(ERRORS[errorCode]);
		}
		
		return this;
	}
	
	private void setLayout() {
		//Make 3 horizontal equal panel layout
		this.setLayout(new GridLayout(3, 1));
				
		//Make 3 padding panels
		for (int i = 0; i < 3; ++i) {
			paddingPanels.add(new JPanel());
			this.add(paddingPanels.get(i));	
		}	
		
		int paddingSize = (null == attributeFieldNames?0:attributeFieldNames.length);
		//paddingPanels.get(1).setLayout(new GridLayout(paddingSize, 2));
		paddingPanels.get(2).setLayout(new FlowLayout());
		for (JPanel panel : paddingPanels) {
			panel.setBackground(Color.WHITE);
		}
	}
	
	protected void addTitle(String title) {
		this.title = title;
		Border border = BorderFactory.createTitledBorder(title);
		this.setBorder(border);
	}
	
	protected void initComponents(String[] attributeFieldNames) {

		this.attributeFieldNames =  attributeFieldNames;
		
		paddingPanels = new ArrayList<>();
		attributeFields = new ArrayList<>();
		createButton = new JButton("Tạo báo cáo");
		createButton.setBackground(new Color(41, 97, 213));
		createButton.setForeground(Color.WHITE);
		warningField = new JLabel();					
		warningField.setForeground(Color.RED);		//Warning have red text
		
		//Create attribute information field
		if (null != attributeFieldNames) {
			for (String name: attributeFieldNames) {
				attributeFields.add(new JLabel(name));
			}
		}
	}
	
	protected void addButtonToLayout() {
		paddingPanels.get(2).add(createButton);	
	}
	
	public BaseReportDetailView(String title, String[] attributeFieldNames) {
		
		addTitle(title);
		initComponents(attributeFieldNames);
		setLayout();
		addButtonToLayout();
		
		setBackground(Color.WHITE);
		for (JPanel panel : paddingPanels) {
			panel.setBackground(Color.WHITE);
		}
		
	}

	@Override
	public Object submit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JButton getSubmitButton() {
		return createButton;
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
}





