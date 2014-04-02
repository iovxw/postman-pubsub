package MembershipService.Algorithm;

import Multicast.Algorithm.ChordData;
import Multicast.Algorithm.PerTopicFinger;


public class TopologyGossipTask implements Runnable{

	private MembershipServiceNode myBox;
	private Integer myTopic;
	public TopologyGossipTask(MembershipServiceNode membershipServiceNode, Integer t)
	{
		this.myBox = membershipServiceNode;
		this.myTopic =t;
	}
	@Override
	public void run() {
//		PerTopicFinger table = myBox.TopologyData.get(myTopic);
//		int gossipTarget = table.getRandom().chordID;
//		ChordData gossipNode = myBox.TopologyData.get(myTopic).Get(gossipTarget);
//		myBox.Gossip(gossipNode.eWolfNode, myTopic);
		
		
	}


}
