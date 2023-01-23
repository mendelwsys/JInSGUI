package com.mwlib.jalv.tbl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 15.10.2021
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class JGrid 	extends JTable
	{

		public JGrid()
		{
		}

		public JGrid(TableModel dm)
		{
			super(dm);
		}

		protected JTableHeader createDefaultTableHeader()
		{
			return new GroupableTableHeader(columnModel);
		}

		public void setModel(TableModel dataModel)
		{
			super.setModel(dataModel);
			TableColumnModel model = getColumnModel();
			for (int i = 0; i < model.getColumnCount(); i++)
				model.getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer()
				{
					public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row,
                                                                   int column)
					{
						JTableHeader header = table.getTableHeader();
						if (header != null)
						{
							setForeground(header.getForeground());
							setBackground(header.getBackground());
							setFont(header.getFont());
						}
						setHorizontalAlignment(JLabel.CENTER);
						setText((value == null) ? "" : value.toString());
						setBorder(UIManager.getBorder("TableHeader.cellBorder"));
						return this;
					}
				});
		}
	}

