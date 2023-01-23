package com.mwlib.jalv.tbl;

import tst.IGridStub;
import tst.ev.IBridgeCmdCtrl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 15.10.2021
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public class ServerJGridBean01 extends JScrollPane
    implements IGridStub
{
    public ServerJGridBean01(Component view, int vsbPolicy, int hsbPolicy)
    {
        super(view, vsbPolicy, hsbPolicy);
    }

    public ServerJGridBean01(Component view) {
        super(view);
    }

    public ServerJGridBean01(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
    }

    public ServerJGridBean01() {
    }

    @Override
    public void initBeanByParameters(String in_params) {
    }

    @Override
    public String putInCmd(String inCmd) {
        return null;
    }


    @Override
    public void showDocument(String mode, String in_parameters)
    {
        //Инициализация таблицы, по параметрам, заголовок + модель данных
        //Вставка таблицы в текущий компонент
        final Frame frame = this.getParentFrame();
        final JTable table = new GridTest().prepareTable();
        this.setViewportView(table);
//        FixedColumnTable fct = new FixedColumnTable(2, this);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                resetFocusOnCompByMouse(e, table);     //TODO Можно и здесь

//                System.out.println("e = " + frame.isFocusable()+" "+ServerJGridBean01.this.isFocusable()+" "+table.isFocusable());
//                table.setFocusCycleRoot(true);
//                Component cmp = table.getEditorComponent();
//                if (cmp!=null)
//                {
//                    cmp.requestFocus();
//                }
//                ServerJGridBean01.this.requestFocus();

//                boolean b=ServerJGridBean01.this.isFocusCycleRoot();
//                System.out.println("b1 = " + b);
//                b=getParentFrame().isFocusCycleRoot();
//                System.out.println("b2 = " + b);
//                if (bridge!=null)
//                    bridge.putCtrlEvent(new CtrlCmd("CHECK_CMD", "PARAMS"));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
//                resetFocusOnCompByMouse(e, table); //TODO фокусировка на поле ввода
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        frame.setAlwaysOnTop(true);
        frame.toFront();
    }

    public void resetFocusOnCompByMouse(MouseEvent e, JTable table) {
        //TODO 200211126 А без этого не будет работать фокус ввода !!!!@#$%!!! при выполнении из под SAP GUI
        //TODO Потртил неделю на этот ..., почему вопрос, можно наврное и диалог использовать для этого, или окно какое
        JComponent pparentFrame = table;
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.show(pparentFrame, e.getX(), e.getY());
        pparentFrame.requestFocus();
        popupMenu.setVisible(false);
        popupMenu.removeAll();
        pparentFrame.remove(popupMenu);
    }


    private IBridgeCmdCtrl bridge;
    public void setBridge(IBridgeCmdCtrl bridge)
    {
             this.bridge = bridge;
    }


    protected Frame parentFrame;
    public Frame getParentFrame()
    {
        if (parentFrame == null)
        {
            Component c;
            for(c = this; c.getParent() != null; c = c.getParent())
                if(c instanceof Frame)
                    return (Frame)c;

            if(c instanceof Frame)
                return parentFrame=(Frame)c;
            else
                return parentFrame=new Frame();
        }
        return parentFrame;
    }

    public String testExChange(String testString)
    {
        System.out.println("testString = " + testString);
        return testString+"_ALIVE";
    }

}
