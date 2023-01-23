package san.misc;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 18.05.2018
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class BASE64Decoder
{
    volatile static int style=0; //0 - не проверено 1-старый стиль 2-новый стиль
    volatile static private Class cl;

    public byte[] decodeBuffer(String var1) throws IOException
    {
        if (style==0)
            setStyle();
        if (style==1)
        {
              return new BASE64Decoder_OLD().decodeBuffer(var1);
        }
        else
        {
            return new BASE64Decoder_NEW().decodeBuffer(var1);
        }
    }

    private void setStyle() {
        try
        {
            cl=getClass().getClassLoader().loadClass("sun.misc.BASE64Decoder");
            style=1;
        }
        catch (Exception e)
        {
            System.out.println("using new style of coder decoder = " + e.getMessage());
            try {
                cl=getClass().getClassLoader().loadClass("java.util.Base64");
                style=2;
            }
            catch (ClassNotFoundException e1)
            {
                e1.printStackTrace();
            }
        }
    }

//    public void decodeBuffer(InputStream var1, OutputStream var2) throws IOException {
//        System.out.println("use san.misc.BASE64Decoder to decode 1 = ");
//        base64Decoder.decodeBuffer(var1,var2);
//    }

    public byte[] decodeBuffer(InputStream var1) throws IOException
    {
        if (style==0)
            setStyle();

        if (style==1)
        {
              return new BASE64Decoder_OLD().decodeBuffer(var1);
        }
        else
        {
            return new BASE64Decoder_NEW().decodeBuffer(var1);
        }

//        System.out.println("use san.misc.BASE64Decoder to decode 2.2 = ");
//        Base64.Decoder decoder = Base64.getMimeDecoder();
//
//        byte[] bt = new byte[10 * 1024];
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        int rv;
//        InputStream wrap = decoder.wrap(var1);
//        try {
//            while ((rv = wrap.read(bt))>0)
//                bos.write(bt,0,rv);
//        }
//        catch (IOException e)
//        {
//            System.out.println("e00 = " + e.getMessage());
//            e.printStackTrace();
//        }
//        bos.close();
//
//        return bos.toByteArray();
        //return base64Decoder.decodeBuffer(var1);
    }
}
