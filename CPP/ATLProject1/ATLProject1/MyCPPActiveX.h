// MyBridgeX4Java.h : Declaration of the CMyBridgeX4Java
#pragma once
#include "resource.h"       // main symbols
#include <atlctl.h>
#include "ATLProject1_i.h"
#include <stdlib.h>
#include <string>
#include <map>
#include "_IMyCPPActiveXEvents_CP.H"
#include "Channels.h"

#if defined(_WIN32_WCE) && !defined(_CE_DCOM) && !defined(_CE_ALLOW_SINGLE_THREADED_OBJECTS_IN_MTA)
#error "Single-threaded COM objects are not properly supported on Windows CE platform, such as the Windows Mobile platforms that do not include full DCOM support. Define _CE_ALLOW_SINGLE_THREADED_OBJECTS_IN_MTA to force ATL to support creating single-thread COM object's and allow use of it's single-threaded COM object implementations. The threading model in your rgs file was set to 'Free' as that is the only threading model supported in non DCOM Windows CE platforms."
#endif


using namespace ATL;


//int initJVM();
//int setFrameSize(int width, int height);
//int callJFrame(HWND& hanler);
//int ReleaseVM();

#define DEF_JAVA_CMD L"java.exe"
#define DEF_JAVA_OPTION L""
#define DEF_OK_STATUS L"OK"
#define DEF_DELTA_SOKET 300

struct CConnectDesc //Описатель соединения для каждой системы.
{
	int numOutSocket;
	int cntSystem;
};

// CMyBridgeX4Java
class ATL_NO_VTABLE CMyBridgeX4Java :
	public CComObjectRootEx<CComSingleThreadModel>,
