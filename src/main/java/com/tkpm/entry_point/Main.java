package com.tkpm.entry_point;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.tkpm.controller.LoginController;

public class Main {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
			UIManager.put("Component.arrowType", "chevron");
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		LoginController controller = new LoginController();
		controller.run();
	}

}
