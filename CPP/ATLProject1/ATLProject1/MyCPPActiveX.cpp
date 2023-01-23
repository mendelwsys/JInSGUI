// MyBridgeX4Java.cpp : Implementation of CMyBridgeX4Java
#include "stdafx.h"
#include "MyQueue.h"
#include "MyCPPActiveX.h"
#include <codecvt>
#include <string>


//HANDLE CMyBridgeX4Java::m_hProcess = 0;
int CMyBridgeX4Java::cntObjects = 0;
std::mutex CMyBridgeX4Java::cnt_mutex;

std::map<std::wstring, CConnectDesc> CMyBridgeX4Java::sysName2Socket;
std::mutex CMyBridgeX4Java::map_mutex;


void CMyBridgeX4Java::EmbedWindow(HWND hWnd4Embed)
{
	//HWND calcHwnd = FindWindow(L"CalcFrame", NULL);
	//GetModuleBaseNameW
	//HWND Hwnd4Embed = FindWindow(L"SunAwtFrame", L"RZD Reporting System");
	//if (Hwnd4Embed != NULL)
	{
		// Change the parent so the calc window belongs to our apps main window 
		::SetParent(hWnd4Embed, m_hWnd);


		DWORD style = ::GetWindowLong(hWnd4Embed, GWL_STYLE); //get the b style
		style &= ~(WS_SIZEBOX | WS_POPUP | WS_CAPTION); //reset the "caption" and "popup" bits
		//style &= ~(WS_POPUP | WS_CAPTION); //reset the "caption" and "popup" bits
		style |= WS_CHILD; //set the "child" bit
		::SetWindowLong(hWnd4Embed, GWL_STYLE, style); //set the new style of b
		
													 // Update the style so the calc window is embedded in our main window
													 //		SetWindowLong(calcHwnd, GWL_STYLE, GetWindowLong(calcHwnd, GWL_STYLE) | WS_CHILD);

													 // We need to update the position as well since changing the parent does not
													 // adjust it automatically.
		//SetWindowPos(hWnd4Embed, NULL, 0, 0, 0, 0, SWP_DRAWFRAME | SWP_NOSIZE | SWP_NOZORDER);
		RECT Rect;
		ZeroMemory(&Rect, sizeof(Rect));
		BOOL res=GetWindowRect(&Rect);


		//SetWindowPos(hWnd4Embed, NULL, 0, 0, Rect.right- Rect.left, Rect.bottom-Rect.top, SWP_NOSIZE | SWP_NOZORDER);
		//SetWindowPos(hWnd4Embed, NULL, 0, 0, 0, 0, SWP_NOSIZE | SWP_NOZORDER);
		LONG wEmbed = Rect.right - Rect.left;
		LONG hEmbed = Rect.bottom - Rect.top;
		::SetWindowPos(hWnd4Embed, NULL, 0, 0, wEmbed, hEmbed, SWP_NOZORDER);
		//::SetWindowPos(hWnd4Embed, HWND_TOPMOST, 0, 0, wEmbed, hEmbed, 0);
//		::SetCapture(hWnd4Embed);
	}
}


