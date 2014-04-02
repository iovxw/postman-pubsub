package Mailbox.Messages;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import il.technion.ewolf.kbr.Node;
import Tools.Topic;



public class TopicRequest extends Topic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6315603423225792935L;
	public TopicRequest(int t) {
		super(t);
		
	}
	public TopicRequest(List<Integer> t) {
		super(t);
		
	}
	public TopicRequest setGossip(List<PublicationMessage> g)
	{
		this.gossip =g;
		return this;
	}
	public List<PublicationMessage> gossip;
	public int publicationNumber;
	public int topicID;
	public Set<Node> otherMailboxes;
	
}
