package Client;

public class PollTask implements Runnable {

	private ClientApplication application;
	
	public PollTask(ClientApplication app)
	{
		application = app;
	}
	@Override
	public void run() {
		if(application.active.get())
		{
			application.myPollManager.PollRound();
		}
		
		
	}

}