std::wstring CMyBridgeX4Java::_createJavaServer(std::string& out_port, std::string& in_port)
{
	std::wstring resCmd;
	std::wstring l_cmd = _getFullCmdLine();
	STARTUPINFO cif;
	ZeroMemory(&cif, sizeof(STARTUPINFO));
	PROCESS_INFORMATION l_pi;
	DWORD resVal;

	wchar_t* l_runCmd = new wchar_t[l_cmd.length() + 1];
	ZeroMemory(l_runCmd, (l_cmd.size() + 1) * sizeof(wchar_t));
	memcpy(l_runCmd, l_cmd.c_str(), l_cmd.size()* sizeof(wchar_t));

	//TODO
	//1. Расширить интерфейс для установки процесса (для того что бы можно было например запускать батник или другой exe)+
	//2. Вести журнал java, видимо в саповском tmp+
	//3. Надо как-то уметь вручную перезапускать сервер. (Вызов через sapGUI) +(startBridge и stopBridge)
	//4. Заканчивать процесс в диструкторе? когда все окна закрыты+
	//5. Заканчивать автоматом процесс на стороне Java ?, когда все потоки обработки закрыты (через минуту)+
	//6. Получение состояния сервера.

	LPCTSTR lpApplicationName = NULL;
	if (m_appName.length() != 0)
		lpApplicationName = m_appName.c_str();

	if (CreateProcess(lpApplicationName, l_runCmd , NULL, NULL, FALSE, CREATE_NO_WINDOW, NULL, NULL, &cif, &l_pi) == TRUE)
	{
		Sleep(1000);
		int cntWt = 20; //Даем не более 20 секунд для запуска процесса серевера отображения.
		int resChannel = -1;
		do
		{
			 resChannel = initChannels(out_port, in_port);
			 if (!resChannel)
			 {
				 m_getEventThread = new std::thread(thunk2, this, &CMyBridgeX4Java::fireOutPutThread);
				 resCmd = DEF_OK_STATUS;
				 return resCmd;
			 }
			cntWt--;
		} while ((resVal = WaitForSingleObject(l_pi.hProcess, 1000)) == WAIT_TIMEOUT && cntWt>0);

		resCmd = L"Server was start, but can't initChannels to server with code:";
		resCmd = resCmd + L"resVal:" + std::to_wstring(resVal) + L" resChannel:" +std::to_wstring(resChannel)+L" and cntWt:"+ std::to_wstring(cntWt);
	}
	else
	{
		resVal = GetLastError();
		resCmd = L" Error process starting: "+std::to_wstring(resVal)+L" Cmd: "+ _getFullCmdLine();
	}
	return resCmd;
}

// int initJVM()
//{
//		
//	using namespace std;
//
//	if (true)
//		return 0;
//
//	if (jvm == NULL)
//	{
//		{
//			//jsize  cntVms = 0;
//			//jint rc = JNI_GetCreatedJavaVMs(&jvm, 1, &cntVms);  // YES !!
//			//if (jvm != NULL)
//			//{
//			//	rc = jvm->GetEnv((void**)&env, JNI_VERSION_1_6);
//			//	if (rc != JNI_OK) 
//			//	{
//			//		return -2;
//			//	}
//			//}
//			//else
//			{
//				JavaVMInitArgs vm_args;                        // Initialization arguments
//				JavaVMOption* options = new JavaVMOption[1];   // JVM invocation options
//															   //	options[0].optionString = "-Djava.class.path=.";   // where to find java .class
//				options[0].optionString = "-Djava.class.path=C:\\PapaWK\\Projects\\JavaProj\\TestBean1\\out\\production\\TestBean1";
//				vm_args.version = JNI_VERSION_1_6;             // minimum Java version
//				vm_args.nOptions = 1;                          // number of options
//				vm_args.options = options;
//				vm_args.ignoreUnrecognized = false;     // invalid options make the JVM init fail
//
//
//				jint rc = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);  // YES !!
//				delete options;    // we then no longer need the initialisation options. 
//				if (rc != JNI_OK) {
//					// TO DO: error processing... 
//					//	cin.get();
//					//		exit(EXIT_FAILURE);
//					return -1;
//				}
//			}
//		}
//
//
//	}
//
//	int getEnvStat = jvm->GetEnv((void **)&env, JNI_VERSION_1_6);
//	if (getEnvStat == JNI_EDETACHED) 
//	{
//		if (jvm->AttachCurrentThread((void **)&env, NULL) != 0) 
//		{
//			return -14;
//		}
//	}
//
//
//	//if (_cls2 != NULL)
//	//{
//	//	env->DeleteGlobalRef(_cls2);
//	//}
//	
//	if (_cls2==NULL)
//	{
//		jclass cls2;
//		cls2 = env->FindClass("su/mwlib/tst/MainClass");  // try to find the class
//		if (cls2 == NULL)
//			return -10;
//		_cls2 = (jclass)env->NewGlobalRef(cls2);
//		env->DeleteLocalRef(cls2);
//	}
//
////	if (_callJFrame==NULL)
//	_callJFrame = env->GetStaticMethodID(_cls2, "callJFrame", "([Ljava/lang/String;I)J");// find method
//	if (_callJFrame == NULL)
//		return -11;
//
////	if (_setFrameSize == NULL)
//	_setFrameSize = env->GetStaticMethodID(_cls2, "setFrameSize", "(II)V");// find method
//	if (_setFrameSize == NULL)
//		return -12;
//	
//	return 0;
//}
//

