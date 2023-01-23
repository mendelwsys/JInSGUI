package tst;

import java.awt.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 28.02.17
 * Time: 18:19
 * Фабрика для класса рекдактирования документа
 */
public interface IGuiStubFactory extends ILoaderCtrl
{
     ICommonGuiStub createDocEditComponent(Component comp, Map<String,String> params)  throws Exception;
}
