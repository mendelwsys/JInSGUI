package tst.interprocess;

import tst.*;
import tst.ev.BridgeCmdCtrl2;
import tst.ev.CtrlCmd;
import tst.ev.IBridgeCmdCtrl;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 05.02.18
 * Time: 13:25
 * Сервер для взаимодействия с dll через сокеты.
 */
public class Server
{
    final static AtomicLong inCommand =  new AtomicLong(0);
    final static AtomicLong outCommand =  new AtomicLong(0);

//    public static String compressString(String forCompress, String charsetName) throws IOException
//    {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        OutputStream zout=new DeflaterOutputStream(out,new Deflater(Deflater.DEFAULT_COMPRESSION, true));
//
//        try {
//            zout.write(forCompress.getBytes(charsetName));
//            zout.flush();
//            zout.close();
//            return new BASE64Encoder().encode(out.toByteArray());
//        }
//        finally
//        {
//            try {
//                    zout.close();
//            } catch (IOException e)
//            {
//                //
//            }
//        }
//    }
//
//
//
//
//    public static String deCompressString(String compressedString,String charsetName) throws IOException
//    {
//        InputStream unzip =null;
//        ByteArrayOutputStream bos =null;
//        try
//        {
//            byte[] zipped = new BASE64Decoder().decodeBuffer(compressedString);
//            unzip = new InflaterInputStream(new ByteArrayInputStream(zipped), new Inflater(true));
//            bos = new ByteArrayOutputStream();
//            byte[] bt= new byte[1024*1024];
//            int ln;
//            while ((ln=unzip.read(bt))>0)
//            {
//                bos.write(bt,0,ln);
//            }
//            bos.flush();
//            return bos.toString(charsetName);
//        }
//        finally
//        {
//            try {
//                if (bos!=null)
//                    bos.close();
//                if (unzip!=null)
//                    unzip.close();
//            } catch (IOException e)
//            {
//                //
//            }
//
//        }
//    }



//    private static class PRM
//    {
//        public String parName;
//        public String parValue;
//
//        private PRM(String parName, String parValue) {
//            this.parName = parName;
//            this.parValue = parValue;
//        }
//        public String toString()
//        {
//            return "parName = " + parName +"\n parValue = " + parValue;
//        }
//
//    }

    public static final String CMD_EXIT = "EXIT";
    public static final String CMD_INIT_SESSIONS = "CMD_INIT_SESSIONS";
    public static final String CMD_REQUEST = "CMD_REQUEST";
    public static final String CMD_GET_STATUS = "CMD_GET_STATUS";
    public static final String CMD_SKIP = "CMD_SKIP";
    public static final String CMD_STOP_SERVER = "CMD_STOP_SERVER";

    public static final int WAIT_FOR_GET_CMD = 2 * 1000;
    public static final int DEF_IN_PORT_N = 3333;
    public static int in_portNumber = DEF_IN_PORT_N;
    public static final int DEF_OUT_PORT_N = 3433;
    public static int out_portNumber = DEF_OUT_PORT_N;
    public static final long bufSize = 10 * 1024 * 1024;
    public static final int WAIT_COUNTER = 60; //Минуту



    private static class SendWrapper
    {
        public long length;
        public String payload;
        public String toString()
        {
            return "length = " + length +"\n payload = " + payload +"\n";
        }
    }

    private static class SessionStructure
    {
        public final BridgeCmdCtrl2 bridge=new BridgeCmdCtrl2();
        public volatile ICommonGuiStub stub;
        public volatile JFrame tu4_parent;
    }


    private static final AtomicLong sessionCounter = new AtomicLong(0);
    private static final ConcurrentHashMap<Long,SessionStructure> serverSessions = new ConcurrentHashMap<Long,SessionStructure>();
    private static  final InitGuiFactory factory=new InitGuiFactory();



