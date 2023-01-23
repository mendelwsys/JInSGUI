package com.mwlib.jalv.tbl;

import tst.ICommonGuiStub;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 15.10.2021
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class GridTest {


    public static void main(String[] args) {

	 new GridTest().test();

    }


    class TestEditorModel
    			extends AbstractTableModel
    	{

			@Override
			public int getRowCount() {
				return 10;
			}

			@Override
			public int getColumnCount() {
				return 6;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				Object obj=mp.get(""+rowIndex+"_"+columnIndex);
				if (obj==null)
					return rowIndex*getColumnCount()+columnIndex+1+100;
				return obj;
			}


			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return true;
			}

			private Map<String,Object> mp=new HashMap();

			public void setValueAt(Object obj, int row, int col)
			{
				mp.put(""+row+"_"+col,obj);
			}

//			public String getColumnName(int col)
//			{
//					return ""+(col+1);//return RangeFactory.getFatory().getColumnName(this.typeRange,col);
//			}

		}


    public void test()
    {
		final JFrame  testFrame =new JFrame();
		final JTable tableC = prepareTable();
		tableC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableC.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableC.getColumnModel().getColumn(1).setPreferredWidth(100);
		JScrollPane scrollPanelC = new JScrollPane();



//		tableC.getDefaultEditor().addCellEditorListener(new CellEditorListener() {
//			@Override
//			public void editingStopped(ChangeEvent e) {
//
//			}
//
//			@Override
//			public void editingCanceled(ChangeEvent e) {
//
//			}
//		});

//		final JPopupMenu popupMenu = new JPopupMenu();
//		popupMenu.add(MethodExtentions.createMenuItem("Копировать данные", "", "Copy_data", new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Component cl = tableC.getEditorComponent();
//				if (cl!=null)
//				{
//					System.out.println("cl = " + cl);
//				}
//
//			}
//		}, tableC));
//		popupMenu.add(MethodExtentions.createMenuItem("Вставить данные", "", "Paste_data", new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//			}
//		}, tableC));

//  	  tableC.addMouseListener(new MouseListener() {
//      @Override
//      public void mouseClicked(MouseEvent e)
//      {
//
////                System.out.println("e = " + frame.isFocusable()+" "+ServerJGridBean01.this.isFocusable()+" "+table.isFocusable());
////                table.setFocusCycleRoot(true);
////                Component cmp = table.getEditorComponent();
////                if (cmp!=null)
////                {
////                    cmp.requestFocus();
////                }
////                ServerJGridBean01.this.requestFocus();
//
////                boolean b=ServerJGridBean01.this.isFocusCycleRoot();
////                System.out.println("b1 = " + b);
////                b=getParentFrame().isFocusCycleRoot();
////                System.out.println("b2 = " + b);
////                if (bridge!=null)
////                    bridge.putCtrlEvent(new CtrlCmd("CHECK_CMD", "PARAMS"));
//      }
//
//      @Override
//      public void mousePressed(MouseEvent e) {
//
//      }
//
//      @Override
//      public void mouseReleased(MouseEvent e) {
//          JComponent pparentFrame = table;
//          JPopupMenu popupMenu = new JPopupMenu();
//          popupMenu.show(pparentFrame, e.getX(), e.getY());
//          pparentFrame.requestFocus();
//          popupMenu.setVisible(false);
//          popupMenu.removeAll();
//          pparentFrame.remove(popupMenu);
//
//      }
//
//      @Override
//      public void mouseEntered(MouseEvent e) {
//
//      }
//
//      @Override
//      public void mouseExited(MouseEvent e) {
//
//      }
//  });


		scrollPanelC.setViewportView(tableC);

		FixedColumnTable fct = new FixedColumnTable(2, scrollPanelC);


  		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  		testFrame.setTitle("Тестовая программа для таблицы  с заголовком");
//  		testFrame.setContentPane(scrollPanelC);
		testFrame.add(scrollPanelC);

		testFrame.pack();

	    int wPos = (int) (screenSize.getWidth()/2 - testFrame.getWidth()/2);
	    int hPos = (int) (screenSize.getHeight()/2 - testFrame.getHeight()/2);
	    testFrame.setLocation(wPos,hPos);


//		tableC.getParent().addFocusListener(new FocusListener() {
//			@Override
//			public void focusGained(FocusEvent e) {
//				Component cl = tableC.getEditorComponent();
//				if (cl!=null)
//				{
//					System.out.println("cl = " + cl);
//				}
//
//			}
//
//			@Override
//			public void focusLost(FocusEvent e) {
//
//			}
//		});


        testFrame.setVisible(true);
    }

	public JTable prepareTable()
	{
		TestEditorModel model = new TestEditorModel();
		JTable tableC = new JGrid(model);
//		{
		//public TableCellEditor getCellEditor(int row, int column) {
		//       TableColumn tableColumn = getColumnModel().getColumn(column);
		//       TableCellEditor editor = tableColumn.getCellEditor();
		//       if (editor == null) {
		//           editor = getDefaultEditor(getColumnClass(column));
		//       }
		//       return new  DefaultCellEditor( new JTextField() );
		////	   {
		////		   public boolean isFocusTraversable() {
		////		       				return true;
		////					}
		////
		////	   });
		//   }
		//
		//			public boolean editCellAt(int row, int column, EventObject e)
		//			{
		//				boolean rv=super.editCellAt(row,column,e);
		//				Component comp = getEditorComponent();
		//				if (comp!=null)
		//				{
		//					System.out.println("comp = " + comp);
		//					comp.requestFocus();
		//				}
		//				return rv;
		//			}

		//			public boolean editCellAt(int row, int column, EventObject e)
		//			{
		//
		//       if (cellEditor != null && !cellEditor.stopCellEditing()) {
		//           return false;
		//       }
		//
		//       if (row < 0 || row >= getRowCount() ||
		//           column < 0 || column >= getColumnCount()) {
		//           return false;
		//       }
		//
		//       if (!isCellEditable(row, column))
		//           return false;
		//
		////       if (editorRemover == null) {
		////           KeyboardFocusManager fm =
		////               KeyboardFocusManager.getCurrentKeyboardFocusManager();
		////           editorRemover = new CellEditorRemover(fm);
		////           fm.addPropertyChangeListener("permanentFocusOwner", editorRemover);
		////       }
		//
		//       TableCellEditor editor = getCellEditor(row, column);
		//       if (editor != null) {// && editor.isCellEditable(e)) {
		//           editorComp = prepareEditor(editor, row, column);
		//           if (editorComp == null) {
		//               removeEditor();
		//               return false;
		//           }
		//           editorComp.setBounds(getCellRect(row, column, false));
		//           add(editorComp);
		//           editorComp.validate();
		//           editorComp.repaint();
		//
		//           if (editorComp instanceof JTextField)
		//		   {
		//		   	editorComp.addKeyListener(new KeyListener() {
		//				@Override
		//				public void keyTyped(KeyEvent e) {
		//					System.out.println("keyTyped = " + e);
		//				}
		//
		//				@Override
		//				public void keyPressed(KeyEvent e) {
		//					System.out.println("keyPressed = " + e);
		//				}
		//
		//				@Override
		//				public void keyReleased(KeyEvent e) {
		//
		//				}
		//			});
		////			  KeyboardFocusManager fm =
		////				  KeyboardFocusManager.getCurrentKeyboardFocusManager();
		////			   Window wnd = fm.getActiveWindow();
		////			  fm.clearGlobalFocusOwner();
		////			   Container fc = fm.getCurrentFocusCycleRoot();
		////			   if (fc==null)
		////			   {
		////				   fm.setGlobalCurrentFocusCycleRoot(frame);
		//////				   frame.requestFocus();
		////			   }
		//
		//		   }
		//
		//           setCellEditor(editor);
		//           setEditingRow(row);
		//           setEditingColumn(column);
		//           editor.addCellEditorListener(this);
		//
		//		   frame.setFocusCycleRoot(false);
		//		   panel.setFocusCycleRoot(true);
		//
		//           editorComp.requestFocus();
		//
		//
		////		  frame.requestFocus();
		////		  frame.setFocusable(true);
		////		  frame.setAutoRequestFocus(true);
		////		  frame.setAlwaysOnTop(true);
		////		  frame.requestFocus();
		//
		//		  return true;
		//       }
		//       return false;
		//   }
		//			public Component prepareEditor(TableCellEditor editor, int row, int column)
		//			{
		////								Object value = getValueAt(row, column);
		////					boolean isSelected = isCellSelected(row, column);
		////					Component comp = editor.getTableCellEditorComponent(this, value, isSelected,
		////															  row, column);
		////					if (comp instanceof JComponent) {
		////						JComponent jComp = (JComponent)comp;
		////						if (jComp.getNextFocusableComponent() == null) {
		////							jComp.setNextFocusableComponent(this);
		////						}
		////					}
		//				    Component comp = super.prepareEditor(editor, row, column);
		//				    if (comp instanceof JTextField)
		//					{
		//						//((JTextField)comp).;
		////						Container parent = comp.getParent();
		////						parent.setComponentZOrder(comp,-1);
		//					}
		//
		//					comp.setFocusable(true);
		//					comp.requestFocus();
		//					return comp;
		//			}

		//			public void removeEditor()
		//			{
		//				super.removeEditor();
		//			}
		//		};


		tableC.getTableHeader().setUI(new GroupableTableHeaderUI());
		buildHeaders(tableC);
		return tableC;
	}

	private void buildHeaders(JTable table)
   	{

   		TableColumnModel cm = table.getColumnModel();
   		cm.setColumnMargin(0);
//   		TestEditorModel editorModel = (TestEditorModel) table.getModel();

//   		THeader[] headers = new THeader[]{};

				//RangeFactory.getFatory().getColHeadersByType(editorModel.getTypeRange());


   		GroupableTableHeader g_header = (GroupableTableHeader) table.getTableHeader();
   		g_header.resetColumnGroups();


   		ColumnGroup g_group = new ColumnGroup(
   				//editorModel.getTypeRange().equals(RangeFactory.H1_NMR) ?
                        "Иерархичный заголовок\nподзаголовок иерархичный" );
                        //: "13C intervals");

   		int index=0;
//   		for (THeader header : headers)
//		index = headersfromroot(header.getRoot(), g_group, cm, index);

		ICommonGuiStub.FName2VarStruct[] nodes =  new ICommonGuiStub.FName2VarStruct[]{
	    		   new ICommonGuiStub.FName2VarStruct("Первый\nПодзаголовок первый",new String[]{"11\nПодзаголовок\n11+1","2"}),
				   new ICommonGuiStub.FName2VarStruct("Второй", null,
						   new ICommonGuiStub.FName2VarStruct[]{
				   		new ICommonGuiStub.FName2VarStruct("Первый 2\nПервый 221",new String[]{"Разность","4"}),
								   new ICommonGuiStub.FName2VarStruct("Второй 2",new String[]{"Сумма\nРазность","Вычет"})})
		}                                                      ;

		for (ICommonGuiStub.FName2VarStruct node : nodes) {
			index = headersfromroot(node, g_group, cm, index);
		}
   		g_header.addColumnGroup(g_group);
   	}

    private int headersfromroot(ICommonGuiStub.FName2VarStruct root, ColumnGroup grp, TableColumnModel cm, int index)
   	{
   		ICommonGuiStub.FName2VarStruct[] chnodes = root.getSUB_FNAME2VAR();
   		if (chnodes !=null)
   		{
   			ColumnGroup g_name = new ColumnGroup(root.getVARNAME());
   			for (ICommonGuiStub.FName2VarStruct chnode : chnodes)
   				index=headersfromroot(chnode,g_name,cm,index);
   			grp.add(g_name);

   		}
   		else
   		{
			TableColumn column = cm.getColumn(index);
			column.setHeaderValue(root.getVARNAME());
			grp.add(column);
   			index++;
   		}
   		return index;
   	}


}
