package Client;

import il.technion.ewolf.kbr.Node;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TopicSubscribeFail extends TopicSubscribeTask{


	
	

	public TopicSubscribeFail(ClientApplication app, int t)
	{
		super(app,t);
		
	}
	@Override
	public void run() {
		
		Set<Node> mailbox = null;
		try
		{
			
			mailbox = application.myPollManager.getMailboxesForTopic(this.topic);
			
		
		if(mailbox == null || mailbox.size() ==0)
		{
			
			// if I didn't find a mailbox, I ask the home node to become one.
			this.application.homeNode.Activate(topic);
			// now it is a new mailbox;
			this.application.homeNode.isDown.set(false);
			this.application.homeNode.Subscribe(topic,this.application.myPollManager.bm ,this.application.myPollManager.getPublications(topic));
			application.myPollManager.AddMailbox(topic, application.homeNode.myKbr.getLocalNode());
			System.out.println("local mailbox "+this.application.homeNode.myData.chordID+" subscribed to: "+ topic);
			
			
			//super.run();
			
		}
		else
		{
			
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