LRESULT CMyBridgeX4Java::OnSize(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& /*bHandled*/)
{
	WORD w = LOWORD(lParam);
	WORD h = HIWORD(lParam);

	if (m_hWnd4Embed && w>0 && h>0)
	{
		RECT Rect;
		ZeroMemory(&Rect, sizeof(Rect));
		BOOL res = ::GetWindowRect(m_hWnd4Embed, &Rect);
		
		LONG wEmbed = Rect.right-Rect.left;
		LONG hEmbed = Rect.bottom - Rect.top;
		if (wEmbed!=w || hEmbed!=h)
			::SetWindowPos(m_hWnd4Embed, NULL, 0, 0, w, h, SWP_NOZORDER);
	}



	return LRESULT();
}
// CMyBridgeX4Java
 LRESULT CMyBridgeX4Java::OnCommand(UINT, WPARAM, LPARAM, BOOL &)
 {
	 Fire_MyEv();
	 return LRESULT();
 }

 LRESULT CMyBridgeX4Java::OnLButtonDown(UINT /*uMsg*/, WPARAM /*wParam*/, LPARAM lParam, BOOL& /*bHandled*/)
 {
	 // TODO: Add your message handler code here and/or call default
	 WORD xPos = LOWORD(lParam);  // horizontal position of cursor
	 WORD yPos = HIWORD(lParam);  // vertical position of cursor
	 int cConnections = m_vec.GetSize();
	 m_retStat = cConnections+100;


	 HWND resH=GetTopWindow();
	 if (m_hWnd4Embed == resH)
		 printf("H");

//	 Fire_RiseEvent();
//	 Fire_MyEv();

	 //InvalidateRect(m_hWnd, NULL, NULL);
	 //UpdateWindow();

	 return 0;
 }


 //Поток сообщает GUI о пришедшей команде в выходной поток.
 void CMyBridgeX4Java::fireOutPutThread()
 {
	 while (!m_break)
	 {
		 while (!m_outQueue.empty())
		 {
			 SendMessage(WM_COMMAND, 1001, 0);//Fire event from right thread context

//				auto start = std::chrono::high_resolution_clock::now();
			 std::this_thread::sleep_for(std::chrono::milliseconds(300));
			 //				auto end = std::chrono::high_resolution_clock::now();
		 }
		 std::this_thread::sleep_for(std::chrono::milliseconds(300));
	 }
 }


 int CMyBridgeX4Java::initChannels(std::string& out_port, std::string& in_port)
 {
	 stopChannels();
	 int res = create_channel(m_out_ConnectSocket, out_port.c_str());
	 if (!res)
	 {
		 res = create_channel(m_in_ConnectSocket, in_port.c_str());
		 if (!res)
		 {
			 //Инициализация серверной сессии переда началом обмена данных
			 myCmd initSession("0",CMD_INIT_SESSIONS,NO_PARAMS), resCmd;
			 std::string resS;
			 int res = send_out_channel(initSession.toString().c_str(), m_out_ConnectSocket, resS);
			 if (res < 0)
				 stopBridge(0);
			 else
			 {
				 resCmd.initByStringParam(resS);
				 if (resCmd.getCmd() == initSession.getCmd() && resCmd.getIdCmd() == initSession.getIdCmd())
				 { //get Answer for sessionId
					 myCmd initSession2("1", CMD_INIT_SESSIONS, (m_sessionId=resCmd.getParams()).c_str());
					 resS.clear();
					 send_out_channel(initSession2.toString().c_str(), m_in_ConnectSocket, resS);
					 resCmd.initByStringParam(resS);
					 if (resCmd.getCmd() == initSession2.getCmd() && resCmd.getIdCmd() == initSession2.getIdCmd())
					 {
						 m_onGetThread = new std::thread(thunk2, this, &CMyBridgeX4Java::serverGetter);
					 }
				 }
			 }
			
		 }
	 }
	 return res;
 }

 void CMyBridgeX4Java::stopChannels()
 {
	 if (m_onGetThread != NULL)
	 {
		 if (m_onGetThread->joinable())
			 m_onGetThread->join(); //

		 delete m_onGetThread;
		 m_onGetThread = NULL;
	 }

	 if (m_out_ConnectSocket != INVALID_SOCKET)
	 {
		 closesocket(m_out_ConnectSocket);
		 WSACleanup();
		 m_out_ConnectSocket = INVALID_SOCKET;
	 }

	 if (m_in_ConnectSocket != INVALID_SOCKET)
	 {
		 closesocket(m_in_ConnectSocket);
		 WSACleanup();
		 m_in_ConnectSocket = INVALID_SOCKET;
	 }

 }

 //Получает команды от сервера собирает их и передает в выходной поток
 void CMyBridgeX4Java::serverGetter() 
 {
	 int i = 0;
	 for (;;)
	 {
		 if (m_break)
		 {
			 printf("Exit by setting break command");
			 break;
		 }

		 std::string id = std::to_string(i);
		 myCmd request(id.c_str(), CMD_REQUEST, NO_PARAMS), resCmd;
		 std::string sCmd, resS;
		 //sCmd = request.add2String(sCmd);
		 sCmd = request.toString();
		 const char * insCmd = sCmd.c_str();
		 int res = send_out_channel(insCmd, m_in_ConnectSocket, resS);
		 if (!res)
		 {
			 resCmd.initByStringParam(resS);

			 if (resCmd.getCmd() != CMD_SKIP && resCmd.getCmd() != CMD_GET_STATUS)
			 {
				 m_outQueue.push(resCmd);
			 }
				
			 //else if (resCmd.getCmd() == CMD_GET_STATUS)
				// printf("GOT Command in test: %s \n", resCmd.toString().c_str());
			 //resS.clear();
			 //printf("GOT Command in test: %s \n", resCmd.add2String(resS).c_str());
			 i++;
		 }
		 else
		 {
			 printf("Exit by error from test");
			 break;
		 }
	 }
 }

 std::string& CMyBridgeX4Java::getOutPutThread(std::string& in)
 {
	 while (!m_outQueue.empty())
	 {
		 myCmd cmd = m_outQueue.front();
		 m_outQueue.pop();
		 if (in.size() > 0)
			 in += CMD_DL;
		 in = cmd.add2String(in);
	 }
	 return in;
 }


 STDMETHODIMP CMyBridgeX4Java::sendCmd(BSTR p1, BSTR *resOp)
 {
	 //for (int i = 0;; i++)
	 //{
	 //	std::string id = std::to_string(i), sCmd, resS;
	 //	myCmd cmd(id.c_str(), "TEST", "NO PARAMS"), resCmd;
	 //	sCmd = cmd.add2String(sCmd);
	 //	int res = send_out_channel(sCmd.c_str(), out_ConnectSocket, resS);
	 //	if (res < 0)
	 //		break;
	 //	//				printf("GOT answer for output cmd %s \n", resS.c_str());

	 //	resCmd.initByStringParam(resS);
	 //	std::this_thread::sleep_for(std::chrono::milliseconds(3000));
	 //}
	 //if (m_onTestThread != NULL && m_onTestThread->joinable())
		//m_onTestThread->join();

	 //delete m_onTestThread;
	 //m_onTestThread = new std::thread(thunk2, this, &CMyBridgeX4Java::testOutCmd);
	 std::wstring_convert<std::codecvt_utf8_utf16 <wchar_t> > converter;

	 if (m_out_ConnectSocket != INVALID_SOCKET)
	 {
		 myCmd cmd, resCmd;
		 if (p1 != NULL)
		 {
			 std::wstring w_str(p1);
	  		 std::string str = converter.to_bytes(w_str.c_str());
			 cmd.initByStringParam(str);
/*
if (cmd.getCmd() == "CMD_TREE_REBUILD")
{
cmd.initByStringParam(str);
}

*/

		 }
		 //else
			// cmd = createTestCmd();

		 std::string sCmd, resS;
		 //sCmd = cmd.add2String(sCmd);
		 sCmd = cmd.toString();

		 int res = send_out_channel(sCmd.c_str(), m_out_ConnectSocket, resS);
		 if (res < 0)
			 stopBridge(0);
		 else
		 {
			 resCmd.initByStringParam(resS);
			 if (resCmd.getCmd() == cmd.getCmd() && resCmd.getIdCmd() == cmd.getIdCmd())
			 {

				 if (resCmd.getCmd() == "showDocument")
				 {
					 std::string param = resCmd.getParams();
					 long myHandle = std::stol(param);
					 m_hWnd4Embed = (HWND)myHandle;
					 EmbedWindow(m_hWnd4Embed);
				 }
				 else
				 {

					 std::string resStr = resCmd.getParams();
					 std::wstring wstr = converter.from_bytes(resStr);
					 

					 //if (resCmd.getCmd().compare("buildTree") == 0)
					 //{//Check if the string short enough
						// int resLen = resCmd.getParamsLn();

					 //	std::stringstream ss;
					 //	ss << resLen;
					 //	std::string sResLen;
					 //	ss >> sResLen;

						// std::string replstr = "<?xml version=\"1.0\" encoding=\"windows-1251\" ?>\n<NODES>\n<NODE pid='' sel='X'  cid='1' nType='9' nName=' ";
						// replstr += sResLen + " Structure was skiped 7 '/>\n\n</NODES>";
						// if (resCmd.setDebugParams(replstr.c_str()))
						// {
						//	 //*resOp = SysAllocString(wstr.c_str());
						//	 resStr = resCmd.getParams();
						//	 wstr = converter.from_bytes(resStr);
						// }
					 //}
					 *resOp = SysAllocString(wstr.c_str());
					 return S_OK;
				 }
			 }
		 }
		 //	printf("GOT answer for output cmd %s \n", resS.c_str());
	 }
	 *resOp = SysAllocString(L"ОК");
	 return S_OK;
 }

 
 STDMETHODIMP CMyBridgeX4Java::getServerStatus(BSTR* status)
 {

	 int res = 0;
	 if (m_out_ConnectSocket == INVALID_SOCKET)
	 { //Откроем канал сервера для получения статуса
		 std::string _OUT_PORT = DEFAULT_OUT_PORT;
		 if (m_OUT_PORT > 0)
			 _OUT_PORT = std::to_string(m_OUT_PORT);
		 res = create_channel(m_out_ConnectSocket, _OUT_PORT.c_str());
	 }
	 if (!res)
	 {
		 myCmd cmd("0", CMD_GET_STATUS, NO_PARAMS);
		 std::string resS;
		 res = send_out_channel(cmd.toString().c_str(), m_out_ConnectSocket, resS);//Получение статуса сервера по исходящему каналу
		 std::wstring answer;
		 if (!res)
		 {
			 answer = L"Server channel answer:";
			 std::wstring_convert<std::codecvt_utf8_utf16 <wchar_t> > converter;
			 answer = answer + converter.from_bytes(resS.c_str());
		 }
		 else
		 {
			 answer = L"Error: Command answer with error:";
			 answer= answer+std::to_wstring(res);
		 }
		 *status = SysAllocString(answer.c_str());
	 }
	 else
		 *status = SysAllocString(L"Error: No answer from server");

	 return S_OK;
 } //Получить статус сервера


 

 STDMETHODIMP CMyBridgeX4Java::stopBridge(int mode)
 {
	 if (mode == -1)
	 {
		 std::string _OUT_PORT = DEFAULT_OUT_PORT;
		 if (m_OUT_PORT > 0)
			 _OUT_PORT = std::to_string(m_OUT_PORT);

		 int res = 0;
		 if (m_out_ConnectSocket == INVALID_SOCKET)
		 { //Откроем канал что бы остановить сервер
				res = create_channel(m_out_ConnectSocket, _OUT_PORT.c_str());
		 }
		 if (!res)
		 {
			 myCmd cmd("0", CMD_STOP_SERVER, NO_PARAMS);
			 std::string resS;
			 res = send_out_channel(cmd.toString().c_str(), m_out_ConnectSocket, resS);//Остановка сервера
		 }
	 }
	 
	 if (m_getEventThread != NULL)
	 {
		 m_break = true;
		 if (m_getEventThread->joinable())
			m_getEventThread->join(); //

		 delete m_getEventThread;
		 m_getEventThread = NULL;
		 stopChannels();
		 m_hWnd4Embed = 0;
		 m_break = false;
	 }
	 return S_OK;
 }


 STDMETHODIMP CMyBridgeX4Java::startBridgeEx(BSTR SysName, BSTR* status)
 {
	 if (m_getEventThread != NULL)
		 return S_OK;
	 
	 //std::wstring w_sysName();
	 m_sysName = (std::wstring)SysName;
	 map_mutex.lock();
	 
	 int currentOutSocket = std::stoi(DEFAULT_OUT_PORT);
	 currentOutSocket += 10;
	 if (sysName2Socket.find(m_sysName) != sysName2Socket.end())
	 {
		 {//Сервер стартован соединяемся
			 CConnectDesc& desc = sysName2Socket[m_sysName];
			 m_break = false;
			 m_hWnd4Embed = 0;

			 m_OUT_PORT = desc.numOutSocket;
			 m_IN_PORT = desc.numOutSocket + DEF_DELTA_SOKET;

			 std::string _OUT_PORT = std::to_string(m_OUT_PORT);
			 std::string _IN_PORT = std::to_string(m_IN_PORT);
			 
			 desc.cntSystem++;
			 int res = initChannels(_OUT_PORT, _IN_PORT);
			 if (!res)
			 {
				 m_getEventThread = new std::thread(thunk2, this, &CMyBridgeX4Java::fireOutPutThread);
				 *status = SysAllocString(DEF_OK_STATUS);
			 }
			 else
			 { //Считаем, что сервер не запущен,попытаемся запустить сервер
				 m_urlServer = m_urlServer + L" -socketOut" + std::to_wstring(m_OUT_PORT) + L" -socketIn" + std::to_wstring(m_IN_PORT);
				 std::wstring l_wstatus = _createJavaServer(_OUT_PORT, _IN_PORT);
				 *status = SysAllocString(l_wstatus.c_str());
			 }
		 }
	 }
	 else
	 { //Ищем 
		 for (;;currentOutSocket += 5)
		 {
			 auto it = sysName2Socket.begin();
			 for (; it != sysName2Socket.end(); ++it)
			 {
				 if (it->second.numOutSocket == currentOutSocket)
					 break;
			 }
			 if (it == sysName2Socket.end()) //Не найдено можно использовать текущий номер сокета
			 {
				 CConnectDesc desc;
				 desc.cntSystem = 1;
				 desc.numOutSocket = currentOutSocket;

				 sysName2Socket[SysName] = desc;
				 //Стартуем сервер
				 m_OUT_PORT= currentOutSocket;
				 m_IN_PORT = currentOutSocket + DEF_DELTA_SOKET;

				 std::string _OUT_PORT = std::to_string(m_OUT_PORT);
				 std::string _IN_PORT = std::to_string(m_IN_PORT);

				 m_break = false;
				 m_hWnd4Embed = 0;
				 int res = initChannels(_OUT_PORT, _IN_PORT);
				 if (!res)
				 {
					 m_getEventThread = new std::thread(thunk2, this, &CMyBridgeX4Java::fireOutPutThread);
					 *status = SysAllocString(DEF_OK_STATUS);
				 }
				 else
				 {
					 m_urlServer = m_urlServer + L" -socketOut" + std::to_wstring(m_OUT_PORT) + L" -socketIn" + std::to_wstring(m_IN_PORT);
					 std::wstring l_wstatus = _createJavaServer(_OUT_PORT, _IN_PORT);

					 if (l_wstatus != DEF_OK_STATUS)
						 sysName2Socket[SysName].cntSystem = 0;

					 *status = SysAllocString(l_wstatus.c_str());


				 }

				 break;
			 }
		 }
	 }
	 map_mutex.unlock();
	 
	 return S_OK;
 }

 STDMETHODIMP CMyBridgeX4Java::startBridge(BSTR* status)
 {
	 if (m_getEventThread == NULL)
	 {
		 m_break = false;
		 m_hWnd4Embed = 0;
		 int res = initChannels((std::string)DEFAULT_OUT_PORT, (std::string)DEFAULT_IN_PORT);
		 if (!res)
		 {
			 m_getEventThread = new std::thread(thunk2, this, &CMyBridgeX4Java::fireOutPutThread);
			 *status = SysAllocString(DEF_OK_STATUS);
		 }
		 else
		 { //Считаем, что сервер не запущен,попытаемся запустить сервер
			 std::wstring l_wstatus = _createJavaServer((std::string)DEFAULT_OUT_PORT, (std::string)DEFAULT_IN_PORT);
			 *status = SysAllocString(l_wstatus.c_str());
		 }
	 }
	 return S_OK;
 }


 STDMETHODIMP CMyBridgeX4Java::aboutLib(BSTR* status)
 {
	 if (status!=NULL)
		*status = SysAllocString(L"1.0");
	 return S_OK;
 }
	 

 //// "костыль", чтобы наш codecvt имел публичный деструктор,
 //// как того требует wstring_convert
 //template<class Facet>
 //struct deletable_facet : Facet
 //{
	// template<class ...Args>
	// deletable_facet(Args&& ...args) : Facet(std::forward<Args>(args)...) {}
	// ~deletable_facet() {}
 //};

 STDMETHODIMP CMyBridgeX4Java::getResult(BSTR* res)
 {
	 std::string str;
	 str=getOutPutThread(str);

	 //std::string buildTree("buildTree;");


	//std::wstring_convert<std::codecvt<wchar_t,char, std::mbstate_t> > converter;
	//std::codecvt_base<wchar_t, char, std::mbstate_t>;
	//using convert_type = std::codecvt<wchar_t, char, std::mbstate_t>;
	//std::wstring_convert<deletable_facet<convert_type>, wchar_t> converter;

	 std::wstring_convert<std::codecvt_utf8_utf16 <wchar_t> > converter;
	 std::wstring wstr = converter.from_bytes(str.c_str());
	 //std::wstring wstr(str.begin(), str.end()); //doesn't work
/*
//1
std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
std::string str = converter.to_bytes(L"Hello world");
std::wstring wstr = converter.from_bytes("Hello world");
//2
std::string str("Hello world!!!");
std::wstring wstr(str.begin(), str.end());
*/


	 *res = SysAllocString(wstr.c_str());
	  return S_OK;
 }

 
  
  
 void thunk2(CMyBridgeX4Java* refMy, FN fn)
 {
	 (refMy->*fn)();
 };



