package ProbobalisticLookup.Algorithm;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.Node;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.management.monitor.Monitor;

import org.junit.Assert;

import Client.MultipleTopicContainer.TopologyDataStructure;
import Mailbox.Algorithm.MailboxState;
import Multicast.Algorithm.ChordData;
import Multicast.Messages.MessageContainer;
import ProbobalisticLookup.Messages.HintMessage;
import ProbobalisticLookup.Messages.LookupMessage;


// base class for the system, grant all nodes the ability to route PLS lookups;
public class ProbobalisticLookupNode {
	// for testing;
	public DebugLogic.StatsCollector myMonitor;
	public  KeybasedRouting myKbr;
	Set<HintMessage> hints;
	public static String PLS_HINT_TAG = "PLS_HINT";
	public static String PLS_LOOKUP_TAG = "PLS_LOOKUP";
	int debugIndicator = 0;
	boolean debugTarget =false;
	public TopologyDataStructure TopologyData;

	public  synchronized boolean ConsumeContent(Node Sender, MessageContainer content)
	{
		debugIndicator ++;
		return debugTarget;
	}
	public synchronized void handleHintMessage(Node from, HintMessage message) 
	{
		
		if(hints.contains(message))
			hints.remove(message);
		hints.add(message);
		List<Node> Nighbors = this.myKbr.getNeighbours();
		message.TTL--;
		if(message.TTL<=0)
			return;

		try {


			// send message to all neighbors.
			for (Node node : Nighbors) {
				try
				{

					this.myKbr.sendMessage(node, PLS_HINT_TAG, message);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public synchronized void handleLookupMessage(Node from, LookupMessage lookup) 
	{
		// transfer content to higher places.
		// if consume content decides not to forward anymore, we return to sender. 
		ConsumeContent(from,lookup);
		RouteLookup(lookup);


	}
	public synchronized void RouteLookup(LookupMessage lookup) {
		// check if there is still TTL on the lookup.
		lookup.TTL --;
		if(lookup.TTL<=0){
			try {
				//System.out.println("lookup returned to sender!");
				returnToSender(lookup);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// find all suitable receivers. 
		List<Node> recivers = new ArrayList<Node>();

		//first  we try and send the query to a known mailbox.
		Set<Node> KnownMailboxes = TopologyData.getMatchingNodes(lookup.topic);


		for (HintMessage msg : hints) {
			if(msg.predicate.contains(lookup.topic))
			{

				recivers.add(msg.owner);
			}
		}
		Collections.shuffle(recivers);


		lookup.alreadyVisited.add(this.myKbr.getLocalNode());



		Set<Node> hits = getHits(lookup);
		// first prefer sending deterministically. 
		if(KnownMailboxes.size() >0)
		{
			for (Node node : KnownMailboxes) {
				try
				{
					if(lookup.alreadyVisited.contains(node) || hits.contains(node))
					{
						continue;
					}
					this.myKbr.sendMessage(node, PLS_LOOKUP_TAG, lookup);
					return;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					continue;
				}
			}
		}



		if(recivers.size() >0)
		{
			for (Node node : recivers) {
				try
				{
					if(lookup.alreadyVisited.contains(node)|| hits.contains(node))
					{
						continue;
					}
					//System.err.println("Send to Hint! Topic: " + lookup.topic);
					this.myKbr.sendMessage(node, PLS_LOOKUP_TAG, lookup);
					// we only send once.
					return;
				}
				catch(Exception e)
				{
					// can't send? you are probobly down.
					TopologyData.RemoveNode(node);
					e.printStackTrace();


				}
			}
		}
		// if we reached this point either no receivers or none of them is able to get message.
		List<Node> Neighbors = this.myKbr.getNeighbours();
		Neighbors.removeAll(lookup.alreadyVisited);
		Neighbors.removeAll(hits);

		ArrayList<Node> array = new ArrayList<Node>(Neighbors);

		if(array.size() == 0)
		{

			try {
				returnToSender(lookup);

			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		//todo move to config.
		int retry = 5;
		Node chosenNode = null;

		while(retry >0 )
		{
			try
			{

				Random gen = new Random();
				int chosen = gen.nextInt(array.size());
				chosenNode = array.get(chosen);
				if(lookup.alreadyVisited.contains(chosenNode)||hits.contains(chosenNode))
				{
					System.err.println("what?? how?? in route lookup!");
					retry--;
					continue;
				}
				//System.err.println("Send to random!");
				this.myKbr.sendMessage(chosenNode, PLS_LOOKUP_TAG, lookup);
				// we only send once.
				return;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				retry--;
				continue;
			}
		}
		// if everything is already visited... we give up and just send it already.

		System.out.println("nothing else to do.!");
		try {

			returnToSender(lookup);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private Set<Node> getHits(LookupMessage lookup) {

		Set<Node> hits = new HashSet<Node>();
		if(lookup.hits == null)
			return hits;
		for (ChordData d : lookup.hits) {
			hits.add(d.eWolfNode);
		}
		return hits;
	}
	private synchronized void returnToSender(LookupMessage lookup) throws IOException {
		//sending the lookup back to sender. 
		lookup.TTL =0;

		Assert.assertNotNull(lookup.sender);

		//System.out.println("Lookup returned to sender!");
		this.myKbr.sendMessage(lookup.sender, Configuration.Configuration.LOOKUP_RES_TAG + lookup.ClientName, lookup);

		return;
	}
	public void RegisterEvents()
	{

	}
	public ProbobalisticLookupNode(KeybasedRouting kbr,TopologyDataStructure tds)
	{
		myKbr = kbr;
		this.myMonitor =  new DebugLogic.StatsCollector();
		TopologyData = tds;
		this.hints = new HashSet<HintMessage>();
		myKbr.register(PLS_HINT_TAG, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from,String stam,Serializable req) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onIncomingMessage(Node from, String stam,Serializable msg) {
				try {
					
					myMonitor.nrMessageshandled.incrementAndGet();
					myMonitor.nrHints.incrementAndGet();
					
					HintMessage Message = (HintMessage)msg;
					handleHintMessage(from,Message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}





		});
		kbr.register(PLS_LOOKUP_TAG, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from,String stam ,Serializable req) {

				throw new RuntimeException("send request instead of message!");
			}

			@Override
			public void onIncomingMessage(Node from, String stam,Serializable msg) {
				try {
					myMonitor.nrPLS.incrementAndGet();
					myMonitor.nrMessageshandled.incrementAndGet();
					LookupMessage Message = (LookupMessage)msg;
					handleLookupMessage(from,Message);
				} catch (Exception e) {

					e.printStackTrace();
				}

			}


		});

	}
	public int getState()
	{
		return MailboxState.Inactive;
	}


}
