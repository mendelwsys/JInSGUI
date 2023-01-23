package tst.ev;

import tst.CmdConst;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 14.02.18
 * Time: 15:03
 * Мост для сокетной версии
 */
public class BridgeCmdCtrl2 implements IBridgeCmdCtrl
{

//    public void setSessionId(long sessionId) {
//        this.sessionId = sessionId;
//    }
//    public long getSessionId() {
//        return sessionId;
//    }
//    private long sessionId;

//    public void setObject(Object object) {
//        this.object = object;
//    }
//
//    private Object object;



    public BridgeCmdCtrl2()
    {

    }
    private BlockingQueue<CtrlCmd> outCmdQueue = new LinkedBlockingDeque<CtrlCmd>();

//    private BlockingQueue<CtrlCmd> outCmdQueue_deb = new LinkedBlockingDeque<CtrlCmd>();
    private BlockingQueue<Long> inOutController = new LinkedBlockingDeque<Long>();


    public String getOutCtrlCommands(long timeOut)
    {
            if (timeOut<=0)
                return getCtrlCommands(outCmdQueue);
            else
                return getCtrlCommands(timeOut,outCmdQueue);

    }

    @Override
    public String getOutCtrlCommands() {
        return getCtrlCommands(outCmdQueue);
    }


    private String getCtrlCommands(BlockingQueue<CtrlCmd> outCmdQueue)
    {
             CtrlCmd gs;
             StringBuffer sb= new StringBuffer();
             while ((gs= outCmdQueue.poll())!=null)
             {
                 if (sb.length()>0)
                    sb.append(CmdConst.CMD_DL);
                 gs.buildString(sb);
             }
            if (sb.length()>0)
                return sb.toString();
            return null;
    }

    private String getCtrlCommands(long timeOut,BlockingQueue<CtrlCmd> outCmdQueue)
    {
        try {
            CtrlCmd gs;
            StringBuffer sb= new StringBuffer();
            while ((gs= outCmdQueue.poll(timeOut, MILLISECONDS))!=null)
            {
                if (sb.length()>0)
                   sb.append(CmdConst.CMD_DL);
                gs.buildString(sb);
                timeOut=0;
            }
            if (sb.length()>0)
                return sb.toString();
        }
        catch (InterruptedException e)
        { //
        }
        return null;
    }


    private BlockingQueue<CtrlCmd> inCmdQueue = new LinkedBlockingDeque<CtrlCmd>();


    public String putInCmd(String cmdString)
    {
        CtrlCmd ctrlCmd = new CtrlCmd();
//        System.out.println("cmdString = " + cmdString);
        ctrlCmd.initByStringParam(cmdString);
//        System.out.println("initByStringParam OK : ctrlCmd = " + ctrlCmd.getParams()+" "+ctrlCmd.getCmd());
        inCmdQueue.add(ctrlCmd);
//        System.out.println("add Ok Params: " + ctrlCmd.getParams());
        System.out.println("answer for command: " + ctrlCmd.getCmd()+" was add to inCmdQueue");
        return new CtrlCmd(CmdConst.CMD_OK).buildString(new StringBuffer()).toString();
    }

    public CtrlCmd getInCtrlCommand(long timeOut) throws InterruptedException
    {
            return  inCmdQueue.poll(timeOut, MILLISECONDS);
    }

    public int getOutCtrlCommandSize() throws InterruptedException
    {
            return  outCmdQueue.size();
    }

    public int getInCtrlCommandSize() throws InterruptedException
    {
            return  inCmdQueue.size();
    }

