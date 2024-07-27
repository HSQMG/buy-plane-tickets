package com.tkpm.view;

import javax.swing.JPanel;

public class BaseView extends JPanel implements ViewBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5172795028647841915L;

	@Override
	public BaseView open() {
		this.setVisible(true);
		//this.pack();
		
		return this;
	}
	
	@Override
	public void close() {
		this.setVisible(false);
	}
}
