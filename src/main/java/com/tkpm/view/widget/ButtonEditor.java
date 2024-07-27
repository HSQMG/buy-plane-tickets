package com.tkpm.view.widget;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class ButtonEditor extends DefaultCellEditor {
     private JButton button;
    
    public ButtonEditor(JButton button) {
      super(new JCheckBox());
      this.button = button;
    }
    
    public Component getTableCellEditorComponent(
    		JTable table, 
    		Object value,
    		boolean isSelected, 
    		int row, 
    		int column) {
    	
      return button;
    }
 
 }

