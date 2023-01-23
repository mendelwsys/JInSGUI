package com.mwlib.jalv.tbl;



import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


public class GroupableTableHeader extends JTableHeader
{
//	private static final String uiClassID = "GroupableTableHeaderUI";
	protected Vector columnGroups = null;

	public GroupableTableHeader(TableColumnModel model)
	{
		super(model);
		for (int i = 0; i < model.getColumnCount(); i++)
			model.getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer()
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
					return createTableMultiLineHeader(lines, header.getFont());// new Font("Dialog", 0, 10));

//					return MethodExtentions.createTableMultiLineHeader(lines, new Font("Dialog", 0, 10));

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
			});

		GroupableTableHeaderUI tableHeaderUI = new GroupableTableHeaderUI();
		setUI(tableHeaderUI);
		setReorderingAllowed(false);
	}

	public void setReorderingAllowed(boolean b)
	{
		reorderingAllowed = false;
	}

	public void addColumnGroup(ColumnGroup g)
	{
		if (columnGroups == null)
		{
			columnGroups = new Vector();
		}
		columnGroups.addElement(g);
	}

	public Enumeration getColumnGroups(TableColumn col)
	{
		if (columnGroups == null) return null;
		Enumeration en = columnGroups.elements();
		while (en.hasMoreElements())
		{
			ColumnGroup cGroup = (ColumnGroup) en.nextElement();
			Vector v_ret = cGroup.getColumnGroups(col, new Vector());
			if (v_ret != null)
			{
				return v_ret.elements();
			}
		}
		return null;
	}

	public void setColumnMargin()
	{
		if (columnGroups == null) return;
		int columnMargin = getColumnModel().getColumnMargin();
		Enumeration en = columnGroups.elements();
		while (en.hasMoreElements())
		{
			ColumnGroup cGroup = (ColumnGroup) en.nextElement();
			cGroup.setColumnMargin(columnMargin);
		}
	}

	public void resetColumnGroups()
	{
		if (columnGroups!=null)
			columnGroups.clear();
	}

	 public static JPanel createTableMultiLineHeader(String[] lines) {
		 return createTableMultiLineHeader(lines, new Font("Dialog", 0, 12));
	 }

	 public static JPanel createTableMultiLineHeader(String[] lines, Font font) {
		 JPanel p = new JPanel(new GridLayout(lines.length, 1, 1, 1));
		 p.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

		 for(int i = 0; i < lines.length; ++i) {
			 JLabel l = createLabel(lines[i]);
			 l.setFont(font);
			 l.setHorizontalAlignment(0);
			 p.add(l);
		 }

		 return p;
	 }
		public static JLabel createLabel(String label) {
		 JLabel l = new JLabel(label);
		 l.setFont(new Font("dialog", 0, 12));
		 l.setForeground(Color.black);
		 return l;
	 }

}