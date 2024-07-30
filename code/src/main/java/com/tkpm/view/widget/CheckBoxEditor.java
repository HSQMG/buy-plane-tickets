package com.tkpm.view.widget;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class CheckBoxEditor extends DefaultCellEditor {
     private JCheckBox checkBox;
    
    public CheckBoxEditor(JCheckBox checkBox) {
      super(new JCheckBox());
      this.checkBox = checkBox;
    }
    
    public Component getTableCellEditorComponent(
    		JTable table, 
    		Object value,
    		boolean isSelected, 
    		int row, 
    		int column) {
    	
      return checkBox;
    }
 
 }

