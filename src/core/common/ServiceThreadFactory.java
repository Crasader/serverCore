package core.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceThreadFactory implements ThreadFactory {
    private ThreadGroup group;
    private AtomicInteger threadNumber = new AtomicInteger(0);
    private String namePrefix;
    private boolean daemon;

	public ServiceThreadFactory(String namePreFix) {
		// TODO Auto-generated constructor stub
		this(namePreFix, false);
	}

	public ServiceThreadFactory(String namePreFix, boolean daemon) {
		// TODO Auto-generated constructor stub
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
                .getThreadGroup();
        this.namePrefix = namePreFix + "-thread-";
        this.daemon = daemon;
	}

	public Thread newThread(Runnable r) {
		// TODO Auto-generated method stub
        Thread t = new Thread(group, r, namePrefix
                + threadNumber.getAndIncrement(), 0);
        if(daemon) {
           t.setDaemon(daemon);
        }else{
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
	}

    public String getNamePrefix() {
        return namePrefix;
    }
}
