package Mailbox.Messages;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Multicast.Messages.MessageContainer;
import Multicast.Messages.MessageType;





public class PublicationMessage implements Serializable, Comparable<PublicationMessage>, MessageContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String content;
	@Override
	public int hashCode()
	{
		return content.hashCode();
	}
	public List<Integer> topics;
	
	public PublicationMessage()
	{
		topics = new ArrayList<Integer>();
	}
	@Override
	public int compareTo(PublicationMessage o) {
		return content.compareTo(o.content);
		//return 0;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PublicationMessage)
		{
			return this.content.equals(((PublicationMessage) obj).content);
		}
		return false;
	}
	@Override
	public Object GetMessage() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public int GetMessageType() {
		// TODO Auto-generated method stub
		return MessageType.Publication;
	}
}
