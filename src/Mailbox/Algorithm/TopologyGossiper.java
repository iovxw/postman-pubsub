package Mailbox.Algorithm;

import il.technion.ewolf.kbr.Node;

import java.util.Set;

import Client.PollManager;
import Multicast.Algorithm.ChordData;
import ProbobalisticLookup.Messages.LookupMessage;


public class TopologyGossiper implements Runnable{

	Mailbox myBox;
	PollManager poller;

	TopologyGossiper(Mailbox m)
	{
		myBox = m;
		poller = new PollManager();
	}

	private void Gossip()
	{
		try
		{
			// send lookup for all other mailboxes. 
			for (Integer topic : this.myBox.buckets.rawTopics) {
				LookupMessage m = new LookupMessage();
				m.sender = myBox.myKbr.getLocalNode();
				Set<Node> temp = myBox.TopologyData.getMatchingNodes(topic);
				for (Node node : temp) {
					m.hits.add(new ChordData(node));
				}
				
				m.alreadyVisited = temp;
				m.topic = topic;
				m.ClientName = Configuration.Configuration.MAILBOX_NAME_TAG;
				m.TTL = Configuration.Configuration.PUB_TTL+1;
				
				for(int i= 0; i<Configuration.Configuration.BOX_LOOKUP_TRIES;i++)
				{
					m.TTL = Configuration.Configuration.PUB_TTL+1;
					myBox.handleLookupMessage(myBox.myKbr.getLocalNode(), m);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	

	}

	@Override
	public void run() {
		if(myBox.debugIsOperating)
			Gossip();
	}
}

