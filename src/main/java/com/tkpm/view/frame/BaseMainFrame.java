package com.tkpm.view.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.tkpm.view.action.MappingFeatureActionListener;
import com.tkpm.view.feature_view.BaseFeatureView;
import com.tkpm.view.widget.Category;


public class BaseMainFrame extends BaseFrame {

	protected JPanel basePanel;
	protected JPanel leftPanel;
	protected JPanel rightPanel;
	
	protected List<BaseFeatureView> featureViews;
	protected List<Category> categories;
	
	//Size of MainJFrame
	final protected int HEIGHT = 700;
	final protected int WIDTH = 1250;
	
	protected void constructComponents() {
		rightPanel = new JPanel();
		leftPanel = new JPanel();
		basePanel = new JPanel();
		
		categories = new ArrayList<Category>();
		featureViews = new ArrayList<BaseFeatureView>();
	}
	
	//Init value for menuPanels and featurePanels
	protected void initValueForComponents() {
		//do some thing in the concrete class
	}
	
	protected void initLeftPanel() {
		
		int size = categories.size();
		
		//Setup the left side menu
		// Only add button to left panel, if there are more than 1 option
		//	1 option does not need to show up the button
		if (size > 1) {
			leftPanel.setLayout(new GridLayout(size, 1, 0, 40));
			
			//Add n button on the left side menu
			categories.forEach(category -> {
				leftPanel.add(category);
			});
		}
	}
	
	protected void initRightPanel() {
		
		rightPanel.setLayout(new BorderLayout());
		
		//Add default option when program started
		rightPanel.add(featureViews.get(0), BorderLayout.CENTER);
		
		//Map the menu panels with the right panels:
		//	When a menu panels is clicked, the right panel change the UI
		final int size = categories.size();
		
		//Show up the buttons
		if (1  < size) {
			for (int i = 0; i < size; ++i) {
				categories.get(i).getButton().addActionListener(
						new MappingFeatureActionListener(rightPanel, featureViews.get(i)));		
			}
		} else {
			
			//Just coloring the left panel with the category's border color,
			//	because we don not need button if we only have 1 option
			//leftPanel.setBackground(Category.BORDER_COLOR);
			leftPanel.setBackground(Color.WHITE);
		}
	}
	
	
	protected void combineLeftRightPanel() {
		
		//Combine 2 part together
		basePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
				
		//Split only 2 horizontal part
		gbc.weighty = 1;
				
		//Setup for the left panel
		gbc.weightx = 0.05;
		gbc.gridx = 0;
		basePanel.add(leftPanel, gbc);
				
		//Setup for the right panel
		gbc.weightx = 1 - gbc.weightx;
		gbc.gridx = 1;
		basePanel.add(rightPanel, gbc);
	}
	
	//Init for the MainJFrame
	protected void initMainView() {
		
		//Init properties of main JFrame
		setTitle("Quản lý bán vé máy bay");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		
		//Init value for the components
		constructComponents();
		initValueForComponents();
		
		//Setup left and right part of the main JFrame
		initLeftPanel();
		initRightPanel();
		
		//Combine 2 parts together
		combineLeftRightPanel();
			
		//Activate the JFrame
        setContentPane(basePanel);
        setLocationRelativeTo(null);
        
        //Design the panels
        designPanel();
	}
	
	public BaseMainFrame() {
		initMainView();
	}

	public List<BaseFeatureView> getFeatureViews() {
		return featureViews;
	}
	
	protected void designPanel() {
		basePanel.setBackground(Color.WHITE);
		basePanel.setBorder(new EmptyBorder(10, 8, 0, 16));

		leftPanel.setBackground(Color.WHITE);
		if (categories.size() > 1) {
			leftPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
		}
		else {
			leftPanel.setBorder(new EmptyBorder(0, 0, 0, 8));
		}
	}
}