    public static ICommonGuiStub startGUI(long sessionId, CtrlCmd cmd) throws Exception
    {

        SessionStructure structure=serverSessions.get(sessionId);


        Map<String,String> params = new HashMap<String,String>();
        params.put(ICommonGuiStub.MODE_VNM, ICommonGuiStub.MODE_DOC_VIEW);

        String[] strParams=UtilsClass.toStringParams(cmd);
        for (String strParam : strParams)
        {

            if (strParam !=null && strParam.length()>0 && !strParam.contains("="))
            {
                  params.put(ICommonGuiStub.MODE_VNM, strParam.toUpperCase());
                  break;
            }
        }


 //       Component w = FocusManager.getCurrentManager().getFocusOwner();

        Rectangle bounds = new Rectangle(0,0,5,5);
        final JFrame tu4_parent = structure.tu4_parent=new JFrame();


        ICommonGuiStub stub = structure.stub = factory.createDocEditComponent(tu4_parent, params);

        Method meth = stub.getClass().getMethod("setBridge", IBridgeCmdCtrl.class);
        if (meth!=null)
            meth.invoke(stub,structure.bridge);

        tu4_parent.setBounds(bounds);
        tu4_parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        Runnable runnable = new Runnable()
//        {
//            public void run() {
//                for (SessionStructure sessionStructure : serverSessions.values())
//                        {
//                            sessionStructure.tu4_parent.setAutoRequestFocus(false);
//                        }
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
//        thread.join();

         tu4_parent.setAutoRequestFocus(false); //TODO Антиблокировочная строка, ппочему пока не понятно.

        if (!("printOnly".equals(cmd.getCmd())))
            tu4_parent.show();

        return  stub;
    }


    public static long getHWnd(Frame f)
    {

        try
        {
            Method method = f.getClass().getMethod("getPeer");
            if (method!=null)
                return f.getPeer() != null ? ((sun.awt.windows.WComponentPeer) f.getPeer()).getHWnd() : 0;
        }
        catch (NoSuchMethodException e)
        {
            System.out.println("Error e = " + e.getMessage()+" try to get HWND by direct access to class field");
        }

        try
        {
            Field fld = Component.class.getDeclaredField("peer");
            if (fld!=null)
            {
                fld.setAccessible(true);
                Object peer=fld.get(f);
                if (peer!=null)
                {

                    Method method = peer.getClass().getMethod("getHWnd");
                    if (method!=null)
                    {
                        Object lll=method.invoke(peer);
    //                    Field fldWnd = WComponentPeer.class.getDeclaredField("hwnd");
    //                    fldWnd.setAccessible(true);
    //                    Object lll=fldWnd.get(peer);
                        System.out.println("lll = " + lll);
                        if (lll instanceof Long)
                            return (Long)lll;
                        else
                            throw new UnsupportedOperationException("Error can't acces to HWND of form  "+f+" hwnd not Long :"+((lll!=null)?(lll.getClass()+" :"+lll):null));
                    }
                    else
                        throw new UnsupportedOperationException("Error can't acces to HWND of form no method  getHWnd "+f);
                }
                else
                {
                    System.out.println("Can't acces to HWND of form peer is null");
                    return 0;
                }
            }
            else
                throw new UnsupportedOperationException("Error can't acces to HWND of form fld is null "+f);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new UnsupportedOperationException("Error can't acces to HWND of form fld Error:"+e.getMessage());

        }
        //return 0;
    }

    public static String wkDir=null;


    public static PrintStream outputFile(String name) throws Exception
    {
       return new PrintStream(new FileOutputStream(name), true,StandardCharsets.UTF_8.toString());
   }

