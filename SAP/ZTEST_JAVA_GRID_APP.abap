*&---------------------------------------------------------------------*
*& Report  ZTEST_JAVA_GRID_APP
*&
*&---------------------------------------------------------------------*
*&
*&
*&---------------------------------------------------------------------*

REPORT ZTEST_JAVA_GRID_APP.


"-Variables---------------------------------------------------------
     DATA:
        GL_SPEC1 TYPE REF TO cl_gui_custom_container,
        gr_j Type Ref To ZC4JGUIBRIDGE,
        checkBridge TYPE String,
        bridge_name TYPE String,
        as_sys TYPE String,
        checkBridgeI TYPE I,
        errstart TYPE I,
        l_param TYPE STRING,
        l_param2 TYPE C,
        AV_DEBUG_MODE TYPE BOOLEAN.

CLASS c3 DEFINITION.
  PUBLIC SECTION.
    METHODS  h1 FOR EVENT SIZE_CONTROL OF CL_GUI_DOCKING_CONTAINER.
    METHODS  h2 FOR EVENT LEFT_CLICK_RUN OF CL_GUI_DOCKING_CONTAINER.
    METHODS  jdocviewer_event for event ONCHANGE of ZC4JGUIBRIDGE.

ENDCLASS.

CLASS c3 IMPLEMENTATION.
  METHOD jdocviewer_event.
      DATA lt_cmds       TYPE ZC4JGUIBRIDGE=>ttcmds.

    gr_j->getctrlcmds(
      CHANGING
        cmds = lt_cmds
      EXCEPTIONS
        in_cmd_format_error = 1 ).
    IF sy-subrc = 1.
      gr_j->putincmd( 'CMD_BRIDGE_ERROR;;' ).
   ENDIF.

   gr_j->putincmd( 'initBeanByParameters;;full' ).

   CALL METHOD cl_gui_cfw=>flush.

  ENDMETHOD.

  METHOD h1.

    DATA iix TYPE I.
    iix = iix + 1.

  ENDMETHOD.
  METHOD h2.

    DATA iix TYPE I.
    iix = iix + 1.

  ENDMETHOD.

ENDCLASS.

Start-Of-Selection.

  DATA
                ref_c3 type ref to c3.


  CREATE OBJECT ref_c3.
    "-Main--------------------------------------------------------------
    sy-DYNNR = 1100.

      CREATE OBJECT  GL_SPEC1
       EXPORTING
         container_name = 'GL_SPEC1'.

        Create Object gr_j type ZC4JGUIBRIDGE

          Exporting
            PARENT1 = GL_SPEC1

          Exceptions

            Others = 1.


    gr_j->ADDURLSTRING(
    EXPORTING URLSTRING =
    '"C:/4SAPGUI/JInSGUI.jar" -wkDir"C:/4SAPGUI"' ). "Для работы в штатном режиме

      AV_DEBUG_MODE  = ''.
      as_sys =''.

      IF AV_DEBUG_MODE = 'X' .
        checkBridgeI = gr_j->rmUrlString( 'CHECK_ALIVE').
        IF checkBridgeI NE  -1.
          RAISE cntl_no_objectX.
        ENDIF.
      ENDIF.

      concatenate as_sys '_' sy-sysid '_' sy-UNAME '_' sy-MANDT into bridge_name.
      checkBridge = gr_j->STARTBRIDGE( bridge_name ).
      WAIT UP TO 1 SECONDS.

      IF AV_DEBUG_MODE <> 'X' AND STRLEN( checkBridge ) = 0.
        RAISE cntl_no_objectX.
      ENDIF.

      IF AV_DEBUG_MODE <> 'X' AND STRLEN( checkBridge ) <> 0.
        find FIRST OCCURRENCE OF 'Error process starting:' IN checkBridge MATCH OFFSET errstart.
        if sy-subrc eq 0.
            RAISE CNTL_ERROR_START.
        ENDIF.

        IF checkBridge NE 'OK'.
          RAISE CNTL_START_WITH_WARNING.
        ENDIF.
      ENDIF.

      l_param = 'showDocument;1;JGRID;NON=NON'.
      gr_j->putincmd( l_param ).

         "  REF_DOC->REG_EVENT_MOVE_CONTROL( ).
*           REF_DOC->
*           REF_DOC->REG_EVENT_LEFT_CLICK_RUN_MODE( ).
*           REF_DOC->REG_EVENT_SIZE_CONTROL( ).


        DATA res TYPE CNTL_SIMPLE_EVENTS.
        " REF_DOC->GET_REGISTERED_EVENTS( IMPORTING EVENTS = res ).

***        SET HANDLER ref_c3->h1 FOR REF_DOC.
*        SET HANDLER ref_c3->h2 FOR REF_DOC.
        SET HANDLER ref_c3->jdocviewer_event FOR gr_j.



      Call Screen 1100.


"-End-------------------------------------------------------------------
*&---------------------------------------------------------------------*
*&      Module  STATUS_1100  OUTPUT
*&---------------------------------------------------------------------*
*       text
*----------------------------------------------------------------------*
MODULE STATUS_1100 OUTPUT.
*  SET PF-STATUS 'xxxxxxxx'.
*  SET TITLEBAR 'xxx'.

ENDMODULE.                 " STATUS_1100  OUTPUT
*&---------------------------------------------------------------------*
*&      Module  USER_COMMAND_1100  INPUT
*&---------------------------------------------------------------------*
*       text
*----------------------------------------------------------------------*
MODULE USER_COMMAND_1100 INPUT.
  DATA ix TYPE I.


ix = ix + 1.

case sy-ucomm.

when 'DOUBLE_CLICK'.
  ix =  ix + 1.
  ENDCASE.
ENDMODULE.                 " USER_COMMAND_1100  INPUT