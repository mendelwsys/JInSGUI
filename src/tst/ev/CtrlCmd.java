package tst.ev;

import tst.CmdConst;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 28.02.17
 * Time: 14:51
 *
 */
public class CtrlCmd
{
    public boolean isSeparateThread() {
        return separateThread;
    }

    public void setSeparateThread(boolean separateThread) {
        this.separateThread = separateThread;
    }

    private boolean separateThread=false;

    private static AtomicLong l= new AtomicLong(0);

    public String getCmd() {
        return cmd;
    }

    public String getParams() {
        return params;
    }

    private String cmd;

    public void setParams(String params) {
        this.params = params;
    }

    private  String params;

    public String getIdCmd()
    {
        return idCmd;
    }

    public void setIdCmd(String idCmd)
    {
        this.idCmd = idCmd;
    }

    private  String idCmd;

        public CtrlCmd()
        {
            this("","");
        }

        public CtrlCmd(String cmd)
        {
            this(cmd,"");
        }

        public CtrlCmd(String cmd,String params)
        {

            if (params==null)
                params="";
            this.cmd=cmd;
            this.params=params;
            this.idCmd=String.valueOf(l.getAndIncrement());
        }

        public String getAsString()
        {
            return new StringBuffer().append(cmd).append(CmdConst.PAR_DL).append(idCmd).append(CmdConst.PAR_DL).append(params).toString();
        }

        public StringBuffer buildString(StringBuffer sb)
        {
            sb.append(cmd).append(CmdConst.PAR_DL).append(idCmd).append(CmdConst.PAR_DL).append(params);
            return sb;
        }

        public CtrlCmd initByStringParam(String param)
        {
            String[] parPair=param.split(CmdConst.PAR_DL,-1);
            this.cmd=parPair[0];
            if (parPair.length>1)
                this.idCmd=parPair[1];
            if (parPair.length>2)
                this.params=param.substring((parPair[0]+";"+parPair[1]+";").length());
            if (idCmd==null || idCmd.length()==0)
                this.idCmd=String.valueOf(l.getAndIncrement());
            return this;
        }
}
