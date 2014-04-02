package Client;

import ProbobalisticLookup.Messages.LookupMessage;

public class TopicSubscribeTask implements Runnable{
	
	protected ClientApplication application;
	protected int topic;
	
	public TopicSubscribeTask(ClientApplication app, int t)
	{
		application = app;
		topic = t;
	}
	
	@Override
	public void run() {
		
		
		LookupMessage m = new LookupMessage();
		m.ClientName = application.myname;
		m.topic = this.topic;
		m.Content = null;
		m.sender = application.homeNode.myKbr.getLocalNode();
		//m.alreadyVisited = application.myPollManager.mailboxes.get(this.topic);
		
		for(int i =0; i< Configuration.Configuration.CLIENT_JOIN_TRIES;i++)
		{
			m.TTL = Configuration.Configuration.PUB_TTL +1;
			application.homeNode.handleLookupMessage(application.homeNode.myData.eWolfNode, m);
		}
	}

}
