package san.misc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 17.05.2018
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class BASE64Encoder
{
    volatile static int style=0; //0 - не проверено 1-старый стиль 2-новый стиль
    volatile static private Class cl;

    private void setStyle() {
        try
        {
            cl=getClass().getClassLoader().loadClass("sun.misc.BASE64Encoder");
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

    public String encode(byte[] src)
    {
        if (style==0)
            setStyle();
        if (style==1)
        {
            return new BASE64Encoder_OLD().encode(src);
        }
        else
        {
            return new BASE64Encoder_NEW().encode(src);
        }

    }


//    public String encode(byte[] src)
//    {
//        System.out.println("use san.misc.BASE64Encoder to encode 0 = " );
//        return base64Encoder.encode(src);
//    }
//    public String encode(ByteBuffer var1)
//    {
//        System.out.println("use san.misc.BASE64Encoder to encode 1 = ");
//        return base64Encoder.encode(var1);
//
//    }

//    public void encodeBuffer(InputStream var1, OutputStream var2) throws IOException {
//        System.out.println("use san.misc.BASE64Encoder to encodeBuffer 2 = ");
//        base64Encoder.encodeBuffer(var1,var2);
//
//    }

    public void encodeBuffer(byte[] var1, OutputStream var2) throws IOException
    {
        if (style==0)
            setStyle();
        if (style==1)
        {
            new BASE64Encoder_OLD().encodeBuffer(var1, var2);
        }
        else
              new BASE64Encoder_NEW().encodeBuffer(var1,var2);
    }

    public String encodeBuffer(byte[] var1) {
        if (style==0)
            setStyle();
        if (style==1)
        {
            return new BASE64Encoder_OLD().encodeBuffer(var1);
        }
        else
            return  new BASE64Encoder_NEW().encodeBuffer(var1);

    }


}

