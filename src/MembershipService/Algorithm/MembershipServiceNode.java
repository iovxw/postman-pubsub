package MembershipService.Algorithm;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import Client.MultipleTopicContainer.TopologyDataStructure;
import Configuration.Configuration;
import MembershipService.Messages.TopicTopologyGossipMessage;
import MembershipService.Messages.TopologyContainer;
import MembershipService.Messages.TopologyMessage;
import Multicast.Algorithm.ChangeType;
import Multicast.Algorithm.ChordData;
import Multicast.Algorithm.MulticastNode;
import Multicast.Algorithm.PerTopicFinger;
import Multicast.Messages.MessageContainer;
import Multicast.Messages.MessageType;

public class MembershipServiceNode extends MulticastNode {
	
	private PeriodicTimeoutService worker;
	public void startGossip(Integer topic) throws IOException
	{
		try
		{
		TopologyGossipTask t = new TopologyGossipTask(this, topic);
		worker.AddPeriodicTask(t, Configuration.GOSSIP_TO, TimeUnit.SECONDS);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public MembershipServiceNode(ChordData data, KeybasedRouting kbr,TopologyDataStructure TopologyData) throws IOException {
		super(data,kbr, TopologyData);
		
	}
	@Override
	public void RegisterEvents() {
		super.RegisterEvents();
		this.myKbr.register(Configuration.TOPOLOGY_GOSSIP_TAG, new MessageHandler() 
		{

			@Override
			public byte[] onIncomingRequest(Node from,String stam ,Serializable req) {
				
				myMonitor.nrMessageshandled.incrementAndGet();
				myMonitor.nrTopologyGossip.incrementAndGet();
				
				TopicTopologyGossipMessage Message = null;
				try {
					Message = (TopicTopologyGossipMessage)req;
					return handleTopicTopologyGossipMessage(from,Message);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
				return new byte[0];
			}
			@Override
			public void onIncomingMessage(Node from,String stam, Serializable msg) {
				try {
					onIncomingRequest(from,stam,msg);
				} catch (Exception  e) {
					
					e.printStackTrace();
				}
				
			}
			
			
		}

		);
		this.setWorker(new PeriodicTimeoutService());
	}

	
	// decided not to use. instead using the client/mailbox gossip. 
	//
	public void publicationGossip(Node gossipTarget, int topic)
	{
		/*
		if(gossipTarget.equals(this.myData.eWolfNode))
		{
			System.err.println("Noone to gossip with...");
			return;
		}
		// set a new gossip message. 
		TopicTopologyGossipMessage ttgm = new TopicTopologyGossipMessage();
		// put my data in message.
		ttgm.result = this.perTopicFingerTable.get(topic);

		ttgm.SenderId = this.myData;
		ttgm.topic = topic;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();"+this.myData.chordID +" recived content"+content.GetMessage().toString());
		boolean tmp = virgin;
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(ttgm);
			oos.flush();
			
			Future<byte[]> response = this.myKbr.sendRequest(gossipTarget, Configuration.TOPOLOGY_GOSSIP_TAG, baos.toByteArray()).submit();

			byte[] responseArray = response.get();
			if(responseArray != null)
			{
				ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(response.get()));
				TopicTopologyGossipMessage responsemessage = (TopicTopologyGossipMessage) ois.readObject();
				this.handleTopicTopologyGossipMessage(gossipTarget,responsemessage);
				ois.close();
			}
			
			oos.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
	}
	public void NotifyTopologyChange(ChordData id,Integer topic, int join)
	{
		HashSet<Integer> myOtherTopics = new HashSet<Integer>(this.TopologyData.getTopics());
		MessageContainer mc = new TopologyContainer(join, id, topic,myOtherTopics);
		try {
			this.TopicMulticast(topic, mc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// to be used by the multicast mechanisem.
	@Override
	public boolean ConsumeContent(Node Sender,MessageContainer content) {
		int t = content.GetMessageType(); 
		switch(t)
		{
		case MessageType.String:
			return debugDeliverJustOnce(content);
		case MessageType.TopologyChange:
			return handleTopologyChangeMessage(Sender,(TopologyMessage)content.GetMessage());
		case MessageType.TopologyGossipMessage:
			try {
				 handleTopicTopologyGossipMessage(Sender, (TopicTopologyGossipMessage)content.GetMessage());
				 return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return virgin;
	}
	
public boolean handleTopologyChangeMessage(Node sender, TopologyMessage m)
	{

		switch(m.changeType)
		{
		case ChangeType.Join: 
			
			try {
				boolean res =  this.AddNode(m.topic, m.data);
				
				return res;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		case ChangeType.Leave: 
			System.out.println("Leave!");
			return this.RemoveNode(m.topic, m.data); 
		}
		return false;
	}
	public boolean debugDeliverJustOnce(MessageContainer content)
	{
		System.out.println("Node: "+this.myData.chordID +" recived content"+content.GetMessage().toString());
		boolean tmp = virgin;
		virgin = false;
		return tmp;
		//return false;
	}
	//TODO: remove this method. 
	private synchronized byte[] handleTopicTopologyGossipMessage(Node sender,TopicTopologyGossipMessage m) throws Exception
	{
		return null;
		
//
//		
//		if(!(TopologyData.AddTopic(m.SenderId.eWolfNode, m.topic)))
//		{
//			TopologyContainer tm = new TopologyContainer(ChangeType.Join, m.SenderId, m.topic, new HashSet<Integer>(this.TopologyData.getTopics()));
//			try {
//				
//				this.TopicMulticast(m.topic, tm);
//
//			} catch (Exception e) {
//
//				e.printStackTrace();	
//			}
//		}
//		for (iterable_type iterable_element : iterable) {
//			
//		}
//		topicData.merge(m.result);
//		
//		m.isResponse = true;
//		m.result =topicData;
//		ChordData src = m.SenderId;
//		m.SenderId = this.myData;
//		
//		// send result back. 
//		try {
//			System.out.println(m.SenderId.chordID + " is Sending Replay to : "+ src.chordID);
//			ByteArrayOutputStream baos= new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(m);
//			oos.flush();
//			baos.flush();
//			return baos.toByteArray();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.err.println("Error with gossip message");
//		}
//		return new byte[0];
//
//
//

	}
	public void setWorker(PeriodicTimeoutService worker) {
		this.worker = worker;
	}
	public PeriodicTimeoutService getWorker() {
		if(worker == null)
			worker = new PeriodicTimeoutService();
		return worker;
	}



}