public IOleControlImpl<CMyBridgeX4Java>,
public IOleObjectImpl<CMyBridgeX4Java>,
public IOleInPlaceActiveObjectImpl<CMyBridgeX4Java>,
public IViewObjectExImpl<CMyBridgeX4Java>,
public IOleInPlaceObjectWindowlessImpl<CMyBridgeX4Java>,
public ISupportErrorInfo,
public IConnectionPointContainerImpl<CMyBridgeX4Java>,
public CProxy_IMyBridgeX4JavaEvents<CMyBridgeX4Java>,
public IQuickActivateImpl<CMyBridgeX4Java>,
#ifndef _WIN32_WCE
public IDataObjectImpl<CMyBridgeX4Java>,
#endif
public IProvideClassInfo2Impl<&CLSID_MyBridgeX4Java, &__uuidof(_IMyBridgeX4JavaEvents), &LIBID_ATLProject1Lib>,
public CComCoClass<CMyBridgeX4Java, &CLSID_MyBridgeX4Java>,
public CComControl<CMyBridgeX4Java>,
public IDispatchImpl<_IMyBridgeX4JavaEvents, &__uuidof(_IMyBridgeX4JavaEvents), &LIBID_ATLProject1Lib, /* wMajor = */ 1, /* wMinor = */ 0>,
public IDispatchImpl<IMyBridgeX4Java, &__uuidof(IMyBridgeX4Java), &LIBID_ATLProject1Lib, /* wMajor = */ 1, /* wMinor = */ 0>
{

public:


	CMyBridgeX4Java()
	{
		//m_retStat = initJVM();
		m_bWindowOnly = true;
	}

	DECLARE_OLEMISC_STATUS(OLEMISC_RECOMPOSEONRESIZE |
	OLEMISC_CANTLINKINSIDE |
		OLEMISC_INSIDEOUT |
		OLEMISC_ACTIVATEWHENVISIBLE |
		OLEMISC_SETCLIENTSITEFIRST
		)

		DECLARE_REGISTRY_RESOURCEID(IDR_MyBridgeX4Java)


	BEGIN_COM_MAP(CMyBridgeX4Java)
		COM_INTERFACE_ENTRY(IMyBridgeX4Java)
		COM_INTERFACE_ENTRY2(IDispatch,IMyBridgeX4Java)
		COM_INTERFACE_ENTRY2(IDispatch, _IMyBridgeX4JavaEvents)
		COM_INTERFACE_ENTRY(IViewObjectEx)
		COM_INTERFACE_ENTRY(IViewObject2)
		COM_INTERFACE_ENTRY(IViewObject)
		COM_INTERFACE_ENTRY(IOleInPlaceObjectWindowless)
		COM_INTERFACE_ENTRY(IOleInPlaceObject)
		COM_INTERFACE_ENTRY2(IOleWindow, IOleInPlaceObjectWindowless)
		COM_INTERFACE_ENTRY(IOleInPlaceActiveObject)
		COM_INTERFACE_ENTRY(IOleControl)
		COM_INTERFACE_ENTRY(IOleObject)
		COM_INTERFACE_ENTRY(ISupportErrorInfo)
		COM_INTERFACE_ENTRY(IConnectionPointContainer)
		COM_INTERFACE_ENTRY(IQuickActivate)
#ifndef _WIN32_WCE
		COM_INTERFACE_ENTRY(IDataObject)
#endif
		COM_INTERFACE_ENTRY(IProvideClassInfo)
		COM_INTERFACE_ENTRY(IProvideClassInfo2)
		COM_INTERFACE_ENTRY(_IMyBridgeX4JavaEvents)

		
	END_COM_MAP()

	BEGIN_PROP_MAP(CMyBridgeX4Java)
		PROP_DATA_ENTRY("_cx", m_sizeExtent.cx, VT_UI4)
		PROP_DATA_ENTRY("_cy", m_sizeExtent.cy, VT_UI4)
		// Example entries
		// PROP_ENTRY_TYPE("Property Name", dispid, clsid, vtType)
		// PROP_PAGE(CLSID_StockColorPage)
	END_PROP_MAP()


	BEGIN_MSG_MAP(CMyBridgeX4Java)
		MESSAGE_HANDLER(WM_SIZE, OnSize)
		CHAIN_MSG_MAP(CComControl<CMyBridgeX4Java>)
		DEFAULT_REFLECTION_HANDLER()
		MESSAGE_HANDLER(WM_LBUTTONDOWN, OnLButtonDown)
		MESSAGE_HANDLER(WM_COMMAND, OnCommand)
	END_MSG_MAP()
	// Handler prototypes:
	//  LRESULT MessageHandler(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	//	LRESULT CommandHandler(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);
	//  LRESULT NotifyHandler(int idCtrl, LPNMHDR pnmh, BOOL& bHandled);

	// ISupportsErrorInfo
	STDMETHOD(InterfaceSupportsErrorInfo)(REFIID riid)
	{
		static const IID* const arr[] =
		{
			//&IID_IMyBridgeX4Java,
			&__uuidof(IMyBridgeX4Java)
		};

		for (int i = 0; i < sizeof(arr) / sizeof(arr[0]); i++)
		{
			if (InlineIsEqualGUID(*arr[i], riid))
				return S_OK;
		}
		return S_FALSE;
	}

	// IViewObjectEx
	DECLARE_VIEW_STATUS(VIEWSTATUS_SOLIDBKGND | VIEWSTATUS_OPAQUE)

	

//Образец работы с потоками stl									   
    //std::thread first(foo);     // spawn new thread that calls foo()
	//std::thread second(bar, 0);  // spawn new thread that calls bar(0)
	//std::cout << "main, foo and bar now execute concurrently...\n";
	//// synchronize threads:
	//first.join();                // pauses until first finishes
	//second.join();               // pauses until second finishes
	//std::cout << "foo and bar completed.\n";
	//boolean m_bWasInitIt = false;

private:

	HWND javaChild = 0;
	int m_retStat = 0;
	HWND m_hWnd4Embed = 0;


	mutable concurrent_queue<myCmd> m_outQueue;//Очередь исходящих сообщений
	mutable boolean m_break = false;



	std::thread* m_getEventThread = NULL;     //Поток обслуживает fireOutPutThread
	void fireOutPutThread();//Поток сообщает GUI о пришедшей команде в выходной поток.
	std::string m_sessionId;//Идентификатор сессии

	SOCKET m_out_ConnectSocket = INVALID_SOCKET;
	SOCKET m_in_ConnectSocket = INVALID_SOCKET;
	std::thread* m_onGetThread = NULL;//поток обслуживает  serverGetter()
	void serverGetter(); //Получает команды от сервера собирает их и передает в очередь m_outQueue
	std::string& getOutPutThread(std::string& in);
	int initChannels(std::string& out_port, std::string& in_port);//Инициализировать какналы передачи данных (Сокеты и запустить поток получения данных в очередь m_outQueue)
	void stopChannels();//Остановить каналы и отсоединится от сервера
	void EmbedWindow(HWND hWnd4Embed);//Встроить окно процесса в окно компонента
	std::wstring _createJavaServer(std::string& out_port, std::string& in_port);//Создать процесс java сервера

	static int cntObjects;
	static std::mutex cnt_mutex;


	static std::map<std::wstring, CConnectDesc> sysName2Socket;
	static std::mutex map_mutex;

	int m_OUT_PORT = -1;
	int m_IN_PORT = -1;
	std::wstring m_sysName;


public:

	mutable int m_id = 0;
	myCmd createTestCmd()
	{
		m_id++;
		std::string id=std::to_string(m_id);
		myCmd cmd("TST",id.c_str(),u8"Мои параметры");
		return cmd;
	}


	//HRESULT OnPosRectChange(
	//	LPCRECT lprcPosRect
	//)
	//{
	//	return S_OK;
	//}

	HRESULT OnDraw(ATL_DRAWINFO& di)
	{


		//		if (m_hWnd != NULL)
		//		{
		////			EmbedCalc(m_hWnd);
		//			m_bWasInitIt = TRUE;
		//			return S_OK;
		//		}
		//		else
		//			if (m_bWasInitIt)
		//			{
		//				RECT& rc = *(RECT*)di.prcBounds;
		//				int w = rc.right - rc.left;
		//				int h = rc.bottom - rc.top;
		//				if (w >= 0 && h >= 0)
		//					setFrameSize(w, h);
		//				return S_OK;
		//			}


				//if (!javaChild && m_hWnd!=NULL &&  (retStat=callJFrame(javaChild))==0)
				//{
				//	::SetParent(javaChild, m_hWnd); //a will be the new parent b
				//	DWORD style = ::GetWindowLong(javaChild, GWL_STYLE); //get the b style
				//	style &= ~(WS_SIZEBOX | WS_POPUP | WS_CAPTION); //reset the "caption" and "popup" bits
				//	style |= WS_CHILD; //set the "child" bit
				//	::SetWindowLong(javaChild, GWL_STYLE, style); //set the new style of b
				//	RECT rc; //temporary rectangle
				//	GetClientRect(&rc); //the "inside border" rectangle for a
				//	::MoveWindow(javaChild, rc.left, rc.top, rc.right - rc.left, rc.bottom - rc.top, false); //place b at (x,y,w,h) in a
				//	m_bWasInitIt = TRUE;
				//	return S_OK;
				//}
				//else 
				//	if (m_bWasInitIt)
				//{
				//	RECT& rc = *(RECT*)di.prcBounds;
				//	int w = rc.right - rc.left;
				//	int h = rc.bottom - rc.top;
				//	if (w >= 0 && h >= 0)
				//		setFrameSize(w, h);
				//	return S_OK;
				//}


		RECT& rc = *(RECT*)di.prcBounds;
		// Set Clip region to the rectangle specified by di.prcBounds
		HRGN hRgnOld = NULL;
		if (GetClipRgn(di.hdcDraw, hRgnOld) != 1)
			hRgnOld = NULL;
		bool bSelectOldRgn = false;

		HRGN hRgnNew = CreateRectRgn(rc.left, rc.top, rc.right, rc.bottom);

		if (hRgnNew != NULL)
		{
			bSelectOldRgn = (SelectClipRgn(di.hdcDraw, hRgnNew) != ERROR);
		}

		Rectangle(di.hdcDraw, rc.left, rc.top, rc.right, rc.bottom);
		SetTextAlign(di.hdcDraw, TA_CENTER | TA_BASELINE);

		//wchar_t d[100];
		//_itow_s(m_retStat, d, 30, 10);
		//wcscat_s(d, 50, _T("_ActiveXBridge"));
		//LPCTSTR pszText = d;

		LPCTSTR pszText = _T("ActiveXBridge for Java ver 1.0");



#ifndef _WIN32_WCE
		TextOut(di.hdcDraw,
			(rc.left + rc.right) / 2,
			(rc.top + rc.bottom) / 2,
			pszText,
			lstrlen(pszText));
#else
		ExtTextOut(di.hdcDraw,
			(rc.left + rc.right) / 2,
			(rc.top + rc.bottom) / 2,
			ETO_OPAQUE,
			NULL,
			pszText,
			ATL::lstrlen(pszText),
			NULL);
#endif

		if (bSelectOldRgn)
			SelectClipRgn(di.hdcDraw, hRgnOld);

		DeleteObject(hRgnNew);

		return S_OK;
	}


	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct()
	{

		cnt_mutex.lock();
			cntObjects++;
		cnt_mutex.unlock();
		return S_OK;
	}

	void FinalRelease()
	{
		if (m_OUT_PORT < 0 || m_IN_PORT < 0)
		{
			FinalRelease_OLD();
		}
		else
		{
			map_mutex.lock();
			//Находим себя в map по имени системы.
			if (sysName2Socket.find(m_sysName) != sysName2Socket.end())
			{
				int mode = 0;//Режим остановки
				CConnectDesc& desc = sysName2Socket[m_sysName];
				desc.cntSystem--;//Уменьшаем кол-во систем
				if (desc.cntSystem <= 0)
				{ //Удалим систему если больше нет соединений к системе
					sysName2Socket.erase(m_sysName);
					mode = -1;
				}
				stopBridge(mode);
			}
			map_mutex.unlock();

			cnt_mutex.lock();
				cntObjects--;
			cnt_mutex.unlock();
		}

	}


	void FinalRelease_OLD()
	{
		int l_cntObjects;
		cnt_mutex.lock();
		cntObjects--;
		l_cntObjects = cntObjects;
		if (cntObjects <= 0)
			stopBridge(-1);
		cnt_mutex.unlock();
		if (l_cntObjects>0)
			stopBridge(0);

	}

	LRESULT OnCommand(UINT /*uMsg*/, WPARAM /*wParam*/, LPARAM /*lParam*/, BOOL& /*bHandled*/);
	LRESULT OnSize(UINT /*uMsg*/, WPARAM /*wParam*/, LPARAM /*lParam*/, BOOL& /*bHandled*/);

	LRESULT OnLButtonDown(UINT /*uMsg*/, WPARAM /*wParam*/, LPARAM /*lParam*/, BOOL& /*bHandled*/);
	BEGIN_CONNECTION_POINT_MAP(CMyBridgeX4Java)
		CONNECTION_POINT_ENTRY(__uuidof(_IMyBridgeX4JavaEvents))
	END_CONNECTION_POINT_MAP()

	// _IMyBridgeX4JavaEvents Methods
public:
	STDMETHOD_(void, RiseEvent)()
	{
		// Add your function implementation here.

	}
	STDMETHOD_(void, RiseEvent2)()
	{
		// Add your function implementation here.

	}

	STDMETHOD_(void, MyEv)()
	{
		// Add your function implementation here.

	}

	STDMETHOD(sendCmd)(BSTR p1,BSTR* res);
	STDMETHOD(stopBridge)(int mode);
	STDMETHOD(startBridge)(BSTR* status);
	STDMETHOD(getResult)(BSTR* res);
	STDMETHOD(getServerStatus)(BSTR* status);

	//STDMETHOD(stopBridgeEx)(BSTR SysName,int mode);
	STDMETHOD(startBridgeEx)(BSTR SysName, BSTR* status);
	
	STDMETHOD(aboutLib)(BSTR* status);



	std::wstring m_urlServer, m_joption = DEF_JAVA_OPTION,m_urlJava = DEF_JAVA_CMD;

//	std::wstring m_cmd;

	STDMETHOD(setDefJOption)(BSTR joption, int* res)
	{
		m_joption = joption;
		if (res != NULL)
			*res = 0;
		return S_OK;
	}//Установить опции Java при запуске сервера ( Запускается без опции)
	STDMETHOD(setJarServerUrl)(BSTR urlServer, int* res) {
		m_urlServer = urlServer;
		if (res != NULL)
			*res = 0;
		return S_OK;
	}//Установить путь к jar где выполняетсся сервер.
	STDMETHOD(setJavaCmd)(BSTR urlJava, int* res) {
		m_urlJava = urlJava;
		if (res!=NULL)
			*res = 0;
		return S_OK;
	}//Установить путь к java.

	std::wstring m_appName;

	STDMETHOD(setAppName)(BSTR appName, int* res)
	{
		m_appName = appName;
		if (res != NULL)
			*res = 0;
		return S_OK;
	} //Отдать имя процесса при запуске

	 STDMETHOD(getAppName)(BSTR* msg, BSTR* cmd)
	 {
		 *cmd = SysAllocString(m_appName.c_str());
		 if (m_appName.length()==0)
			*msg = SysAllocString(L"Default empty cmd");
		 else
			 *msg = SysAllocString(L"External set Command");
		 return S_OK;
	 } //Отдать имя процесса при запуске


	 //STDMETHOD(getServerStatus)(BSTR* status)
	 //{
		// //if (res != NULL)
		//	// *res = 0;
		// return S_OK;
	 //} //Отдать имя процесса при запуске

	 //STDMETHOD(setFullCmd)(BSTR cmd) 
	//{
	//	if (cmd == NULL)
	//		cmd = L"";
	//	m_cmd = cmd;
	//	return S_OK;
	//} //Если команада установлена ( не пуста и не null), выполняется команда, остальные опции игнорируются

	std::wstring _getFullCmdLine(std::wstring* msg = NULL)
	{
		//std::wstring cmd;
		//if (m_cmd != L"")
		//{
		//	cmd = SysAllocString(m_cmd.c_str());
		//	if (msg!=NULL)
		//		(*msg) = L"Cmd was overwrite by call setFullCmd, to reset to default cmd line call setFullCmd with empty parameter \"\" ";
		//}
		//else
		//{
		//	cmd = SysAllocString((m_urlJava + L" " + m_joption + L" -jar " + m_urlServer).c_str());
		//	if (msg != NULL)
		//		(*msg) = SysAllocString(L"Default cmd");
		//}

		std::wstring l_cmd;
		l_cmd = SysAllocString((m_urlJava + L" " + m_joption + L" " + m_urlServer).c_str());
		if (msg != NULL)
			(*msg) = SysAllocString(L"Default cmd");
		return l_cmd;
	}
	

	STDMETHOD(getFullCmd)(BSTR* msg, BSTR* cmd)
	{
		std::wstring l_msg;
		std::wstring l_cmd = _getFullCmdLine(&l_msg);
		*cmd = SysAllocString(l_cmd.c_str());
		*msg = SysAllocString(l_msg.c_str());
		return S_OK;
	}//Получить команду которая будет исполнена с пояснениями.


};



OBJECT_ENTRY_AUTO(__uuidof(MyBridgeX4Java), CMyBridgeX4Java)

typedef void (CMyBridgeX4Java::*FN)();

void thunk2(CMyBridgeX4Java*,FN fn);