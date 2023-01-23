package san.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 18.05.2018
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class BASE64Decoder_NEW
{
    public byte[] decodeBuffer(String var1) throws IOException
    {

        System.out.println("use san.misc.BASE64Decoder_NEW to decode 1.2 = ");
        byte[] decode = Base64.getMimeDecoder().decode(var1.getBytes());
        return decode;
    }

//    public void decodeBuffer(InputStream var1, OutputStream var2) throws IOException {
//        System.out.println("use san.misc.BASE64Decoder to decode 1 = ");
//        base64Decoder.decodeBuffer(var1,var2);
//    }

    public byte[] decodeBuffer(InputStream var1) throws IOException
    {
        System.out.println("use san.misc.BASE64Decoder_NEW to decode 2.2 = ");
        Base64.Decoder decoder = Base64.getMimeDecoder();

        byte[] bt = new byte[10 * 1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int rv;
        InputStream wrap = decoder.wrap(var1);
        try {
            while ((rv = wrap.read(bt))>0)
                bos.write(bt,0,rv);
        }
        catch (IOException e)
        {
            System.out.println("e2.2 = " + e.getMessage());
            e.printStackTrace();
        }
        bos.close();

        return bos.toByteArray();
    }
}
