package tst;

import com.mwlib.utils.db.Pair;
import san.misc.BASE64Decoder;
import san.misc.BASE64Encoder;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 27.01.2020
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public class Xml2Struct
{


    public static void main(String[] args) throws Exception
    {

        StringBuilder bd = new StringBuilder();
        String str;


        long l1 = System.currentTimeMillis();

        BufferedReader brd= new BufferedReader(new InputStreamReader(new FileInputStream("C:\\PapaWK\\XMLProbem\\test_big.xml"),"UTF8"));

        brd.readLine();
        while ((str = brd.readLine())!=null)
            bd.append("\n").append(str);


        FileOutputStream fos=new FileOutputStream("C:\\PapaWK\\XMLProbem\\o2big.b64");
        fos.write(arc(bd).getBytes());
        fos.close();



        Map<String,List<String>> pid2List = buildMapBuffer(bd);


        l1 = System.currentTimeMillis()-l1;
        System.out.println("time for load tree " + l1);



//        Set<String> excludeKey =  new HashSet<>();
//        excludeKey.add("603334");
//        excludeKey.add("3337");
//        excludeKey.add("403335");
//        excludeKey.add("203336");
//        excludeKey.add("803333");
//

        l1 = System.currentTimeMillis();
        StringBuilder level = getTestXML(pid2List);
        l1 = System.currentTimeMillis()-l1;

        fos=new FileOutputStream("C:\\PapaWK\\XMLProbem\\o2level.xml");
        fos.write(level.toString().getBytes("WINDOWS-1251"));
        fos.close();

        String param2 = arc(level);

        fos=new FileOutputStream("C:\\PapaWK\\XMLProbem\\o2.b64");
        fos.write(param2.getBytes());
        fos.close();




        System.out.println("time for get XML tree " + l1);
    }

    public static String arc(StringBuilder level) throws IOException {
        String toServer = level.toString();
        return arc(toServer);
    }

//    public static String getDataByJSON(String jsonFilter, IDocumentFrame docFrame)   //TODO !!!Не отлажено!!!
//    {
//        String jsonRes="";
//        try
//        {
//            Map<String,Set<Integer>> nm2Filer  =  new HashMap<>();
//            if (jsonFilter!=null && jsonFilter.length()>0)
//            {
//                if (!jsonFilter.contains("[") || !jsonFilter.contains("]"))
//                    jsonFilter = unArc(jsonFilter);
//                //jsonFilter = URLDecoder.decode(jsonFilter, "UTF-8");
//                BackEndDocBean02.VarFieldStruct[] tab = TJ.str2dat(jsonFilter, BackEndDocBean02.VarFieldStruct[].class);
//                for (BackEndDocBean02.VarFieldStruct varFieldStruct : tab)
//                {
//                    Set<Integer> ll=nm2Filer.get(varFieldStruct.getVARNAME());
//                    if (ll==null)
//                        nm2Filer.put(varFieldStruct.getVARNAME(),ll=new HashSet<Integer>());
//                    ll.add(varFieldStruct.getCOPYNUM());
//                }
//            }
//
//            List<BackEndDocBean02.VarFieldStruct> rl =  new LinkedList<>();
//
//            ArrayList variables = docFrame.getVariables();
//            for (Object variable : variables)
//            {
//                Variable ud = (Variable) variable;
//                if (nm2Filer.size() == 0)
//                    rl.add(new BackEndDocBean02.VarFieldStruct(ud.getVarName(), ud.getValue(), ud.getCopyNum()));
//                else {
//                    Set<Integer> cpnum = nm2Filer.get(ud.getVarName());
//                    if (cpnum != null && cpnum.contains(ud.getCopyNum()))
//                        rl.add(new BackEndDocBean02.VarFieldStruct(ud.getVarName(), ud.getValue(), ud.getCopyNum()));
//                }
//            }
//            BackEndDocBean02.VarFieldStruct[] arrRl = rl.toArray(new BackEndDocBean02.VarFieldStruct[rl.size()]);
//            jsonRes=TJ.dat2str(arrRl);
//            jsonRes = optimizeArc(jsonRes,1.33);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return jsonRes;
//    }


    public static String optimizeArc(String params,double k) throws IOException
    {
        String arc = Xml2Struct.arc(params);
        if (((int)(k*arc.length()))<=params.length())
            return arc;
        return params;
    }

    public static String unArc(String base64) throws IOException
    {
        byte[] bytes = new BASE64Decoder().decodeBuffer(base64);
        GZIPInputStream unzip = new GZIPInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte buff[]=new byte[5*1024*1024];
        try
        {
            int cnt;
            while ((cnt=unzip.read(buff))>0)
                bos.write(buff,0,cnt);
        }
        catch (EOFException e)
        {  //
        }
        bos.flush();
        bos.close();
        return new String(bos.toByteArray(),"UTF-8");
    }


    public static String arc(String toServer) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream zip = new GZIPOutputStream(bos);
//        zip.write("<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n".getBytes("WINDOWS-1251"));
        zip.write(toServer.getBytes("UTF8"));
        zip.close();
        return (new BASE64Encoder()).encode(bos.toByteArray());
    }



    public static StringBuilder getTestXML(Map<String, List<String>> pid2List) {
        Pair<String, Integer> pr = getMaxKey(pid2List, null);
        String sPid = pr.getKey();
        return getLevelXMLList(pid2List, sPid, "<NODE pid='' sel='X'   cid='" + sPid + "' nType='9' nName='Структура документа' />");
    }


    public static Map<String,List<String>> buildMapBuffer(StringBuilder bd) throws IOException
    {
        String inString = bd.toString();

        return buildMapBuffer(inString);
    }

    public static Map<String, List<String>> buildMapBuffer(String inString) throws IOException {
        BufferedReader brd;
        Map<String,List<String>> pid2Count = new HashMap<>();
        String str;

        brd= new BufferedReader(new StringReader(inString));
        while ((str = brd.readLine())!=null)
        {
            String startStr = "pid='";
            int ix = str.indexOf(startStr);
            if (ix<0)
                continue;
            ix+= startStr.length();
            int ix2=str.indexOf("'",ix);
            if (ix2<0)
                continue;

            String key = str.substring(ix, ix2);
            if (key.length()<=0)
                continue;

            List<String> ll=pid2Count.get(key);
            if (ll==null)
                pid2Count.put(key,ll=new LinkedList<String>());
            ll.add(str);
        }
        brd.close();

        return  pid2Count;
    }

    public static Pair<String,Integer> getMaxKey(Map<String, List<String>> pid2List, Set<String> exclude)
    {
        Pair<String,Integer> rv=null;
        int maxKeyCnt=0;
        String maxKey="";
        for (String key : pid2List.keySet())
        {
            List cnt=pid2List.get(key);
            if (cnt.size()>maxKeyCnt && (exclude==null || !exclude.contains(key))) {
                maxKeyCnt = cnt.size();
                maxKey = key;
            }
        }
        //System.out.println("maxKey = " + maxKey+" "+maxKeyCnt);
        return  new Pair<>(maxKey,maxKeyCnt);
    }


    public static StringBuilder getLevelXMLList(Map<String, List<String>> pid2List,String pid,String addTestLine)
    {
        StringBuilder sb=new StringBuilder("<NODES>");
        sb.append("\n").append(addTestLine);
        List<String> ll = pid2List.get(pid);
//        int ix=0;
        for (String s : ll)
        {
//            if (ix%5==0)
                sb.append("\n").append(s);
//            ix++;
        }

        sb.append("\n</NODES>");
        return sb;
    }
}
