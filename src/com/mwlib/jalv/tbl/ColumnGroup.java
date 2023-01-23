package com.mwlib.jalv.tbl;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


public class ColumnGroup
{
	protected TableCellRenderer renderer;
	protected Vector v;
	protected String text;
	protected int margin = 0;

	public ColumnGroup(String text)
	{
		this(null, text);
	}

	public ColumnGroup(TableCellRenderer renderer, String text)
	{
		if (renderer == null)
		{
			this.renderer = new DefaultTableCellRenderer()
			{
				public Component getTableCellRendererComponent(JTable table, Object value,
															   boolean isSelected, boolean hasFocus, int row, int column)
				{


					String lines[] = new String[0];
					if (value!=null)
					{
						String sVal=value.toString();
						lines=sVal.split("\n");
					}

					JTableHeader header = table.getTableHeader();
					return GroupableTableHeader.createTableMultiLineHeader(lines, header.getFont());// new Font("Dialog", 0, 10));

//
//					JTableHeader header = table.getTableHeader();
//					if (header != null)
//					{
//						setForeground(header.getForeground());
//						setBackground(header.getBackground());
//						setFont(header.getFont());
//					}
//					setHorizontalAlignment(JLabel.CENTER);
//					setText((value == null) ? "" : value.toString());
//					setBorder(UIManager.getBorder("TableHeader.cellBorder"));
//					return this;
				}
			};
		}
		else
		{
			this.renderer = renderer;
		}
		this.text = text;
		v = new Vector();
	}


	public void add(Object obj)
	{
		if (obj == null)
		{
			return;
		}
		v.addElement(obj);
	}

	public Vector getColumnGroups(TableColumn c, Vector g)
	{
		g.addElement(this);
		if (v.contains(c)) return g;
		Enumeration en = v.elements();
		while (en.hasMoreElements())
		{
			Object obj = en.nextElement();
			if (obj instanceof ColumnGroup)
			{
				Vector groups =
						(Vector) ((ColumnGroup) obj).getColumnGroups(c, (Vector) g.clone());
				if (groups != null) return groups;
			}
		}
		return null;
	}

	public TableCellRenderer getHeaderRenderer()
	{
		return renderer;
	}

	public void setHeaderRenderer(TableCellRenderer renderer)
	{
		if (renderer != null)
		{
			this.renderer = renderer;
		}
	}

	public Object getHeaderValue()
	{
		return text;
	}

	public Dimension getSize(JTable table)
	{
		Component comp = renderer.getTableCellRendererComponent(table, getHeaderValue(), false, false, -1, -1);
		int height = comp.getPreferredSize().height;
		int width = 0;
		Enumeration en = v.elements();
		while (en.hasMoreElements())
		{
			Object obj = en.nextElement();
			if (obj instanceof TableColumn)
			{
				TableColumn aColumn = (TableColumn) obj;
				width += aColumn.getWidth();
				width += margin;
			}
			else
			{
				width += ((ColumnGroup) obj).getSize(table).width;
			}
		}
		return new Dimension(width, height);
	}

	public void setColumnMargin(int margin)
	{
		this.margin = margin;
		Enumeration en = v.elements();
		while (en.hasMoreElements())
		{
			Object obj = en.nextElement();
			if (obj instanceof ColumnGroup)
			{
				((ColumnGroup) obj).setColumnMargin(margin);
			}
		}
	}
}