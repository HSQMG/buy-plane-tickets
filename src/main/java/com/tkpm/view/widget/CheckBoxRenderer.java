package com.tkpm.view.widget;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer  {
  
	public CheckBoxRenderer() {
    setOpaque(true);
  }
  
  @Override
  public Component getTableCellRendererComponent(
		  JTable table, 
		  Object value,
		  boolean isSelected, 
		  boolean hasFocus, 
		  int row, 
		  int column) {
	  

    return this;
  }
}