    public void postPoneFireEvent(final CtrlCmd ctrlCmd)
    {
        new Thread(new Runnable(){

            public void run()
            {
                try {
                    Thread.sleep(10);
                    putCtrlEvent(ctrlCmd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void fireEvent() {
    }

    @Override
    public void fireEvent(MyEvent event) {

    }

    public void putCtrlEvent(CtrlCmd e)
    {
        outCmdQueue.add(e);

        String params = e.getParams();
        int ix=-1;
        if (params!=null && (ix=params.indexOf("?"))>=0)
        {
            params = params.substring(0,ix);
            System.out.println("Cmd = " + e.getCmd()+" "+" was add to outCmdQueue "+params);
        }
        else
            System.out.println("Cmd = " + e.getCmd()+" "+" was add to outCmdQueue ");

    }

    private volatile boolean shuttingDownBridge =false;
    public void shuttingDownBridge()
    {
        shuttingDownBridge =true;
    }

    private volatile boolean stopBridge =false;
    public void stopBridge()
    {
        stopBridge =true;
    }


    public CtrlCmd syncCtrlEvent(CtrlCmd e) throws BridgeException, RequestException {


        synchronized (this)
        {
            if (inOutController.size()>0)
                System.out.println("!!!Reenterant!!!");
            inOutController.add(1l);
        }


        try {
            if (stopBridge)
                throw new BridgeException("Bridge was stopped");

            boolean user_logout = false;
            if (shuttingDownBridge)
            {
                String params = e.getParams();
                user_logout = params != null && (params.startsWith("z_00b_user_logout?") || params.startsWith("user_logout?"));
                if (!user_logout)
                    throw new BridgeException("Bridge shutting down");
            }

//            { //Отладочный код
//                e.setSessionId(sessionId);
//                outCmdQueue_deb.add(e);
//            }

//       try {//Отладочный код
//                int inCmdSize = getInCtrlCommandSize();
//                if (inCmdSize>0)
//                {
//                    System.out.println("inCmdSize = " + inCmdSize);
//                }
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }

            putCtrlEvent(e);

            CtrlCmd retCmd;
            //Впадаем в ожидание пока придет реакция на эту команду, при этом блокируем дальнейшее исполнение команд
            long tmCounter = System.currentTimeMillis();
            for (int i = 0;;i++)
            {
                try
                {
                    retCmd = getInCtrlCommand(100);
                    if (shuttingDownBridge && !user_logout)
                        throw new BridgeException("Bridge shutting down");
                    if (stopBridge)
                        throw new BridgeException("Bridge was stopped");
                    if (retCmd!=null && CmdConst.CMD_BRIDGE_ERROR.equals(retCmd.getCmd()))
                        throw new BridgeException("Get error command: "+retCmd.getCmd()+" "+retCmd.getParams());

                    if (retCmd!=null && e.getIdCmd().equals(retCmd.getIdCmd()))
                    {
                        if (CmdConst.CMD_REQUEST_ERROR.equals(retCmd.getCmd()))
                            throw new RequestException("403",retCmd.getParams());

                        if (e.getCmd().equals(retCmd.getCmd()))
                            break;
                        else
                           throw new BridgeException(" Put: "+ e.getCmd()+" "+e.getParams()+" Get: "+retCmd.getCmd()+" "+retCmd.getParams());
                    }
                    else //Ожидаем что после подачи этой команды мы уже имеем инициализированные внешнией системой набор переменных через fillFieldValues
                    {

                        if (retCmd!=null)
                        {
                            //Проблема в том что UserInfo содержит статический currentUser на который ссылаются многие классы
                            //Поэтому происходит двойной вызов этого метода из разных потоков
                            //Проблема оcталась поскольу производится при вызове сommonfunction  производится вызов current_parameters.setUserInfo(UserInfo.currentUser); где устанавливается сессия которая может
                            // не совпадать с сессией текущего открытого документа
                            System.out.println("Skip the command with id: "+retCmd.getIdCmd()+" "+retCmd.getCmd()+" "+retCmd.getParams()+" Waiting for command with id:"+e.getIdCmd());
//                            throw new BridgeException("Got command with id: "+retCmd.getIdCmd()+" "+retCmd.getCmd()+" "+retCmd.getParams()+" Wait command with id:"+e.getIdCmd());
                        }


                        long spendTime = (System.currentTimeMillis() - tmCounter) / 1000;
                        if (getOutCtrlCommandSize()>0)
                        {
    //TODO Закоментировали для отладки, ответа ждем вечно.
    //                      if (spendTime>300)
    //                      {
    //                          System.out.println(" System waiting error: Event is not taken by backEnd: tm="+spendTime);
    //                          throw new BridgeException("Bridge connection to Error");
    //                      }
                        }
                        if (i%20==0)
                            System.out.println("Waiting for answer tm =" + spendTime);
                    }
                }
                catch (InterruptedException e1)
                {
                  e1.printStackTrace();
                }
            }
            return retCmd;
        }
        finally
        {
            synchronized (this)
            {
                try {
                    inOutController.poll(100,MILLISECONDS);
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        }

    }


}
