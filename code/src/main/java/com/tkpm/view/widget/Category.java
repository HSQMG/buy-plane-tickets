package com.tkpm.view.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class Category extends JPanel {
	
	/****Private attribute ****/
	//Name of the Category
	private String name;
	
	public static final Color BORDER_COLOR = UIManager.getColor("activeCaptionBorder");
	
	//The option button on the left side of main menu
	private JButton button;
	
	//TOP, LEFT, DOWN, RIGHT borders while add into GridBagLayout
	private List<JPanel> borders;	
	
	/**** Private method ****/
	void setLayout() {
		double borderSize = 0.2;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weighty = borderSize;
		gbc.gridwidth = 3;
		//this.add(borders.get(0), gbc);
		
		gbc.gridwidth = 1;
		gbc.weightx = borderSize;
		gbc.weighty = 1 - 2 *borderSize;
		gbc.gridy = 1;
		//this.add(borders.get(1), gbc);
		
		gbc.gridx = 2;
		//this.add(borders.get(2), gbc);
		
		gbc.gridx = 1;
		this.add(button, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.weighty = borderSize;
		gbc.gridwidth = 3;
		//this.add(borders.get(3), gbc);
	
	}
	
	
	/**** Constructor ****/
	public Category(String name) {
		
		this.name = name;
		borders = new ArrayList<JPanel>();
		for (int i = 0; i < 4; ++i) {
			borders.add(new JPanel());
			borders.get(i).setBackground(BORDER_COLOR);
		}
		button = new JButton(name);
		
		setLayout();
		editButton();
		
	}
	
	/**** Getter ****/
	public String getName() {return name; }
	public JButton getButton() {return button;}
	public List<JPanel> getBorders() {return borders;}
	
	private void editButton() {
		button.setBorder(null);
	}
	
}

