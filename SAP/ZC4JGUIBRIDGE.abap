class ZC4JGUIBRIDGE definition
  public
  inheriting from CL_GUI_CONTROL
  create public .

public section.

  types:
    BEGIN OF t_param,
        pname TYPE STRING,
        pval  TYPE STRING,
    END OF t_param .
  types:
    tparams TYPE TABLE OF T_PARAM WITH KEY pname .
  types:
    BEGIN OF t_cmd, "Тип команды
         cmd TYPE String,
         idcmd  TYPE String,
         params TYPE tparams,
   END OF t_cmd .
  types:
    ttcmds TYPE TABLE OF t_cmd WITH KEY cmd idcmd .

  events ONCHANGE
    exporting
      value(TEXT) type STRING .

  methods CONSTRUCTOR
    importing
      !PARENT1 type ref to CL_GUI_OBJECT optional
    exceptions
      CNTL_ERROR
      CNTL_INITIAL_ERROR .
  methods ADDURLSTRING
    importing
      !URLSTRING type STRING
    returning
      value(RES) type I .
  methods CLEARSTUB .
  methods GETCTRLCMDS
    changing
      !CMDS type TTCMDS
    exceptions
      IN_CMD_FORMAT_ERROR .
  methods PUTINCMD_RV
    importing
      !P_IN_STR type STRING
    exporting
      !OV_STR type STRING .
  methods PUTINCMD
    importing
      !P_IN_STR type STRING .
  methods RMURLSTRING
    importing
      !URLSTRING type STRING
    returning
      value(RES) type I .
  methods TESTEXCHANGE
    importing
      !TESTSTRING type STRING
    returning
      value(RES) type STRING .
  methods STARTBRIDGE
    importing
      value(BRIDGE_NAME) type STRING default ''
    returning
      value(RES_STATUS) type STRING .
  methods STOPBRIDGE .
  methods GETSERVERSTATUS
    returning
      value(RES_STATUS) type STRING .
  methods ABOUTLIB
    returning
      value(ABOUT) type STRING .
  methods BEFOREFREE
    importing
      !P_IN type STRING optional
    returning
      value(P_RETV) type STRING .
  methods PARSEINCMD
    importing
      !P_INCMD type STRING
    changing
      !CMDS type TTCMDS
    exceptions
      IN_CMD_FORMAT_ERROR .
  methods SETJAVACMD
    importing
      !IV_JAVACMD type STRING
    returning
      value(RES) type I .

  methods DISPATCH
    redefinition .
  methods FINALIZE
    redefinition .
  methods SET_REGISTERED_EVENTS
    redefinition .
protected section.
  class-data AT_CHANE_HEAD type CHAR1 .
private section.
ENDCLASS.



CLASS ZC4JGUIBRIDGE IMPLEMENTATION.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->ABOUTLIB
* +-------------------------------------------------------------------------------------------------+
* | [<-()] ABOUT                          TYPE        STRING
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method ABOUTLIB.
      CALL METHOD OF h_control-obj 'aboutLib' = ABOUT.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->ADDURLSTRING
* +-------------------------------------------------------------------------------------------------+
* | [--->] URLSTRING                      TYPE        STRING
* | [<-()] RES                            TYPE        I
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method ADDURLSTRING.
    DATA url4set TYPE String.

    CONCATENATE ' -jar' URLSTRING INTO url4set SEPARATED BY space.

    call method  of h_control-obj  'setJarServerUrl' = res exporting #1 = url4set.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->BEFOREFREE
* +-------------------------------------------------------------------------------------------------+
* | [--->] P_IN                           TYPE        STRING(optional)
* | [<-()] P_RETV                         TYPE        STRING
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method BEFOREFREE.
DATA:
        l_param TYPE STRING.

        l_param = 'getCmdByCmd;113;CMD_EXIT' .
        call method  of h_control-obj  'sendCmd' = P_RETV EXPORTING #1 = l_param.

  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->CLEARSTUB
* +-------------------------------------------------------------------------------------------------+
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method CLEARSTUB.
*    CALL METHOD OF
*      h_control-obj 'clearStub'.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->CONSTRUCTOR
* +-------------------------------------------------------------------------------------------------+
* | [--->] PARENT1                        TYPE REF TO CL_GUI_OBJECT(optional)
* | [EXC!] CNTL_ERROR
* | [EXC!] CNTL_INITIAL_ERROR
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method CONSTRUCTOR.
CLASS cl_gui_cfw DEFINITION LOAD.

    DATA: lv_ctrl_name(80) TYPE c.
    DATA: lv_STYLE TYPE i.

    IF NOT cl_gui_object=>activex IS INITIAL.
      lv_ctrl_name = 'ActiveXBridge.MWLib.1'.
    ENDIF.


  IF lv_style IS INITIAL.
