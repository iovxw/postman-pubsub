package Mailbox.Algorithm;
import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.Node;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.Assert;
import Client.MultipleTopicContainer.TopologyDataStructure;
import Configuration.Configuration;
import Mailbox.Messages.PollMessage;
import Mailbox.Messages.PollReplayMessage;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TopicRequest;
import Mailbox.Messages.TopicsQueryMessage;
import Mailbox.SubscribeCache.SubscriptionCache;
import MembershipService.Messages.TopologyMessage;
import Multicast.Algorithm.ChangeType;
import Multicast.Algorithm.ChordData;
import Multicast.Messages.MessageContainer;
import Multicast.Messages.MessageType;
import ProbobalisticLookup.Messages.LookupMessage;
import DebugLogic.ChrunMessage;
import com.google.inject.Inject;



public  class Mailbox extends PubSubNode   {
	public boolean debugIsOperating =false;
	public BucketManager buckets;
	private boolean inactive = true;
	protected SubscriptionCache subscriptions;
	private MailboxStateController state;
	public AtomicBoolean isDown;
	
	
	
	@Override
	public int getState()
	{
		return state.getState();

	}
	public synchronized Integer getNumberOfMailboxPublications()
	{
		return this.buckets.getPublicationNumber();
	}
	public synchronized void Activate(int topic)
	{
		/*Register only once...!*/
		if(this.state.getState() == MailboxState.Inactive)
		{	
			System.out.println("Mailbox: "+ this.myData.chordID + "is now active!");
			this.state.SetActive();
			RegisterEvents();
			activateGossip();
		}
		if(!this.isSubscribed(topic))
		{
			refreshHints();
			this.Subscribe(topic, buckets, new ArrayList<PublicationMessage>());
		}

	}
	@Inject
	public Mailbox(ChordData chordId,KeybasedRouting kbr,SubscriptionCache sc,TopologyDataStructure TopologyData) throws IOException
	{
		
		super(chordId, kbr, TopologyData);
		buckets = new BucketManager();
		state = new MailboxStateController(this);
		subscriptions = sc;
		isDown = new AtomicBoolean(false);
	}
	@Override
	public boolean ConsumeContent(Node sender, MessageContainer content) {

		if(content.GetMessageType() == MessageType.Publication)
		{
			if(isDown.get())
				return false;
			
			PublicationMessage message = (PublicationMessage)content.GetMessage();
			this.subscriptions.DistributePublish(message);
			return this.buckets.handlePublish(message);
			


		}
		if(content.GetMessageType() == MessageType.PLS_Lookup )
		{
			LookupMessage  lookup= (LookupMessage) content.GetMessage();
			// it is a client looking for a mailbox.
			// preheaps change to topic request.
			int mystate = state.getState();
			//System.out.println("before!");
			if(!isDown.get())
				handlePLSSubscribe(lookup, mystate);
			// the lookup is actually a publication.
			if(lookup.Content!= null)
			{
				if( mystate != MailboxState.Inactive)
				{
					if(!isDown.get())
						handlePLSPublish(lookup);
				}
			}
			//eventually we route the lookup to the next hop.
			return false;
		}
		// not these types... let's hope the super class knows what to do. 
		return super.ConsumeContent(sender,content);
	}

