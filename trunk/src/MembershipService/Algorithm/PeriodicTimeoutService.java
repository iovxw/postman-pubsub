package MembershipService.Algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PeriodicTimeoutService {
	List<Runnable> tasks = new LinkedList<Runnable>();
	private static ScheduledThreadPoolExecutor myPool = new ScheduledThreadPoolExecutor(2);
	
	public void AddPeriodicTask(Runnable task,long period,TimeUnit t)
	{
		tasks.add(task);
		myPool.scheduleAtFixedRate(task, period, period, t);
	}
	public void AddOneShot(Runnable task,long period,TimeUnit t)
	{
		tasks.add(task);
		myPool.schedule(task, period, t);
	}
	public void RemoveTasks()
	{
		myPool.shutdown();
	}
}