* otherwise the control would be invisible and the mistake would be
* hard to find
      lv_style = cl_gui_control=>ws_visible + cl_gui_control=>ws_child + cl_gui_control=>ws_clipsiblings.
    ENDIF.

SUPER->CONSTRUCTOR(
  EXPORTING
    CLSID              = lv_ctrl_name
    LIFETIME           = 2 "lifetime_default
    SHELLSTYLE         = lv_style
    PARENT             = parent1
    AUTOALIGN          = 'x'
  EXCEPTIONS
    CNTL_ERROR         = 1
    CNTL_SYSTEM_ERROR  = 2
    CREATE_ERROR       = 3
    LIFETIME_ERROR     = 4
    PARENT_IS_SPLITTER = 5
    others             = 6
       ).

IF SY-SUBRC <> 0.
  RAISE cntl_initial_error.
ENDIF.


DATA: my_simple_event TYPE cntl_simple_event,
        my_simple_events TYPE cntl_simple_events.

  my_simple_event-eventid = 16411. "CLICK IT"
  my_simple_event-appl_event = space.
  APPEND my_simple_event TO my_simple_events.

  my_simple_event-eventid = 3. "CLICK IT"
  my_simple_event-appl_event = space.
  APPEND my_simple_event TO my_simple_events.

  my_simple_event-eventid = 2. "CLICK IT"
  my_simple_event-appl_event = space.
  APPEND my_simple_event TO my_simple_events.

  my_simple_event-eventid = 1. "CLICK IT"
  my_simple_event-appl_event = space.
  APPEND my_simple_event TO my_simple_events.

  CALL METHOD me->set_registered_events
    EXPORTING
      events = my_simple_events.

*CALL METHOD cl_gui_cfw=>flush
*    EXCEPTIONS
*      cntl_system_error = 1
*      cntl_error        = 2
*      OTHERS            = 3.
*
*  CASE sy-subrc.
*    WHEN 0.
*    WHEN 1.
*      RAISE cntl_initial_error.
*    WHEN OTHERS.
*      RAISE cntl_error.
*  ENDCASE.

  CALL METHOD CL_GUI_CFW=>SUBSCRIBE
    EXPORTING
      shellid = me->h_control-shellid
      ref = me
    EXCEPTIONS
      OTHERS = 1.

IF sy-subrc NE 0.
    RAISE cntl_error.
  ENDIF.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->DISPATCH
* +-------------------------------------------------------------------------------------------------+
* | [--->] CARGO                          TYPE        SYUCOMM
* | [--->] EVENTID                        TYPE        I
* | [--->] IS_SHELLEVENT                  TYPE        CHAR1
* | [--->] IS_SYSTEMDISPATCH              TYPE        CHAR1(optional)
* | [EXC!] CNTL_ERROR
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method DISPATCH.

CASE eventid.
    WHEN 16411 OR 1 OR 2 OR 3.
      CALL METHOD cl_gui_cfw=>flush.
      RAISE EVENT onchange EXPORTING text = 'PROP_CHANE'.
    WHEN OTHERS.
      RAISE cntl_error.
  ENDCASE.
*CALL METHOD SUPER->DISPATCH
*  EXPORTING
*    CARGO             =
*    EVENTID           =
*    IS_SHELLEVENT     =
**    IS_SYSTEMDISPATCH =
**  EXCEPTIONS
**    CNTL_ERROR        = 1
**    others            = 2
*        .
*IF SY-SUBRC <> 0.
** Implement suitable error handling here
*ENDIF.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->FINALIZE
* +-------------------------------------------------------------------------------------------------+
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method FINALIZE.
    CALL METHOD SUPER->FINALIZE.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->GETCTRLCMDS
