package tst.ev;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 18.11.16
 * Time: 16:42
 * Событие которое будет обрабатываться из GUI
 */
public class MyEvent extends EventObject {
  public MyEvent(Object source) {
    super(source);
  }
}