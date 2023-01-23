package tst.ev;


/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 19.02.18
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
public interface IBridgeCmdCtrl {
    String getOutCtrlCommands();

    String putInCmd(String cmdString);

    CtrlCmd getInCtrlCommand(long timeOut) throws InterruptedException;

    int getOutCtrlCommandSize() throws InterruptedException;

    int getInCtrlCommandSize() throws InterruptedException;

    void postPoneFireEvent(CtrlCmd ctrlCmd);

    void fireEvent();

    void fireEvent(MyEvent event);

    void putCtrlEvent(CtrlCmd e);

    void shuttingDownBridge();

    void stopBridge();

    CtrlCmd syncCtrlEvent(CtrlCmd e) throws BridgeException, RequestException;
}
