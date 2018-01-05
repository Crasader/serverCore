package core.service.net.tcp;

import org.slf4j.Logger;

import core.common.ServiceThreadFactory;
import core.common.constant.Loggers;
import core.service.net.NettyServerService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public abstract class TcpServerService extends NettyServerService {

    private Logger logger = Loggers.serverLogger;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ServiceThreadFactory bossThreadNameFactory;
    private ServiceThreadFactory workerThreadNameFactory;
    private ChannelInitializer channelInitializer;

    private ChannelFuture serverChannelFuture;
    
    public TcpServerService(int serverPort,
    		String bossTreadName,
    		String workThreadName,
    		ChannelInitializer channelInitializer) {
		super(serverPort);
		// TODO Auto-generated constructor stub
        this.bossThreadNameFactory = new ServiceThreadFactory(bossTreadName);
        this.workerThreadNameFactory = new ServiceThreadFactory(workThreadName);
        this.channelInitializer = channelInitializer;
	}

	@Override
	public boolean startService() throws Exception {
		// TODO Auto-generated method stub
        boolean serviceFlag  = super.startService();
        bossGroup = new NioEventLoopGroup(1, bossThreadNameFactory);
        workerGroup = new NioEventLoopGroup(0, workerThreadNameFactory);
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap = serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 65536)
                    .childOption(ChannelOption.SO_SNDBUF, 65536)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))  // heap buf 's better
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(channelInitializer);

            serverChannelFuture = serverBootstrap.bind(serverPort).sync();

            //TODO这里会阻塞main线程，暂时先注释掉
//            serverChannelFuture.channel().closeFuture().sync();
            serverChannelFuture.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e) {
            logger.error(e.toString(), e);
            serviceFlag = false;
        }
        return serviceFlag;
	}

	@Override
	public boolean stopService() throws Exception {
		// TODO Auto-generated method stub
        boolean flag = super.stopService();
        if(bossGroup != null){
            bossGroup.shutdownGracefully();
        }
        if(workerGroup != null){
            workerGroup.shutdownGracefully();
        }
        return flag;
	}
}
