package Multicast.Algorithm;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.Node;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import Client.MultipleTopicContainer.TopologyDataStructure;
import Configuration.Configuration;
import Multicast.Messages.MessageContainer;
import Multicast.Messages.MulticastMessage;
import ProbobalisticLookup.Algorithm.ProbobalisticLookupNode;



public abstract class MulticastNode extends ProbobalisticLookupNode{

	public boolean virgin = true;

	public ChordData myData;

	//public ConcurrentHashMap<Integer,Node> Neighbors;
	

	public MulticastNode(ChordData data,KeybasedRouting kbr ,TopologyDataStructure td) throws IOException
	{
		super(kbr,td);
		TopologyData =td;
		myData = data;
	}
	@Override
	public void RegisterEvents() {
		super.RegisterEvents();
		//Neighbors = new ConcurrentHashMap<Integer, Node>();


		this.myKbr.register(Configuration.MULTICAST_TAG, new MessageHandler() 
		{



			@Override
			public void onIncomingMessage(Node from, String stam,
					Serializable msg) {
				try {
					myMonitor.nrMessageshandled.incrementAndGet();
					myMonitor.nrPublish.incrementAndGet();
					
					MulticastMessage Message = (MulticastMessage)msg;	
					HandleMulticastMessage(from,Message);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			@Override
			public Serializable onIncomingRequest(Node arg0, String arg1,
					Serializable arg2) {
				throw new RuntimeException("don't use requests!");
			}});




	}

	public boolean AddNode(Integer topic, ChordData nodeData) throws Exception
	{
		return  this.TopologyData.AddTopic(nodeData.eWolfNode, topic);

	}

	public boolean TryAddNode(Integer topic, ChordData nodeData) throws Exception
	{
		if(!TopologyData.getTopics().contains(topic))
		{
			return false;

		}
		return  this.TopologyData.AddTopic(nodeData.eWolfNode,topic);

	}
	public boolean RemoveNode(Integer topic, ChordData nodeData)
	{
		this.TopologyData.RemoveNode(nodeData.eWolfNode);
		return true;
	}
	public void HandleMulticastMessage(Node Sender,MulticastMessage m) throws IOException
	{
		// we will handle every specific message - and then multicast the message to the rest. 
		// System.err.println(this.myData.chordID+" : recived publication "+ m.Content);

		//		if(m.maxID<this.myData.chordID)
		//		{
		//			return;
		//		}
		MultiCast(Sender,m.Topic,m.Content,m.maxID,Configuration.MULTICAST_FANOUT);

	}
	public boolean TopicMulticast(Integer topic, MessageContainer Content) throws IOException
	{
		this.ConsumeContent(this.myData.eWolfNode, Content);
	
			Set<Node> table = this.TopologyData.getMatchingNodes(topic);
			for (Node chordData : table) {
				if(!chordData.equals(this.myData.eWolfNode))
					SendMultiCast(new ChordData(chordData),topic,Content,Integer.MAX_VALUE);
			}
			return false;
		

		//		if(this.myData.chordID != Finger.getFirst().chordID)
		//		{
		//
		//
		//			SendMultiCast(Finger.getFirst(),topic,Content,this.myData.chordID -1);
		//		}
		//
		//		return MultiCast(this.myData.eWolfNode,topic,Content,(Finger.getLast().chordID), Configuration.MULTICAST_FANOUT);
	}

	private boolean MultiCast(Node Sender,Integer topic, MessageContainer Content,Integer To, int Fanout) throws IOException
	{

		if(!this.ConsumeContent(Sender,Content))
		{

			return true;
		}	
		if(To <= this.myData.chordID)
		{

			return true; 
		}

		//		PerTopicFinger Finger = perTopicFingerTable.get(topic);
		//
		//		if(Finger == null)
		//		{
		//			Finger = new PerTopicFinger(this.myData);
		//			perTopicFingerTable.put(topic, Finger);
		//		}
		//
		//		ChordData Next = Finger.getNext();
		//		int Stepdistance = (Next.chordID + To)/Configuration.MULTICAST_FANOUT;
		//
		//
		//		for(int i =1; i<=Configuration.MULTICAST_FANOUT; i++)
		//		{
		//			if(Next == null)
		//			{
		//				System.out.println("Next is null");
		//				continue;
		//			}
		//
		//			int rangeMiddle = (Next.chordID + i*Stepdistance);
		//			SendMultiCast(Next,topic,Content,rangeMiddle);
		//			Next = Finger.GetNext(rangeMiddle);
		//
		//		}
		return true;		
	}
	private void SendMultiCast(ChordData reciver, Integer topic, MessageContainer content, int maxId) throws IOException {
		try
		{

			MulticastMessage m = new MulticastMessage();
			m.Topic = topic;
			m.Content= content;
			m.maxID = maxId;


			this.myKbr.sendMessage(reciver.eWolfNode, Configuration.MULTICAST_TAG,m);

		}
		catch(Exception e)
		{
			System.err.println("Error Sending message: Cid: " + this.myData.chordID +"reciver = "+ reciver.chordID );
			this.TopologyData.RemoveNode(reciver.eWolfNode);
			reciver = new ChordData(this.TopologyData.getMatchingNodes(topic).iterator().next());
			if(reciver.chordID < maxId){
				SendMultiCast(reciver,topic,content,maxId);
			}


		}
	}



}
