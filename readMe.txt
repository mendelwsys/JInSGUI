An example of integrating SAP GUIx32 for Windows with a java8x64 Application via an ActiveX bridge.

In order to run the example, you need to register Activex BridgeX4Java.dll ./CPP/bin/
BridgeX4Java.dll is a 32x bit library for exchanging messages with a Java server.
In this example, messages are exchanged over a socket.
The Java Server is a separate x64 process, which significantly expands the memory available for use.

As an example of a call with a parameter, the TESTEXCHANGE method is implemented, on the java server
String testExChange(String testString) in the IGridStub interface.

It is possible to extend the set of Java methods available to call from abap. 
An example of such an extension is described in new—allSample.txt.
In order to expand the set, you need to add a method description to the interface
void Sample_With_3_Args(String string_Arg_1, int int_Arg_1, String string_Arg_2);
And make its implementation in ServerJGridBean01.

As you can see from the example, the general format of the call description is: 
"Method_name;Method_Id;Param_1;Param_2;...;Param_n"
Naturally, the question arises that if the string parameter contains the separator ';' in this case
you can use, for example Url Ecoding, a on the server side Url Decoding for the this parameter

It is also possible to indicate to the server that the method call must be carried out in a separate thread,
(this is sometimes necessary so as not to block the dialog exchange of SAP GUI and SAP BackEnd) 
for this, it is necessary pass parameter @@THR in parameter array: 
"Method_name;Method_Id;@@THR;Param_1;Param_2;...;Param_n".
see also new—allSample.txt.
