package tst.ev;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 27.09.17
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class BridgeException extends Exception
{

    public BridgeException() {
    }

    public BridgeException(String message) {
        super(message);
    }

    public BridgeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BridgeException(Throwable cause) {
        super(cause);
    }
}
