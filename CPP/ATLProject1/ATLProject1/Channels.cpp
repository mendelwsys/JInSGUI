// ConsoleApplication4.cpp : Defines the entry point for the console application.
//

//#define WIN32_LEAN_AND_MEAN

#include "stdafx.h"
#include "Channels.h"

//#include <winsock2.h>
//#include <ws2tcpip.h>
//#include <stdlib.h>
//#include <stdio.h>
//#include <string>
//#include "MyQueue.h"
//
//
//// Need to link with Ws2_32.lib, Mswsock.lib, and Advapi32.lib
//#pragma comment (lib, "Ws2_32.lib")
//#pragma comment (lib, "Mswsock.lib")
//#pragma comment (lib, "AdvApi32.lib")
//
//
//#define DEFAULT_BUFLEN 512
//#define DEFAULT_OUT_PORT "3333"
//#define DEFAULT_IN_PORT "3433"
//
//int create_channel(SOCKET& ConnectSocket, PCSTR port);
//int send_out_channel(const char * sendbuf, SOCKET out_ConnectSocket, std::string& resCmd);
//int get_from_channel(const char * sendbuf, SOCKET in_ConnectSocket, std::string& resCmd);

//int test1();



//int test1()
//{
//
//	//const char *sendbuf = u8"<CMD>\n\
//		//		<ON>ON_NAME</ON>\n\
//	//	<F>F_NAME</F>\n\
//	//	<P N='Имя параметра1' ENN='false' ENV='false' PAC='false'>Значение1</P>\n\
//	//	<P N='Имя параметра2' ENN='false' ENV='false' PAC='false'>Значение2</P>\n\
//	//	</CMD>\n";
//	SOCKET out_ConnectSocket = INVALID_SOCKET;
//	SOCKET in_ConnectSocket = INVALID_SOCKET;
//	int res = create_channel(out_ConnectSocket, DEFAULT_OUT_PORT);
//	if (!res)
//	{
//		res = create_channel(in_ConnectSocket, DEFAULT_IN_PORT);
//		if (!res)
//		{
//			std::thread m_onTestThread(test1Getter, in_ConnectSocket);
//			for (int i = 0;; i++)
//			{
//				std::string id = std::to_string(i), sCmd, resS;
//				myCmd cmd(id.c_str(), "TEST", "NO PARAMS"), resCmd;
//				sCmd = cmd.add2String(sCmd);
//				int res = send_out_channel(sCmd.c_str(), out_ConnectSocket, resS);
//				if (res < 0)
//					break;
//				//				printf("GOT answer for output cmd %s \n", resS.c_str());
//
//				resCmd.initByStringParam(resS);
//				std::this_thread::sleep_for(std::chrono::milliseconds(3000));
//
//			}
//
//		}
//	}
//
//
//	return 0;
//}


int create_channel(SOCKET& ConnectSocket, PCSTR port)
{

	WSADATA wsaData;
	struct addrinfo *result = NULL,
		*ptr = NULL,
		hints;

	// Initialize Winsock
	int iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed with error: %d\n", iResult);
		return 1;
	}

	ZeroMemory(&hints, sizeof(hints));
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_protocol = IPPROTO_TCP;

	// Resolve the server address and port
	iResult = getaddrinfo("127.0.0.1", port, &hints, &result);
	if (iResult != 0) {
		printf("getaddrinfo failed with error: %d\n", iResult);
		WSACleanup();
		return 1;
	}


	// Attempt to connect to an address until one succeeds
	for (ptr = result; ptr != NULL; ptr = ptr->ai_next)
	{

		// Create a SOCKET for connecting to server
		ConnectSocket = socket(ptr->ai_family, ptr->ai_socktype,
			ptr->ai_protocol);
		if (ConnectSocket == INVALID_SOCKET)
		{
			printf("socket failed with error: %ld\n", WSAGetLastError());
			WSACleanup();
			return 1;
		}

		// Connect to server.
		iResult = connect(ConnectSocket, ptr->ai_addr, (int)ptr->ai_addrlen);
		if (iResult == SOCKET_ERROR) {
			closesocket(ConnectSocket);
			ConnectSocket = INVALID_SOCKET;
			continue;
		}
		break;
	}
	freeaddrinfo(result);


	if (ConnectSocket == INVALID_SOCKET) {
		printf("Unable to connect to server!\n");
		WSACleanup();
		return 1;
	}
	return 0;
}

int send_to_channel(const char* sendbuf, SOCKET ConnectSocket)
{

	// Send an initial buffer
	long ln = strlen(sendbuf);
	std::string sendIt = std::to_string(ln);
	sendIt += "\n";
	sendIt += sendbuf;

	int iResult = send(ConnectSocket, sendIt.c_str(), (int)strlen(sendIt.c_str()), 0);
	
	if (iResult == SOCKET_ERROR) {
		printf("send failed with error: %d\n", WSAGetLastError());
		closesocket(ConnectSocket);
		WSACleanup();
		return 1;
	}
	//	printf("PUT CMD ==\n %s \n == Send %ld bytes \n", sendIt.c_str(), iResult);
	//// shutdown the connection since no more data will be sent
	//iResult = shutdown(ConnectSocket, SD_SEND);
	//if (iResult == SOCKET_ERROR) {
	//	printf("shutdown failed with error: %d\n", WSAGetLastError());
	//	closesocket(ConnectSocket);
	//	WSACleanup();
	//	return 1;
	//}



	return 0;
}
int recive_from_channel(std::string& res, SOCKET ConnectSocket)
{
	char recvbuf[DEFAULT_BUFLEN];
	int recvbuflen = DEFAULT_BUFLEN;

	// Receive until the peer closes the connection
	long ln = -1l;
	int iResult;

	std::size_t found = std::string::npos;
	do
	{
		int sepLn = strlen("\n");
		iResult = recv(ConnectSocket, recvbuf, recvbuflen - 1, 0);
		
		if (iResult > 0)
		{
			//printf("Bytes received: %d\n", iResult);
			recvbuf[iResult] = 0;
			res += recvbuf;
			if (ln<0)
			{
				found = res.find("\n");
				if (found != std::string::npos)
				{
					const std::string sln = res.substr(0, found);
					ln = std::stoi(sln);
				}
			}

			if (ln >= 0)
			{
				int rsv = res.length() - found - sepLn;
				if (rsv >= ln)
				{
					//					printf("GOT CMD ==\n %s \n == \n", res.c_str());
					res = res.substr(found + sepLn, ln);
					break;
				}
			}
		}
		else if (iResult == 0)
		{
			printf("Connection closed\n");
			//		iResult = shutdown(ConnectSocket, SD_SEND);
			//			if (iResult == SOCKET_ERROR) 
			{
				// cleanup
				closesocket(ConnectSocket);
				WSACleanup();
			}
			return -2;
		}
		else
		{
			printf("recv failed with error: %d\n", WSAGetLastError());
			return -1;
		}
	} while (iResult > 0);

	return 0;
}


int send_out_channel(const char * sendbuf, SOCKET out_ConnectSocket, std::string& resCmd)
{
	int res = send_to_channel(sendbuf, out_ConnectSocket);
	if (!res)
		return recive_from_channel(resCmd, out_ConnectSocket);
	return res;
}
