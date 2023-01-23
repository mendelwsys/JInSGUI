package tst;

import san.misc.BASE64Decoder;
import tst.ev.CtrlCmd;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 21.02.17
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class UtilsClass  extends UtilsClassA
{

    public static final String WITH_OPTIONS = "@@OPTS";
    public static final String PARAMS_LN="@@LN=";
    public static final String PK_ZIP="@@PK_ZIP";
    public static final String URL_ENCODE="@@URL_ENCODE";
    public static final String THREAD="@@THR";


    private static StringBuilder getCntOfParams(String params) {
        StringBuilder s_params= new StringBuilder();
        int i=0;
        while (Character.isDigit(params.charAt(i)))
        {
            s_params.append(params.charAt(i));
            i++;
        }
        return s_params;
    }


    public static String[] toStringParams2(CtrlCmd cmd) throws Exception
    {
        String params = cmd.getParams();
        if (params != null && params.length() > 0)
        {
            if (params.startsWith(WITH_OPTIONS))
            { //Новый формат
                return getStringsWithOptions(cmd, params);
            }
        }
        return null;
    }
    public static String[] toStringParams(CtrlCmd cmd) throws Exception
    {
        String params = cmd.getParams();
        if (params != null && params.length() > 0)
        {
            if (params.startsWith(WITH_OPTIONS))
            { //Новый формат
                return getStringsWithOptions(cmd, params);
            }
            else
            {
                String[] split = params.split(CmdConst.PAR_DL, -1);
                for (int i = 0; i < split.length; i++)
                    if (THREAD.equals(split[i]))
                    {
                        cmd.setSeparateThread(true);
                        java.util.List<String> rv= new LinkedList<>(Arrays.asList(split));
                        rv.remove(i);
                        if (rv.size()>0)
                            return rv.toArray(new String[rv.size()]);
                        return null;
                    }
                return split;
            }
        }
        else
            return null;
    }

    public static String[] getStringsWithOptions(CtrlCmd cmd, String params) throws IOException
    {
        List<String> rv= new LinkedList<>();
        params=params.substring(WITH_OPTIONS.length());

        int parLn=-1;
        boolean isPk=false;
        boolean isUrlEncode=false;

        while( params.length()>0)
        {

            if (params.startsWith(CmdConst.PAR_DL) && parLn>=0)
            {
                String param = params.substring(1,parLn+1);
                if (isUrlEncode)
                    param= URLDecoder.decode(param,  "UTF-8");
                if (isPk)
                {
                    byte[] bytes = (new BASE64Decoder()).decodeBuffer(param);
                    GZIPInputStream unzip = new GZIPInputStream(new ByteArrayInputStream(bytes));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024 * 1024];
                    int cnt=0;
                    try {
                        while ((cnt = unzip.read(buff))>0)
                            bos.write(buff,0,cnt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bos.close();
                    param=new String(bos.toByteArray(),"UTF-8");
                }
                rv.add(param);
                params=params.substring(1+parLn);

                parLn=-1;
                isPk=false;
                isUrlEncode=false;
            }
            else  if (params.startsWith(PK_ZIP))
            {
                params = params.substring(PK_ZIP.length());
                isPk=true;
            }
            else  if (params.startsWith(URL_ENCODE))
            {
                params = params.substring(URL_ENCODE.length());
                isUrlEncode=true;
            }
            else if (params.startsWith(PARAMS_LN))
            {
                params=params.substring(PARAMS_LN.length());
                StringBuilder s_cnt = getCntOfParams(params);
                params=params.substring(s_cnt.length());
                parLn=Integer.parseInt(s_cnt.toString());
            }
            else if (params.startsWith(THREAD))
            {
                params = params.substring(THREAD.length());
                cmd.setSeparateThread(true);
            }
            else
                break;
        }
        if (rv.size()>0)
            return rv.toArray(new String[rv.size()]);
        return null;
    }

}