    public static void main(String[] args) throws Exception
    {

        System.out.println("ln = " + args.length);
        for (String arg : args) {
            if (arg.startsWith("-wkDir")) {
                wkDir = arg.substring("-wkDir".length());
                System.out.println("wkDir=" + wkDir);
            }
            else if (arg.startsWith("-socketOut"))
            {
                try
                {
                    in_portNumber= Integer.parseInt(arg.substring("-socketOut".length()));
                }
                catch (NumberFormatException e)
                {
                }
            }
            else if (arg.startsWith("-socketIn"))
            {
                try
                {
                    out_portNumber= Integer.parseInt(arg.substring("-socketIn".length()));
                }
                catch (NumberFormatException e)
                {
                }
            }
            else
                System.out.println("Unknown parameter = " + arg);
        }

        try {
            if (wkDir!=null && wkDir.length()>0)
            {
                File file = new File(wkDir);
                if (file.isDirectory())
                {
                    String sOutFileName="sOutServerLog.log";
                    String fileName = wkDir + "\\" + sOutFileName;
                    PrintStream logStream=outputFile(fileName);
                    System.out.println("outPut will redirect to file:" + fileName);
                    System.setOut(logStream);
                    System.setErr(logStream);

                }
                else
                {
                    System.out.println((file.isFile()?"workDir is file ":" does not exists ")+wkDir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Версия RUNTIME:" + System.getProperty("java.runtime.version"));
        System.out.println("Версия JAVA:" + System.getProperty("java.version"));
        System.out.println("Версия browser :" + System.getProperty("browser.version"));
        System.out.println("Версия os :" + System.getProperty("os.version"));
        System.out.println("Версия VM :" + System.getProperty("java.vm.version"));
        System.out.println("HTTP Agent :" + System.getProperty("http.agent"));
        System.out.println("Макс. доступная память:" + Runtime.getRuntime().maxMemory() / 1024L + " Kb");



        Runnable inThread = new Runnable()
        {
            @Override
            public void run()
            {
                startInCmd();
            }
        };

        Runnable outThread = new Runnable()
        {
            @Override
            public void run()
            {
                startOutCmd();
            }
        };
        new Thread(inThread).start();
        new Thread(outThread).start();

        boolean beforeStopFlag=false;
        int waitCounter=0;
        for (;;)
        {
            try
            {
                boolean _beforeStopFlag=beforeStopFlag;
                beforeStopFlag=(inCommand.get()==0 && outCommand.get()==0);
                if (!_beforeStopFlag || !beforeStopFlag)
                        waitCounter=WAIT_COUNTER;

                if (beforeStopFlag)
                {
                    if (waitCounter<=0)
                    {
                        System.out.println("Stop server");
                        System.exit(0);
                    }
                    waitCounter--;
                }

                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                //
            }
        }

        //test1Channel();
        //startGUI();
//        startOutCmd();
    }


    public static int executeCmd(CtrlCmd cmd, Object[] rv, long sessionId)
    {
        try {

            SessionStructure structure=serverSessions.get(sessionId);
            if (structure==null)
                return -1;
            final ICommonGuiStub stub;
            if (structure.stub==null)
                 stub=startGUI(sessionId, cmd);
            else
                stub=structure.stub;

            String methodName=cmd.getCmd();
            String[] parameters = UtilsClass.toStringParams(cmd);

            final Method met;
            if (parameters==null)
            {

                if (stub instanceof IGridStub)
                    met = getMethodByClass(IGridStub.class,methodName);
                else
                    met = stub.getClass().getMethod(methodName);

                if (cmd.isSeparateThread())
                {

                    Runnable runnable = new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                met.invoke(stub);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    new Thread(runnable).start();
                    rv[0]=null;
                }
                else
                    rv[0]=met.invoke(stub);
            }
            else
            {
                Method[] methods;
                methods = stub.getClass().getDeclaredMethods();

                br2:
                {
                    for (final Method inMet : methods)
                    {
                        try
                        {

                            if (inMet.getName().equals(methodName))
                            { //пробуем совместить параметры и вызвать метод.
                                Class[] types = inMet.getParameterTypes();
                                if (types.length!=parameters.length)
                                    continue;
                                final Object[] oPars =new Object[types.length];
                                br1:
                                {
                                    for (int i = 0, typesLength = types.length; i < typesLength; i++)
                                    {
                                        Class type = types[i];
                                        if (type.equals(String.class))
                                        {
                                            oPars[i]=parameters[i];
                                        }
                                        else if (type.equals(byte.class) || type.equals(Byte.class))
                                        {
                                            oPars[i]=Byte.valueOf(parameters[i]);
                                        }
                                        else if (type.equals(short.class) || type.equals(Short.class))
                                        {
                                            oPars[i]=Short.valueOf(parameters[i]);
                                        }
                                        else if (type.equals(int.class) || type.equals(Integer.class))
                                        {
                                            oPars[i]=Integer.valueOf(parameters[i]);
                                        }
                                        else if (type.equals(long.class) || type.equals(Long.class))
                                        {
                                            oPars[i]=Long.valueOf(parameters[i]);
                                        }
                                        else if (type.equals(float.class) || type.equals(Float.class))
                                        {
                                            oPars[i]=Float.valueOf(parameters[i]);
                                        }
                                        else if (type.equals(double.class) || type.equals(Double.class) || type.equals(BigDecimal.class))
                                        {
                                            Double aDouble = Double.valueOf(parameters[i]);
                                            oPars[i]= aDouble;
                                            if (type.equals(BigDecimal.class))
                                                oPars[i]=BigDecimal.valueOf(aDouble);
                                        }
                                        else if (type.equals(boolean.class) || type.equals(Boolean.class))
                                        {
                                            oPars[i]=Boolean.valueOf(parameters[i]);
                                        }
                                        else
                                            break br1;
                                    }


                                    if (cmd.isSeparateThread())
                                    {
                                        Runnable runnable = new Runnable()
                                        {
                                            public void run()
                                            {
                                                try
                                                {
                                                    inMet.invoke(stub,oPars);
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        new Thread(runnable).start();
                                        rv[0]=null;
                                    }
                                    else
                                        rv[0]=inMet.invoke(stub,oPars);
                                    break br2;
                                }
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            //
                        }
                    }
                    throw new Exception("Method not found");
                }
            }

            if ( "showDocument".equals(cmd.getCmd()) && structure.tu4_parent!=null )
                    rv[0]=String.valueOf(getHWnd(structure.tu4_parent));
            return 0;
        }
        catch (Exception e)
        {
            //
  //          System.out.println("Exception on cmd:" + cmd.getAsString()+"  msg:"+e.getMessage());
            e.printStackTrace();

        }
        return -1;

    }

    protected static Method[] getDeclaredMethodsByClass(Class cl)
    {
        Method[] methods = cl.getDeclaredMethods();
        List<Method> ml = new LinkedList<>(Arrays.asList(methods));
        Class[] iCl = cl.getInterfaces();
        if (iCl!=null)
            for (Class anICl : iCl)
                ml.addAll(Arrays.asList(getDeclaredMethodsByClass(anICl)));

        Class sCl = cl.getSuperclass();
        if (sCl!=null)
            ml.addAll(Arrays.asList(getDeclaredMethodsByClass(sCl)));
        return ml.toArray(new Method[ml.size()]);
    }

    protected static Method getMethodByClass(Class cl,String methodName) throws NoSuchMethodException
    {
        Method met;
        try {
            met = cl.getMethod(methodName);
            if (met!=null)
                return met;
        }
        catch (Exception e)
        {

        }


        Class[] iCl = cl.getInterfaces();
        if (iCl!=null)
            for (Class anICl : iCl)
            {
                met = getMethodByClass(anICl, methodName);
                if (met!=null)
                  return met;
            }
        Class sCl = cl.getSuperclass();
        if (sCl!=null)
            return getMethodByClass(sCl, methodName);

        throw new NoSuchMethodException(methodName);
    }



//    private static void test1Channel() throws Exception
//    {
//        for (int i=0;;)
//        {
//            CtrlCmd inCmd = bridge.getInCtrlCommand(WAIT_FOR_GET_CMD);
//            //сдесь при получении команды
//            //1. Создать фрайм
//            //2. Разобрать параметры и вызвать showDocument
//            //3. Передать мост вновь создаваемому объекту
//
//            if (inCmd!=null)
//            {
////                String idCmd = inCmd.getIdCmd();
////                CtrlCmd outCmd = new CtrlCmd(inCmd.getCmd(), inCmd.getParams() + "_" + i);
////                outCmd.setIdCmd(idCmd);
////                bridge.putCtrlEvent(outCmd);
//                if (CMD_EXIT.equals(inCmd.getCmd()))
//                    break;
////                executeCmd(inCmd);
//                i++;
//            }
//        }
//    }




    private static void startInCmd()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(in_portNumber);
            for (;;)
            {
                final Socket clientSocket = serverSocket.accept();
                Runnable invoker =new Runnable()
                {

                    @Override
                    public void run()
                    {
                        char[] inBuff=new char[(int)bufSize];
                        PrintWriter out = null;
                        BufferedReader in = null;
                        long sessionId=-1;
                        try
                        {

                            inCommand.incrementAndGet();
                            in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.UTF_8));
                            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
                            //out = new PrintWriter(clientSocket.getOutputStream(), true);
                            for (;;)
                            {
                                SendWrapper myCmd = getCommandFromChannel(bufSize, inBuff, in);
                                if (myCmd==null) break;

                                CtrlCmd inCmd = new CtrlCmd();
                                inCmd.initByStringParam(myCmd.payload);


                                Object[] rv = new Object[1];
                                CtrlCmd outCmd = new CtrlCmd(CMD_SKIP);
                                outCmd.setIdCmd(inCmd.getIdCmd());

                                if (CMD_STOP_SERVER.equals(inCmd.getCmd()))
                                {
                                    System.out.println("SERVER STOP FROM IN CHANNEL: Active clients: "+inCommand.get());
                                    System.out.flush();
                                    System.exit(0);
                                }

                                if (inCmd.getCmd().equals(CMD_INIT_SESSIONS))
                                {   sessionId = sessionCounter.incrementAndGet();

                                    SessionStructure sessionStructure = new SessionStructure();

//                                    {
//                                        sessionStructure.bridge.setSessionId(sessionId);
//                                        sessionStructure.bridge.setObject(serverSessions);
//                                    }

                                    serverSessions.put(sessionId, sessionStructure);
                                    outCmd = new CtrlCmd(inCmd.getCmd(), String.valueOf(sessionId));
                                    outCmd.setIdCmd(inCmd.getIdCmd());
                                }
                                else if (inCmd.getCmd().equals(CMD_GET_STATUS))
                                {
                                    outCmd = new CtrlCmd(inCmd.getCmd(),String.valueOf(sessionId));
                                    outCmd.setIdCmd(inCmd.getIdCmd());
                                }
                                else if (CmdConst.CMD_CALL_MENU.equalsIgnoreCase(inCmd.getCmd()))
                                {
                                    SessionStructure structure=serverSessions.get(sessionId);
                                    if (structure==null)
                                        break;
                                    structure.stub.putInCmd(inCmd.getAsString());
                                }
                                else if (executeCmd(inCmd, rv,sessionId)!=0)
                                {
                                    SessionStructure structure=serverSessions.get(sessionId);
                                    if (structure==null)
                                        break;
                                    structure.bridge.putInCmd(myCmd.payload);
                                }
                                else
                                {
                                    if (rv[0]!=null)
                                    {
                                        outCmd = new CtrlCmd(inCmd.getCmd(),rv[0].toString());
                                        outCmd.setIdCmd(inCmd.getIdCmd());
                                    }
                                }

//                                String forSend=outCmd.buildString(new StringBuffer()).toString();
//                                int lnOutCmd=forSend.getBytes(StandardCharsets.UTF_8).length;
//                                String payload = lnOutCmd + "\n" + forSend;
//                                out.write(payload);
//                                out.flush();

                                if ("buildTree".startsWith(outCmd.getCmd()) || "getOneLevelOfTree".startsWith(outCmd.getCmd()))
                                {
                                    String params = outCmd.getParams();
                                    outCmd.setParams(Xml2Struct.optimizeArc(params,1.33));
//                                    String arc = Xml2Struct.arc(params);
//                                    if (((int)(1.33*arc.length()))<=params.length())
//                                        outCmd.setParams(arc);

//                                    if (params.length() > 1024*1024)
//                                    {
////                                        Map<String, List<String>> pid2List = Xml2Struct.buildMapBuffer(params);
////                                        StringBuilder sbParams=Xml2Struct.getTestXML(pid2List);
//                                        //System.out.println("sbParams.length() = " + sbParams.length());
//                                        String arc = Xml2Struct.arc(sbParams);
//                                        outCmd.setParams(arc);
//                                    }

//                                    String forSend=outCmd.buildString(new StringBuffer()).toString();
//                                    int lnOutCmd=forSend.getBytes(StandardCharsets.UTF_8).length;
//                                    String payload = lnOutCmd + "\n" + forSend;
//                                    PrintWriter fos=new PrintWriter(new OutputStreamWriter(new FileOutputStream("C:\\PapaWK\\XMLProbem\\t.xml"),StandardCharsets.UTF_8));
//                                    fos.write(payload);
//                                    fos.flush();
//                                    fos.close();
                                }

                                String payload=send2Client(out,outCmd);

                                //System.out.println("IN CHANNEL: Send answer "+ payload+" to Client on "+myCmd.payload);
                                System.out.println("IN CHANNEL: Send answer "+ ((payload!=null)?payload.length():0)+" to Client on "+((myCmd.payload!=null)?myCmd.payload.length():0));

                                if (CMD_EXIT.equalsIgnoreCase(inCmd.getCmd()))
                                {
                                    System.out.println("IN CHANNEL: GOT Exit command, break socket");
                                    break;
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {

                            try {
                                if (out!=null)
                                    out.close();
                            } catch (Exception e) {
                                //
                            }
                            try {
                                if (in!=null)
                                    in.close();
                            } catch (Exception e) {
                                //
                            }
                            try {
                                if (clientSocket!=null)
                                    clientSocket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            releaseAll(sessionId);
                            inCommand.decrementAndGet();
                        }
                    }
                };
                new Thread(invoker).start();
            }
        }
        catch (Exception ex)
        {
        }
    }

    private static void releaseAll(long sessionId) {
        SessionStructure structure=serverSessions.remove(sessionId);
        if (structure!=null)
        {
            structure.bridge.stopBridge();
            structure.tu4_parent.setVisible(false);
            structure.tu4_parent.dispose();
        }
    }

    private static void startOutCmd()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(out_portNumber);
            for (;;)
            {
                final Socket clientSocket = serverSocket.accept();
                Runnable invoker =new Runnable()
                {

                    @Override
                    public void run()
                    {
                        char[] inBuff=new char[(int) bufSize];
                        PrintWriter out = null;
                        BufferedReader in = null;
                        long sessionId=-1;
                        try
                        {
                            outCommand.incrementAndGet();
                            in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.UTF_8));
                            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
                            for (;;)
                            {

                                SendWrapper myCmd = getCommandFromChannel(bufSize, inBuff, in);
                                if (myCmd==null) break;

                                CtrlCmd inCmd = new CtrlCmd();
                                inCmd.initByStringParam(myCmd.payload);

                                String outPayload;
                                if (inCmd.getCmd().equals(CMD_INIT_SESSIONS))
                                {
                                    try {
                                        sessionId=Long.parseLong(inCmd.getParams());
                                    } catch (NumberFormatException e)
                                    {
                                        System.out.println("Can't get session id from parameters:"+inCmd.getParams());
                                        e.printStackTrace();
                                    }
                                    CtrlCmd outCmd = new CtrlCmd(inCmd.getCmd(),String.valueOf(sessionId));
                                    outCmd.setIdCmd(inCmd.getIdCmd());
                                    outPayload = send2Client(out, outCmd);
                                }
                                else
                                if (inCmd.getCmd().equals(CMD_GET_STATUS))
                                {
                                    CtrlCmd outCmd = new CtrlCmd(inCmd.getCmd(),String.valueOf(sessionId));
                                    outCmd.setIdCmd(inCmd.getIdCmd());
                                    outPayload = send2Client(out, outCmd);
                                }
                                else
                                {
                                    if (CMD_STOP_SERVER.equals(inCmd.getCmd()))
                                    {
                                        System.out.println("SERVER STOP FROM OUT CHANNEL: Active clients: "+outCommand.get());
                                        System.out.flush();
                                        System.exit(0);
                                    }
                                    if (CMD_EXIT.equals(inCmd.getCmd()))
                                    {
                                        System.out.println("OUT CHANNEL: GOT Exit command, break output channel");
                                        break;
                                    }
                                    //По сути сюда приходит запрос
                                    SessionStructure structure=serverSessions.get(sessionId);
                                    if (structure==null)
                                        break;
                                    String forGuiSend = structure.bridge.getOutCtrlCommands(WAIT_FOR_GET_CMD);
                                    if (forGuiSend!=null)
                                    {
                                        int lnOutCmd=forGuiSend.getBytes(StandardCharsets.UTF_8).length;
                                        outPayload = lnOutCmd + "\n" + forGuiSend;
                                        out.write(outPayload);
                                        out.flush();
                                    }
                                    else
                                    {
                                        CtrlCmd outCmd = new CtrlCmd(CMD_SKIP);
                                        outCmd.setIdCmd(inCmd.getIdCmd());
                                        outPayload = send2Client(out, outCmd);
                                    }
                                }

                                if (!outPayload.contains("CMD_SKIP"))
                                    System.out.println("OUT CHANNEL: Send "+ outPayload+" to Client");

                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {

                            try {
                                if (out!=null)
                                    out.close();
                            } catch (Exception e) {
                                //
                            }
                            try {
                                if (in!=null)
                                    in.close();
                            } catch (Exception e) {
                                //
                            }
                            try {
                                if (clientSocket!=null)
                                    clientSocket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            releaseAll(sessionId);
                            outCommand.decrementAndGet();
                        }
                    }
                };
                new Thread(invoker).start();
            }
        }
        catch (Exception ex)
        {
        }
    }

    private static String send2Client(PrintWriter out, CtrlCmd outCmd)
    {
        String forSend=outCmd.buildString(new StringBuffer()).toString();
        int lnOutCmd=forSend.getBytes(StandardCharsets.UTF_8).length;
        String payload = lnOutCmd + "\n" + forSend;
        out.write(payload);
        out.flush();
        return payload;
    }

    private static SendWrapper getCommandFromChannel(long bufsize, char[] inbuff, BufferedReader in) throws IOException
    {
        StringBuilder cmd=new StringBuilder();
        String  sLn=in.readLine();
        if (sLn==null)
        {
            System.out.println("Channel was close, get "+ sLn+" length");
            return null;//End of Channel
        }
        long ln;
        try {
            ln = Long.parseLong(sLn);
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
            System.out.println("Error in Channel : not length in channel"+ sLn);
            return null;
        }
        int cnt=0;
        long rest=ln;
        while( rest>0 && (cnt=in.read(inbuff,0,(int)Math.min(bufsize,rest)))>0 )
        {
            cmd.append(inbuff,0,cnt);

            rest-=new String(inbuff,0,cnt).getBytes(StandardCharsets.UTF_8).length;
            if (rest>0)
                System.out.println("rest = " + rest);
        }

        if (cnt<0)
        {
            System.out.println("Client close connection");
            return null;
        }
//        cmd.append(inbuff,0,cnt);

        //Обработка комманды и отдача результата
        SendWrapper myCmd = new SendWrapper();
        myCmd.length=ln;
        myCmd.payload =cmd.toString();
//        System.out.println("Got Command: " + myCmd.toString());
        return myCmd;
    }

}