* +-------------------------------------------------------------------------------------------------+
* | [<-->] CMDS                           TYPE        TTCMDS
* | [EXC!] IN_CMD_FORMAT_ERROR
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method GETCTRLCMDS.

   DATA :
         ls_tmp TYPE string.

  CALL METHOD OF
    h_control-obj 'getResult' = ls_tmp.

    PARSEINCMD( EXPORTING P_INCMD = LS_TMP CHANGING CMDS = CMDS EXCEPTIONS IN_CMD_FORMAT_ERROR = 1 ).
    IF sy-subrc = 1.
      RAISE IN_CMD_FORMAT_ERROR.
    ENDIF.



  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->GETSERVERSTATUS
* +-------------------------------------------------------------------------------------------------+
* | [<-()] RES_STATUS                     TYPE        STRING
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method GETSERVERSTATUS.
      CALL METHOD OF h_control-obj 'getServerStatus' = RES_STATUS.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->PARSEINCMD
* +-------------------------------------------------------------------------------------------------+
* | [--->] P_INCMD                        TYPE        STRING
* | [<-->] CMDS                           TYPE        TTCMDS
* | [EXC!] IN_CMD_FORMAT_ERROR
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method PARSEINCMD.
    DATA:
      ls_tmp TYPE string,
      lr_cmd TYPE T_CMD,

      lt_cmds TYPE TABLE OF string,
      lt_params TYPE TABLE OF string,
      lt_parvals TYPE TABLE OF string,
      li_ix TYPE i.

FIELD-SYMBOLS: <lp> TYPE T_PARAM,
               <lcmds> LIKE LINE OF lt_cmds.

  ls_tmp = P_INCMD.
  SPLIT ls_tmp AT  '%%'  INTO TABLE lt_cmds.


  LOOP AT LT_cmds  ASSIGNING <lcmds>.

     CLEAR LR_CMD.
     SEARCH <lcmds> FOR ';'.
     IF sy-SUBRC <> 0.
       RAISE IN_CMD_FORMAT_ERROR."Ошибочный формат данных
     ENDIF.
     li_ix = sy-FDPOS.
     LR_CMD-CMD = <lcmds>+0(li_ix).
     li_ix = li_ix + 1.

     ls_tmp = <lcmds>+li_ix.
     SEARCH ls_tmp FOR ';'.
     IF sy-SUBRC <> 0.
         RAISE IN_CMD_FORMAT_ERROR."Ошибочный формат данных
     ENDIF.
     li_ix = sy-FDPOS.
     LR_CMD-IDCMD = ls_tmp+0(li_ix).
     li_ix = li_ix + 1.

     ls_tmp = ls_tmp+li_ix.

   IF LR_CMD-CMD EQ 'CALL_RFC'.

      APPEND INITIAL LINE TO LR_CMD-PARAMS ASSIGNING <lp>.
      <lp>-pname = 'pars'.
      <lp>-PVAL = ls_tmp.

   ELSE.

    SPLIT ls_tmp AT  ';'  INTO TABLE lt_params.

    FIELD-SYMBOLS: <lPARAM> LIKE LINE OF  lt_params.

    LOOP AT lt_params  ASSIGNING <lPARAM>.

     APPEND INITIAL LINE TO LR_CMD-PARAMS ASSIGNING <lp>.
     SPLIT <lPARAM>  AT  '='  INTO TABLE lt_parvals.

       "<lp>-pname = lt_parvals[ 1 ].
       READ TABLE LT_PARVALS INTO <lp>-pname INDEX 1.
       IF lines( lt_parvals ) > 1.
         READ TABLE LT_PARVALS INTO <lp>-pval INDEX 2.
         "<lp>-PVAL = lt_parvals[ 2 ].
       ELSE.
         CLEAR <lp>-PVAL.
       ENDIF.

    ENDLOOP.
   ENDIF.

    MODIFY CMDS FROM LR_CMD TRANSPORTING PARAMS CMD WHERE CMD = LR_CMD-CMD AND IDCMD = LR_CMD-IDCMD.
    IF SY-SUBRC <> 0.
      APPEND LR_CMD TO CMDS.
    ENDIF.

  ENDLOOP.

  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->PUTINCMD
* +-------------------------------------------------------------------------------------------------+
* | [--->] P_IN_STR                       TYPE        STRING
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method PUTINCMD.
        call method  of h_control-obj  'sendCmd'  EXPORTING #1 = P_IN_STR.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->PUTINCMD_RV
* +-------------------------------------------------------------------------------------------------+
* | [--->] P_IN_STR                       TYPE        STRING
* | [<---] OV_STR                         TYPE        STRING
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method PUTINCMD_RV.
   call method  of h_control-obj  'sendCmd' = OV_STR EXPORTING #1 = P_IN_STR.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->RMURLSTRING
