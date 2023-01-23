package tst;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 24.08.2018
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public interface ICommonGuiStub
{

    class FName2VarStruct
    {
        public FName2VarStruct(String VARNAME, String VARVALUE)
        {
            this.VARNAME = VARNAME;
            this.VARVALUE = VARVALUE;
        }

        public FName2VarStruct(String VARNAME, String VARVALUE, String[][] SUB_FNAME2VAR)
        {
            this.VARNAME = VARNAME;
            this.VARVALUE = VARVALUE;


            if (SUB_FNAME2VAR!=null)
            {
                this.SUB_FNAME2VAR = new FName2VarStruct[SUB_FNAME2VAR.length];
                for (int i = 0; i < SUB_FNAME2VAR.length; i++)
                    this.SUB_FNAME2VAR[i] = new FName2VarStruct(SUB_FNAME2VAR[i][0],SUB_FNAME2VAR[i][1]);
            }
        }
        public FName2VarStruct(String VARNAME,  String[] SUB_FNAME2VAR)
        {
            this.VARNAME = VARNAME;


            if (SUB_FNAME2VAR!=null)
            {
                this.SUB_FNAME2VAR = new FName2VarStruct[SUB_FNAME2VAR.length];
                for (int i = 0; i < SUB_FNAME2VAR.length; i++)
                    this.SUB_FNAME2VAR[i] = new FName2VarStruct(SUB_FNAME2VAR[i],(String)null);
            }
        }

        public FName2VarStruct(String VARNAME, String VARVALUE, FName2VarStruct[] SUB_FNAME2VAR) {
            this.VARNAME = VARNAME;
            this.VARVALUE = VARVALUE;
            this.SUB_FNAME2VAR = SUB_FNAME2VAR;
        }

        public String getVARNAME()
        {
            return VARNAME;
        }

        public String getVARVALUE() {
            return VARVALUE;
        }

        public FName2VarStruct() {
        }

        public String VARNAME;
        public String VARVALUE;

        public FName2VarStruct[] getSUB_FNAME2VAR() {
            return SUB_FNAME2VAR;
        }

        public FName2VarStruct[] SUB_FNAME2VAR;
    }



    String MODE_JGRID = "JGRID";
    String MODE_DOC_VIEW = "VIEW";
    String MODE_VNM = "MODE";

    int maxBlockSize = 1000;


    void initBeanByParameters(String in_params);

    String putInCmd(String inCmd);

    void showDocument(String mode, String in_parameters);


}
