package Client.MultipleTopicContainer;

import il.technion.ewolf.kbr.Node;

import java.util.HashSet;
import java.util.Set;

import Multicast.Algorithm.ChordData;
import Tools.Topic;

public class MailboxInformation {
	private Node node;
	private Set<Integer> topics;

	public Node getNode() {
		return node;
	}

	public MailboxInformation setMailbox(Node nd) {
		this.node = nd;
		topics = new HashSet<Integer>();
		return this;
	}
	
	public boolean isInterested(Topic t)
	{
		return topics.containsAll(t.getMyTopic());
	}
	public boolean isInterested(Integer t)
	{
		return topics.contains(t);
	}
	
	
	public boolean AddTopic(Topic t)
	{
		return topics.addAll(t.getMyTopic());
	}
	public boolean AddTopic(Integer t)
	{
		return topics.add(t);
	}
	 
	@Override
	public boolean equals(Object o)
	{
		MailboxInformation mbi;
		if(o instanceof MailboxInformation )
		{
			mbi = (MailboxInformation)o;
			return this.node.equals(mbi.node);
		}
		//To use directly the chordData;
		return this.node.equals(o);
	}
	public int hashCode()
	{
		return this.node.hashCode();
	}
}
