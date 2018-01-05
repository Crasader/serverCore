package core.service.net;

import core.service.IServerService;

public abstract class ServerService implements IServerService {

	public ServerService() {
		// TODO Auto-generated constructor stub
	}

	public boolean initialize() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean startService() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean stopService() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public void release() {
		// TODO Auto-generated method stub

	}

	public byte getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

}
