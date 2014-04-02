package ProbobalisticLookup.Messages;

import il.technion.ewolf.kbr.Node;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import Mailbox.Messages.PublicationMessage;
import Multicast.Algorithm.ChordData;
import Multicast.Messages.MessageContainer;
import Multicast.Messages.MessageType;

public class LookupMessage implements Serializable, MessageContainer {

	private static final long serialVersionUID = 202587047617618585L;
	public Integer TTL;
	public Integer topic;
	public Node sender;
	public Set<Node> alreadyVisited;
	public Set<ChordData> hits;
	public String ClientName;
	public PublicationMessage Content;
	public LookupMessage()
	{
		alreadyVisited = new HashSet<Node>();
		hits = new HashSet<ChordData>();
		TTL = Configuration.Configuration.PUB_TTL;
		
	}
	@Override
	public Object GetMessage() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public int GetMessageType() {
		// TODO Auto-generated method stub
		
			return MessageType.PLS_Lookup;
		
	}

}
