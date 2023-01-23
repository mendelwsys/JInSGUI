#pragma once

#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include "MyQueue.h"


// Need to link with Ws2_32.lib, Mswsock.lib, and Advapi32.lib
#pragma comment (lib, "Ws2_32.lib")
#pragma comment (lib, "Mswsock.lib")
#pragma comment (lib, "AdvApi32.lib")

#define DEFAULT_BUFLEN 512
//#define DEFAULT_BUFLEN 524288
#define DEFAULT_OUT_PORT "3333"
#define DEFAULT_IN_PORT "3433"

int create_channel(SOCKET& ConnectSocket, PCSTR port);
int send_out_channel(const char * sendbuf, SOCKET out_ConnectSocket, std::string& resCmd);