	private synchronized boolean handlePLSSubscribe(LookupMessage lookup,
			int mystate) {

		lookup.alreadyVisited.add(this.myKbr.getLocalNode());
		// if I am not active - nothing to do here. 
		if(mystate == MailboxState.Inactive )
			return false;
		// if I am registered to the topic update the hits. 
		if(this.isSubscribed(lookup.topic))
		{
			addHits(lookup);
			lookup.hits.add(this.myData);
			return false;
		}
		// if I am not subscribed - I'll subscribe only if I am recruiting, and no one else did already.
		if(!this.isSubscribed(lookup.topic) && lookup.Content == null)
		{
			// if the mailbox is not full - and is not subscribed to the topic
			// subscribe only if no one else did. 
			// also: only subscribe if you don't know a better one. 
			Set<Node> nodes = TopologyData.getMatchingNodes(lookup.topic);
			nodes.remove(this.myKbr.getLocalNode());

			if(nodes.size()==0 && lookup.hits.size() ==0 && mystate == MailboxState.Recruting && lookup.TTL < Configuration.PUB_TTL)
			{
				this.LookupSubscribe(lookup, buckets, new ArrayList<PublicationMessage>());
				lookup.hits.add(this.myData);
				return false;
			}
		}
		return false;


	}
	private void addHits(LookupMessage lookup) {
		for (ChordData hit : lookup.hits) {
			try {
				if(this.TopologyData.AddTopic(hit.eWolfNode,lookup.topic)){
					// if it's a new one, we first notice the change.
					this.NotifyTopologyChange(hit, lookup.topic, ChangeType.Join);
					// then we introduce this mailbox by supplying the list of topics it possess
					// the other mailbox will re introduce itself to us. when it receive the message.
					myKbr.sendMessage(hit.eWolfNode, Configuration.TOPICS_GOSSIP_TAG, new TopicsQueryMessage().setTopics(TopologyData.getTopics()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void LookupSubscribe(LookupMessage lookup, BucketManager buckets2,List<PublicationMessage> publications) {


		this.Subscribe(lookup.topic, buckets2,publications);
		// learn from all the other hits.
		this.addHits(lookup);



	}

	private void handlePLSPublish(LookupMessage lookup) {
		Set<Node> Table = this.TopologyData.getMatchingNodes(lookup.topic);
		if(Table.size()>0)
			return;

		// update my data with the new mailboxes
		for (Node data  : Table) {

			lookup.hits.add(new ChordData(data));
		}
		// handle the actual publish message.
		if(this.buckets.handlePublish(lookup.Content))
		{
			this.Publish(lookup.Content, lookup.topic);
			this.subscriptions.DistributePublish(lookup.Content);
		}

	}

	int MaxTopics;
	int MaxBuckets;
	@Override
	public synchronized void RegisterEvents() {
		super.RegisterEvents();
		if(!inactive)
			return;
		inactive = false;
		this.myKbr.register(Configuration.POLL_TAG, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from,String stam ,Serializable req) {
				throw new RuntimeException("dont do it!");
			}

			@Override
			public void onIncomingMessage(Node from, String stam, Serializable req) {
				try
				{
					
					myMonitor.nrPolls.incrementAndGet();
					myMonitor.nrMessageshandled.incrementAndGet();
					
					PollMessage Message = (PollMessage)req;
					
					Assert.assertNotNull(Message);
					Assert.assertNotNull(Message.requestedTopics);
					Assert.assertTrue(Message.requestedTopics.size() >0);
					Assert.assertNotNull(Message.requestedTopics.get(0).getMyTopic());
					Assert.assertTrue(Message.requestedTopics.get(0).getMyTopic().size()>0);
					//Assert.assertTrue(Message.requestedTopics.get(0).trId > 0);

					
					///// DEBUG ONLY - if mailbox is down don't do poll. return a down message. 
					if(isDown.get())
					{
						myKbr.sendMessage(from, Configuration.POLL_RESULT_TAG+Message.ClientName, new DebugLogic.ChrunMessage());
						return;
					}
					
					
					
					PollReplayMessage PollResult = buckets.handlePoll(Message,TopologyData);
					// let the client know all the other topics this mailbox supports.
					PollResult.otherTopics = new HashSet<Integer>(TopologyData.getTopics());
					for (TopicRequest t : Message.requestedTopics) {
						if(t.gossip==null)
							continue;
						for (PublicationMessage pm : t.gossip) {
							if(pm != null && buckets.handlePublish(pm))
							{
								subscriptions.DistributePublish(pm);
								Publish(pm,pm.topics.get(0));

							}
						}

					}

					// send the message to the correct client.
					myKbr.sendMessage(from, Configuration.POLL_RESULT_TAG+Message.ClientName, PollResult);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}






		});

		this.myKbr.register(Configuration.CLIENT_PUBLISH, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from,String stam ,Serializable req) {
				throw new RuntimeException("Ops!");
			}

			@Override
			public void onIncomingMessage(Node from,String stam, Serializable msg) {

				try
				{
					// if I am down I do nothing.
					if(isDown.get())
						return;
					myMonitor.nrMessageshandled.incrementAndGet();
					myMonitor.nrPublish.incrementAndGet();


					PublicationMessage pm = (PublicationMessage) msg;
					//verify I got something reasonable!.
					Assert.assertNotNull(pm.content);
					Assert.assertNotNull(pm.topics);
					Assert.assertTrue(pm.topics.size()>0);
					//System.err.println("Mailbox received Client publication! topic: "+ pm.topics.get(0));
					// currently supporting publish of only one thing. 
					// TODO: (possible problem: publish may be blocking!)
					Publish(pm, pm.topics.get(0));
					subscriptions.DistributePublish(pm);
					buckets.handlePublish(pm);
					// just return ACK so the client knows we accepted the message. 

				}
				catch(Exception e)
				{
					System.err.println("Mailbox could not recieve client publication!");
				}

			}




		});

		this.myKbr.register(Configuration.LOOKUP_RES_TAG + Configuration.MAILBOX_NAME_TAG, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from,String stam, Serializable req) {
				throw new RuntimeException("Don't do it!");
			}

			@Override
			public void onIncomingMessage(Node from,String stam ,Serializable msg) {
				try {

					myMonitor.nrMessageshandled.incrementAndGet();
					myMonitor.nrTopologyGossip.incrementAndGet();
					
					LookupMessage lm = (LookupMessage)msg;
					//add the new hits of the lookup...
					addHits(lm);

				} catch (Exception e) {
					e.printStackTrace();
				} 
			}


		});
		this.myKbr.register(Configuration.TOPICS_GOSSIP_TAG, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from,String stam, Serializable req) {
				throw new RuntimeException("Don't do it!");
			}

			@Override
			public void onIncomingMessage(Node from,String stam ,Serializable msg) {
				try {

					myMonitor.nrMessageshandled.incrementAndGet();
					myMonitor.nrTopologyGossip.incrementAndGet();
					
					TopicsQueryMessage tqm = (TopicsQueryMessage)msg;
					//add the new hits of the lookup...
					for (Integer topic : tqm.getTopics()) {
						//add the topics without changing the registration. 
						if(TopologyData.AddNode(from, topic))
						{
							ChordData d = new ChordData();
							d.chordID = 0;
							d.eWolfNode = from;
							NotifyTopologyChange(d, topic, ChangeType.Join);
						}
					}
					if(!tqm.isResponse())
					{
						myKbr.sendMessage(from, Configuration.TOPICS_GOSSIP_TAG, tqm.setResponse().setTopics(TopologyData.getTopics()));
					}

				} catch (Exception e) {
					e.printStackTrace();
				} 
			}


		});


	}
	public synchronized Integer getNumberOfTopics()
	{
		return this.buckets.rawTopics.size();
	}
	@Override
	boolean isSubscribed(int Topic) {
		return this.buckets.rawTopics.contains(Topic);
	}
	private void activateGossip()
	{
		TopologyGossiper pg = new TopologyGossiper(this);
		this.getWorker().AddPeriodicTask(pg, Configuration.CONTENT_GOSSIP_TO, TimeUnit.SECONDS);
	}


}
