package com.tkpm.view.feature_view.header_view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BaseHeader extends JPanel{

	private List<JPanel> panels;
	
	private static final int NAV_BAR_PANEL_INDEX = 0;
	
	private JButton logoutButton;
	
	private void initComponents() {
		panels = new ArrayList<>(Arrays.asList(
					new JPanel()
				));
		logoutButton = new JButton("Đăng xuất");
	}
	
	private void reupdatePanels() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (JPanel panel: panels) {
			panel.setBackground(Color.WHITE);
			add(panel);
		}
	}
	
	public void setupLayout() {
		reupdatePanels();
		
		//Setup the nav bar
		JPanel navBarPanel = panels.get(NAV_BAR_PANEL_INDEX);
		navBarPanel.setLayout(new BoxLayout(navBarPanel, BoxLayout.X_AXIS));
		navBarPanel.add(Box.createHorizontalGlue());
		navBarPanel.add(logoutButton);
		navBarPanel.add(Box.createRigidArea(new Dimension(10, 0)));
	}
	
	public BaseHeader() {
		initComponents();
		setupLayout();
		setBackground(Color.WHITE);
	}
	
	public BaseHeader(JPanel panel) {
		initComponents();
		panels.add(panel);
		setupLayout();
	}
	public JButton getLogoutButton() {return logoutButton;}
	public List<JPanel> getPanels() {return panels;}
}
