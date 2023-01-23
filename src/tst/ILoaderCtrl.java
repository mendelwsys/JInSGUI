package tst;

import java.net.URL;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 17.05.17
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public interface ILoaderCtrl
{
    int ok=0;
    int err=-1;

    int addUrlString(String urlString);
    int rmUrlString(String urlString);
    String getXMLUrls();
    List<URL> getListUrls();

}
