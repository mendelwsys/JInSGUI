package tst;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 17.05.17
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public class URLCtrlImpl implements ILoaderCtrl
{
    private List<URL> urls=new LinkedList<URL>();
    public int addUrlString(String urlString)
    {
        try {

            URL url = new URL(urlString);
            urls.add(url);
            return ok;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return err;
    }

    public int rmUrlString(String urlString)
    {
        try {
            URL findUrl = new URL(urlString);
            for (Iterator<URL> iterator = urls.iterator(); iterator.hasNext(); )
            {
                URL url = iterator.next();
                if (url.equals(findUrl))
                {
                    iterator.remove();
                    return ok;
                }
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return err;
    }

    public int addXMLUrls(String xmlUrls)
    {
        return ok;
    }

    public String getXMLUrls()
    {
        StringBuilder sb = new StringBuilder();
        for (URL url : urls)
           sb.append("<url ref='"+url.toString()+"'/>\n");
        return "<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n" + "<URL>\n" + sb.toString() + "</URL>";
    }

    public List<URL> getListUrls()
    {
        return urls;
    }
}
