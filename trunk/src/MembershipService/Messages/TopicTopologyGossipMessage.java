package MembershipService.Messages;

import java.io.Serializable;

import Multicast.Algorithm.ChordData;
import Multicast.Algorithm.PerTopicFinger;

public class TopicTopologyGossipMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5593264326508214715L;
	public Integer topic;
	public ChordData SenderId;
	public boolean isResponse;
	public PerTopicFinger result;
}
