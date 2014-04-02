package Mailbox.Messages;
import il.technion.ewolf.kbr.Node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PollReplayMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3267525014074801924L;
	
	public HashMap <Integer,List<PublicationMessage>> Content;
	// hich hikers !
	public HashMap <Integer,Set<Node>> otherMailboxes;
	public Set<Integer> otherTopics;
public  PollReplayMessage()
{
	Content = new HashMap <Integer,List<PublicationMessage>>();
	otherMailboxes = new HashMap<Integer,Set<Node>>();
	otherTopics = new HashSet<Integer> ();
}
	public void addContent(List<PublicationMessage> getPublicationMessages, Integer topicId) {
		Content.put(topicId, getPublicationMessages);
	}
	public void addNodes(Set<Node> nodes, Integer trId) {
		otherMailboxes.put(trId, nodes);
	}


}
