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
public class BASE64Encoder_OLD
{
    public String encode(byte[] src)
    {
        System.out.println("use san.misc.BASE64Encoder_OLD to encodeBuffer 1.1 = ");
        return new sun.misc.BASE64Encoder().encode(src);
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
//        new sun.misc.BASE64Encoder().encodeBuffer(var1,var2);
//
//    }

    public void encodeBuffer(byte[] var1, OutputStream var2) throws IOException
    {
        System.out.println("use san.misc.BASE64Encoder_OLD to encodeBuffer 2.1 = ");
        new sun.misc.BASE64Encoder().encodeBuffer(var1,var2);
    }

    public String encodeBuffer(byte[] var1) {
        System.out.println("use san.misc.BASE64Encoder_OLD to encodeBuffer 3.1 = ");
        return new sun.misc.BASE64Encoder().encodeBuffer(var1);
    }


}

