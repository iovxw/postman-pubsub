package MembershipService.Messages;

import java.io.Serializable;

import Multicast.Messages.MessageContainer;
import Multicast.Messages.MessageType;

public class TopicTopologyGossipContainer implements MessageContainer,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TopicTopologyGossipMessage m;
	@Override
	public int GetMessageType() {
		 return MessageType.TopologyGossipMessage;
	}

	@Override
	public Object GetMessage() {
		return m;
	}
	public TopicTopologyGossipContainer(TopicTopologyGossipMessage message) {
		m = message;
	}
	
}
