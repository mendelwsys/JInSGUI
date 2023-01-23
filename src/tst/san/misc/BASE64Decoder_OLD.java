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
public class BASE64Decoder_OLD
{
    public byte[] decodeBuffer(String var1) throws IOException
    {
        System.out.println("use san.misc.BASE64Decoder_OLD to decode 1.2 = ");
        return new sun.misc.BASE64Decoder().decodeBuffer(var1);
    }

//    public void decodeBuffer(InputStream var1, OutputStream var2) throws IOException {
//        System.out.println("use san.misc.BASE64Decoder to decode 1 = ");
//        base64Decoder.decodeBuffer(var1,var2);
//    }

    public byte[] decodeBuffer(InputStream var1) throws IOException
    {
        System.out.println("use san.misc.BASE64Decoder_OLD to decode 2.2 = ");
        return new sun.misc.BASE64Decoder().decodeBuffer(var1);
    }
}
