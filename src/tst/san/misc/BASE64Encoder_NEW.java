package san.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 17.05.2018
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class BASE64Encoder_NEW
{
    public String encode(byte[] src)
    {
        System.out.println("use san.misc.BASE64Encoder_NEW to encodeBuffer 1.1 = ");
        return new String(Base64.getMimeEncoder().encode(src));
    }
    //sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();

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

//    public void encodeBuffer(InputStream var1, OutputStream var2) throws IOException
//    {
//        System.out.println("use san.misc.BASE64Encoder to encodeBuffer 2 = ");
//        Base64.Encoder encoder = Base64.getEncoder();
//
//        ByteArrayOutputStream bos=new ByteArrayOutputStream();
//        byte[] bt= new byte[10*1024];
//        int ln=0;
//        while((ln=var1.read(bt))>0)
//            bos.write(bt,0,ln);
//        bos.close();
//        encoder.wrap(var2).write(bos.toByteArray());
//    }

    public void encodeBuffer(byte[] var1, OutputStream var2) throws IOException
    {
        System.out.println("use san.misc.BASE64Encoder_NEW to encodeBuffer 2.1 = ");
        Base64.Encoder encoder = Base64.getMimeEncoder();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream wrap = encoder.wrap(bos);
        wrap.write(var1);
        wrap.close();
        var2.write(bos.toByteArray());
        var2.write(CRLF);
    }

    private static final byte[] CRLF = new byte[] {'\r', '\n'};
    public String encodeBuffer(byte[] var1)
    {
        System.out.println("use san.misc.BASE64Encoder_NEW to encodeBuffer 3.1 = ");
        Base64.Encoder encoder = Base64.getMimeEncoder();
        String s = encoder.encodeToString(var1);
        s+=new String(CRLF);
        return s;

    }


}

