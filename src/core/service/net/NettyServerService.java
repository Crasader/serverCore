package core.service.net;

import java.net.InetSocketAddress;

public abstract class NettyServerService extends ServerService {

    protected int serverPort;
    protected InetSocketAddress serverAddress;

    public NettyServerService(int serverPort) {
		// TODO Auto-generated constructor stub
        this.serverPort = serverPort;
        this.serverAddress = new InetSocketAddress(serverPort);
	}
}
