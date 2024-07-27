package com.tkpm.view.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

import com.tkpm.view.feature_view.BaseFeatureView;
import com.tkpm.view.feature_view.detail_view.BaseReadOnlyDetailView;
import com.tkpm.view.feature_view.tabbed_controller_view.BaseTabbedControllerView;


public class ChangeTabListener implements MouseListener  {
	
	private BaseTabbedControllerView tabbedControllerView;
	private List<JPanel> parts;
	
	public ChangeTabListener(BaseTabbedControllerView displayTableView, List<JPanel> parts) {
		this.tabbedControllerView = displayTableView;
		this.parts = parts;

	}
	
	public void mouseClicked(MouseEvent e) {
         //do nothing
    }


	public void mouseEntered(MouseEvent arg0) {
		//do nothing
	}


	public void mouseExited(MouseEvent arg0) {
		//do nothing
	}


	//Change detail and header while change tab
	public void mousePressed(MouseEvent arg0) {
		int selectedIndex = tabbedControllerView.getTabbedPanel().getSelectedIndex();
		
		Component centerView = tabbedControllerView.getCenterViews().get(selectedIndex);
		JPanel detailView = tabbedControllerView.getDetailViews().get(selectedIndex);
		JPanel headerView = tabbedControllerView.getHeaderViews().get(selectedIndex);
		
        tabbedControllerView.setCurrentCenterView(centerView);
        tabbedControllerView.setCurrentDetailView(detailView);
        tabbedControllerView.setCurrentHeaderView(headerView);
        
        parts.forEach((part) -> {
        	part.removeAll();
        });  
      
        parts.get(BaseFeatureView.CENTER_INDEX).add(tabbedControllerView, BorderLayout.CENTER);
        parts.get(BaseFeatureView.DETAIL_INDEX).add(detailView, BorderLayout.CENTER);
        parts.get(BaseFeatureView.HEADER_INDEX).add(headerView, BorderLayout.CENTER);
			
//        displayTableView.getCurrentDetailView().setBackground(new Color(204, 204, 255));
//        displayTableView.getCurrentHeaderView().setBackground(UIManager.getColor("activeCaptionBorder"));
        
        parts.forEach((part) -> {
        	part.repaint();
        });
//        parts.get(BaseFeatureView.HEADER_INDEX).repaint();
//        parts.get(BaseFeatureView.DETAIL_INDEX).repaint();
	}


	public void mouseReleased(MouseEvent arg0) {
		//do nothing
	}
}
