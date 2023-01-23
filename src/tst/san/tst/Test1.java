package san.tst;

import san.misc.BASE64Decoder_OLD;
import san.misc.BASE64Encoder_NEW;
import san.misc.BASE64Encoder_OLD;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 21.05.2018
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class Test1
{
    public static void main(String[] args) throws IOException {

        BASE64Encoder_NEW newEncoder = new BASE64Encoder_NEW();
        BASE64Encoder_OLD oldEncoder=new BASE64Encoder_OLD();

        byte[] bytes = "Вот эта строка \n || \"Вот эта строка 2\n\n\n\n".getBytes();
        {
            String newS=newEncoder.encodeBuffer(bytes);
            String oldS=oldEncoder.encodeBuffer(bytes);

            if (!newS.equals(oldS))
            {
                System.out.println("Error Implementation of encodeBuffer 1");
            }
        }


        ByteArrayOutputStream bosold = new ByteArrayOutputStream();
        ByteArrayOutputStream bosnew = new ByteArrayOutputStream();

        oldEncoder.encodeBuffer(bytes, bosold);
        newEncoder.encodeBuffer(bytes,bosnew);

        byte[] oldB=bosold.toByteArray();
        byte[] newB=bosnew.toByteArray();

        String sOld= new String(oldB);
        String sNew= new String(newB);

        //String res=new String(new BASE64Decoder_OLD().decodeBuffer(sNew));
        if (!sOld.equals(sNew))
            System.out.println("Error Implementation of encodeBuffer 2.1");
        else
        {
            if (newB.length != oldB.length)
                System.out.println("Error Implementation of encodeBuffer 2.2 " + (newB.length - oldB.length));
            else
                for (int i = 0; i < newB.length; i++) {
                    if (newB[i] != oldB[i]) {
                        System.out.println("Error Implementation of encodeBuffer 2.3 " + i);
                        break;
                    }
                }
        }


    }
}
