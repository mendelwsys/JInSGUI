#pragma once

//#include <boost/thread/mutex.hpp>
//#include <boost/thread/thread.hpp>
#include <queue>
#include <mutex>
#include <chrono>
#include <thread>
#include <vector>
#include <sstream>
#include <string>

template<typename Data>
class concurrent_queue
{
private:
	std::queue<Data> the_queue;
	mutable std::mutex the_mutex;
public:
	void push(const Data& data)
	{
		the_mutex.lock();
		the_queue.push(data);
		the_mutex.unlock();
	}

	bool empty() const
	{
		the_mutex.lock();
		bool res=the_queue.empty();
		the_mutex.unlock();
		return res;
	}

	Data& front()
	{
		the_mutex.lock();
		Data& res=the_queue.front();
		the_mutex.unlock();
		return res;
	}

	Data const& front() const
	{
		the_mutex.lock();
		return the_queue.front();
	}


	void pop()
	{
		the_mutex.lock();
		the_queue.pop();
		the_mutex.unlock();
	}
};

#define PAR_DL  ";"
#define CMD_DL "%%"

#define CMD_TEST "CMD_TEST"
#define CMD_OK "CMD_OK"
#define CMD_SKIP "CMD_SKIP"
#define CMD_REQUEST "CMD_REQUEST" 
#define CMD_GET_STATUS "CMD_GET_STATUS"
#define NO_PARAMS "NO_PARAMS"
#define CMD_INIT_SESSIONS "CMD_INIT_SESSIONS"
#define CMD_STOP_SERVER "CMD_STOP_SERVER"


class myCmd
{
	std::string id;
	std::string cmd;
	std::string params;
public:
	myCmd() {}
	myCmd(const myCmd & cmd)
	{
		this->id = cmd.id;
		this->cmd = cmd.cmd;
		this->params = cmd.params;
	}

	myCmd(std::string& id,std::string& cmd, std::string& params)
	{
		this->id = id;
		this->cmd = cmd;
		this->params = params;
	}

	myCmd(const char* id, const char* cmd, const char* params)
	{
		this->id = id;
		this->cmd = cmd;
		this->params = params;
	}
	
	std::string& add2String(std::string& in)
	{
		in += cmd + PAR_DL + id + PAR_DL + params;
		return in;
	}

	std::string toString()
	{
		std::string in;
		in = cmd + PAR_DL + id + PAR_DL + params;
		return in;
	}


	std::string getCmd() const
	{
		return cmd;
	}

	//int getParamsLn() const
	//{
	//	return params.length();
	//}

	//int setDebugParams(const char* instr)
	//{
	//	if (params.length() > (30 * 1024))
	//	{
	//		params = instr;
	//		return 1;
	//	}
	//	return 0;
	//		
	//}
	std::string getParams() const
	{
		return params;
	}

	std::string getIdCmd()
	{
		return id;
	}

	void initByStringParam(std::string param)
	{
		cmd.clear();
		id.clear();
		params.clear();

		std::istringstream f(param);
		std::string s;
		int ix = 0;
		while (getline(f, s, ';')) 
		{
			//strings.push_back(s);
			switch (ix)
			{
				case 0:
					cmd = s;
					break;
				case 1:
					id = s;
					break;
				default:
					break;
			}
			ix++;
			if (ix > 1)
				break;
		}
		std::size_t pos = cmd.length() + 1 + id.length() + 1;
		//std::size_t pos = param.find(cmd + ";" + id + ";");
		if (pos < param.length())
			this->params = param.substr(pos);  //get from "live" to the end

		//std::string[] parPair = param.split(";");
		//this.cmd = parPair[0];
		//if (parPair.length>1)
		//	this.idCmd = parPair[1];
		//if (parPair.length>2)
		//	this.params = param.substring((parPair[0] + ";" + parPair[1] + ";").length());
		//if (idCmd == null || idCmd.length() == 0)
		//	this.idCmd = String.valueOf(l.getAndIncrement());
	}
};
