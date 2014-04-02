package MembershipService.Messages;

import java.io.Serializable;
import java.util.HashSet;

import Multicast.Algorithm.ChangeType;
import Multicast.Algorithm.ChordData;
import Multicast.Messages.MessageContainer;
import Multicast.Messages.MessageType;

public class TopologyContainer implements MessageContainer, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TopologyMessage content;
	public TopologyContainer(int join,ChordData d, Integer t, HashSet<Integer> myOtherTopics)
	{
		content = new TopologyMessage(join, d, t,myOtherTopics);
	}
	public TopologyContainer(TopologyMessage tm) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public int GetMessageType() {
		return MessageType.TopologyChange;
	}

	@Override
	public Object GetMessage() {
		return this.content;
	}

}
