package MembershipService.Messages;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import Multicast.Algorithm.ChangeType;
import Multicast.Algorithm.ChordData;

public class TopologyMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1318131914849209282L;
	public int changeType;
	public ChordData data;
	public Integer topic;
	public Set<Integer> myOtherTopics;
	public TopologyMessage(int join,ChordData d, Integer t, HashSet<Integer> myOtherTopics2)
	{
		changeType = join;
		data = d;
		topic = t;
		myOtherTopics = new HashSet<Integer>();
		//myOtherTopics = myOtherTopics2;
	}
}