* +-------------------------------------------------------------------------------------------------+
* | [--->] URLSTRING                      TYPE        STRING
* | [<-()] RES                            TYPE        I
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method RMURLSTRING.

  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->SETJAVACMD
* +-------------------------------------------------------------------------------------------------+
* | [--->] IV_JAVACMD                     TYPE        STRING
* | [<-()] RES                            TYPE        I
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method SETJAVACMD.


    call method  of h_control-obj  'setJavaCmd' = res exporting #1 = iv_javacmd.

  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->SET_REGISTERED_EVENTS
* +-------------------------------------------------------------------------------------------------+
* | [--->] EVENTS                         TYPE        CNTL_SIMPLE_EVENTS
* | [EXC!] CNTL_ERROR
* | [EXC!] CNTL_SYSTEM_ERROR
* | [EXC!] ILLEGAL_EVENT_COMBINATION
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method SET_REGISTERED_EVENTS.

    data: simple_event type cntl_simple_event, "// event
          ex_event     type cntl_event,"// eventid, is_shellevent
          events_ex type cntl_events.  "// table

*   map simple_event into ex_event, append to events_ex
*    loop at events into simple_event.
*      case simple_event-eventid.
*     when 2.
*            ex_event-eventid = 2.
*            ex_event-is_shellevent = ' '.
*         check for system / application event
*     if simple_event-appl_event is initial.
*      ex_event-is_systemevent = 'X'.
*     endif.
*    append ex_event to events_ex.
*  when others.
*    raise illegal_event_combination.
*  endcase.
*  endloop.

LOOP AT events INTO simple_event.
    ex_event-eventid = simple_event-eventid.
    ex_event-is_shellevent = space.
    IF simple_event-appl_event IS INITIAL.
      ex_event-is_systemevent = 'X'.
    ENDIF.
    APPEND ex_event TO events_ex.
    CLEAR simple_event.
    CLEAR ex_event.
  ENDLOOP.

  call method me->set_registered_events_ex
  exporting
  eventtab = events_ex
  exceptions
    cntl_error                = 1
    cntl_system_error         = 2
    illegal_event_combination = 3
  others                    = 4.

  case sy-subrc.
    when 0.
    when 1. raise cntl_error.
    when 2. raise cntl_system_error.
    when 3. raise illegal_event_combination.
    when others. raise cntl_error.
  endcase.

registered_simple_events[] = events.

endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->STARTBRIDGE
* +-------------------------------------------------------------------------------------------------+
* | [--->] BRIDGE_NAME                    TYPE        STRING (default ='')
* | [<-()] RES_STATUS                     TYPE        STRING
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method STARTBRIDGE.

      IF strlen( BRIDGE_NAME ) > 0.
        DATA LV_ABOUT TYPE STRING.

        LV_ABOUT = ABOUTLIB( ).
        IF strlen( LV_ABOUT ) > 0.
          CALL METHOD OF h_control-obj 'startBridgeEx' = RES_STATUS EXPORTING #1 = BRIDGE_NAME.
        ELSE.
          CALL METHOD OF h_control-obj 'startBridge' = RES_STATUS.
        ENDIF.
      ELSE.
        CALL METHOD OF h_control-obj 'startBridge' = RES_STATUS.
      ENDIF.

  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->STOPBRIDGE
* +-------------------------------------------------------------------------------------------------+
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method STOPBRIDGE.
        CALL METHOD OF h_control-obj 'stopBridge'.
  endmethod.


* <SIGNATURE>---------------------------------------------------------------------------------------+
* | Instance Public Method ZC4JGUIBRIDGE->TESTEXCHANGE
* +-------------------------------------------------------------------------------------------------+
* | [--->] TESTSTRING                     TYPE        STRING
* | [<-()] RES                            TYPE        STRING
* +--------------------------------------------------------------------------------------</SIGNATURE>
  method TESTEXCHANGE.

    DATA:
        l_param TYPE STRING.

        CONCATENATE  'testExChange;113;' TESTSTRING INTO L_PARAM.
        call method  of h_control-obj  'sendCmd' = RES EXPORTING #1 = l_param.

*        call method  of h_control-obj  'testExChange' = RES exporting #1 = TESTSTRING.
  endmethod.
ENDCLASS.