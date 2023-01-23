package tst;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 15.05.17
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
public class UtilsClassA
{

    public static String getFileAsString(String name) throws IOException
    {
            FileInputStream fis =null;
            ByteArrayOutputStream bos;
            try {
                fis = new FileInputStream(name);
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[5 * 1024];
                int r;
                while ((r=fis.read(b))>0)
                {
                    bos.write(b,0,r);
                }
                bos.close();
            }
            finally
            {
                if (fis!=null)
                    fis.close();
            }


            return new String(bos.toByteArray(),"Windows-1251");
    }


    public static void writeFileAsString(String fname,String content)
    {
        FileWriter fileWriter= null;
        try {
            fileWriter = new FileWriter(fname);
            fileWriter.write(content);
            fileWriter.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (fileWriter!=null)

                try {
                    fileWriter.close();
                } catch (IOException e)
                {
                    ;
                }

        }
    }




}
