package Client;

import il.technion.ewolf.kbr.MessageHandler;
import il.technion.ewolf.kbr.Node;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.Assert;
import Client.MultipleTopicContainer.TopologyDataStructure;
import Configuration.Configuration;
import Mailbox.Algorithm.Mailbox;
import Mailbox.Messages.PollReplayMessage;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TemporarySubscribeMessage;
import MembershipService.Algorithm.PeriodicTimeoutService;
import Multicast.Algorithm.ChordData;
import ProbobalisticLookup.Messages.LookupMessage;
import Tools.MyBloomFilter;
import Parser.Event;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public  class ClientApplication {


	public Mailbox homeNode;
	public ClientPollManager myPollManager;
	public String myname;
	public  Set<Integer> debugSubscribedTopics;
	public int misses;
	public int total;
	
	
	
	private List<Event> myEvents;
	private List<Integer> mysubscriptions;
	private  Integer myId;
	private Double from; 
	private ClientEventManager EventManager;
	boolean virgin = true;
	public AtomicBoolean active; 
	public AtomicBoolean NeedAnotherPoll;
	public MyBloomFilter filter;
	List<Node> blackList;
	
	
	@Inject
	public ClientApplication(Mailbox box,@Named("gil.client.name")String AppName,@Named("gil.client.filter.size")int filtersize,TopologyDataStructure topology)
	{
		this.active = new AtomicBoolean(false);
		System.out.println("Created Client: "+AppName);
		myname = AppName;
		homeNode = box;
		virgin = true;
		myPollManager = new ClientPollManager(box.myKbr, this, topology);
		debugSubscribedTopics = new ConcurrentSkipListSet<Integer>();
		filter = new MyBloomFilter(filtersize);
		NeedAnotherPoll = new AtomicBoolean(false);
		blackList = new ArrayList<Node>();
	}
	
	
	public synchronized void calculateHitRate() {

		
		for (Integer tt : this.debugSubscribedTopics) 
		{
			
			
			if(ClientTester.stats.get(tt) != null)
			{
				this.misses +=(ClientTester.stats.get(tt)-this.GetNumberOfPublications(tt));
				this.total += ClientTester.stats.get(tt);
			}
		}
		FileWriter outfile;
		try {
			outfile = new FileWriter(this.myname+".txt",true);
			outfile.write(total +" "+ misses+ " "+ this.myPollManager.numberOfMailboxesContacted +"\n");
			outfile.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public void registerEvents()
	{

		this.homeNode.myKbr.register(Configuration.LOOKUP_RES_TAG + this.myname, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from,String stam,Serializable req) {
				throw new RuntimeException("Don't do it!");
			}

			@Override
			public void onIncomingMessage(Node from,String stam ,Serializable msg) {
				try {
					LookupMessage lm = (LookupMessage)msg;
					for ( ChordData d : lm.hits) {
						
						if(blackList.contains(d.eWolfNode))
							continue;
						myPollManager.AddMailbox(lm.topic, d.eWolfNode);
					}

				} catch (Exception e) {
					e.printStackTrace();
				} 
			}

			


		});

		this.homeNode.myKbr.register(Configuration.POLL_RESULT_TAG + this.myname, new MessageHandler()
		{

			@Override
			public void onIncomingMessage(Node from, String tag,Serializable content) {
				try
				{
					Assert.assertNotNull(content);
					if(content == null)
						return;
					
					if(content instanceof DebugLogic.ChrunMessage)
					{
						// if the box is down it won't answer.
						myPollManager.RemoveMailbox(from);
						blackList.add(from);
						return;
					}
					
					
					
					
					PollReplayMessage result =  (PollReplayMessage)content;
					// store the result in my own buckets. 
					myPollManager.poller.StorePollReplayInformation(from,result, myPollManager.bm, myPollManager.mailboxes);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public byte[] onIncomingRequest(Node from, String tag,
					Serializable content) {
				throw new RuntimeException("Don't do it!");
			}

		}
		);
		this.homeNode.myKbr.register(Configuration.SUBSCRIBE_RESULT_TAG+this.myname, new MessageHandler() {

			@Override
			public byte[] onIncomingRequest(Node from, String tag, Serializable content) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onIncomingMessage(Node from, String tag, Serializable content) {
				try
				{
					
					PublicationMessage pm = (PublicationMessage) content;
					myPollManager.bm.handlePublish(pm);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		);
		
	}
	
	public boolean nextSubscribe()
	{
		if(mysubscriptions.isEmpty())
		{
			//System.out.println("no more topics!  " +  this.myId);
			return false;
		}
			try {
			this.SubscribeTopic(mysubscriptions.remove(0));
			System.out.println("Subscribed!  " +  this.myId);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
	public void BeginExperiment(double from, double to)
	{
		
		
		
	}

	public  synchronized void SubscribeTopic(Integer Topic) throws Exception
	{
		filter.add(Topic);
		debugSubscribedTopics.add(Topic);
		myPollManager.makebucket(Topic);


		Runnable r = new TopicSubscribeTask(this,Topic);
		PeriodicTimeoutService worker = this.homeNode.getWorker();
		if(worker == null)
			throw new Exception("No Worker!");
		worker.AddOneShot(r, 0, TimeUnit.SECONDS);
		Runnable fail = new TopicSubscribeFail(this,Topic);
		worker.AddOneShot(fail, Configuration.LOOKUPTIMEOUT, TimeUnit.SECONDS);
		if(virgin)
		{
			virgin = false;
			PollTask pt = new PollTask(this);
			this.homeNode.getWorker().AddPeriodicTask(pt, Configuration.POLLROUNDFREQUENCY, TimeUnit.SECONDS);
		}
	}
	public synchronized void PublishTopic(Integer topic, String Message)
	{
		// precondition check my input:
		Assert.assertTrue(topic>=0);
		Assert.assertNotNull(Message);
		System.out.println("Publish: " +topic +": "+ Message);
		// logic:
		// build publication message.
		PublicationMessage pm = new PublicationMessage();
		pm.content = Message;
		pm.topics =new ArrayList<Integer>();
		pm.topics.add(topic);
		Node box = this.myPollManager.getNode(topic);
		// check if I have a mailbox at all.
		if(box == null)
		{
			myPollManager.bm.handlePublish(pm);
			try {
				this.myPollManager.PollRound();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return;
		}
		try{
			// pick one of the mailboxes to start publishing from.
			// I need reliable send, parameter check before send: 
			Assert.assertNotNull(box);
		
			// the actual send. 
			this.homeNode.myKbr.sendMessage(box, Configuration.CLIENT_PUBLISH, pm);
			// client add the publish to its own publications.
			myPollManager.bm.handlePublish(pm);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void sendTemporarySubscribe(Node target)
	{
		try {
			//create subscribe message!
			TemporarySubscribeMessage tsm = new TemporarySubscribeMessage();
			tsm.setClientName(myname)
			.setClientFilter(filter)
			.setClientNode(homeNode.myKbr.getLocalNode());
			// send it to target. 
			
			homeNode.myKbr.sendMessage(target, Configuration.SUBSCRIBE_TAG, tsm);
		} catch (IOException e) {
			// what can I do... sometimes we fail. 
			e.printStackTrace();
		}


		//homeNode.myKbr.sendMessage(to, tag, msg)
	}

	// Debug functions:
	public synchronized Integer GetKnownMailboxesByHomeNode(Integer topic)
	{
		return this.homeNode.TopologyData.getMatchingNodes(topic).size();
	}
	public synchronized Integer GetNumberOfMailboxes(Integer topic)
	{
		try
		{
			return this.myPollManager.getMailboxesForTopic(topic).size();
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	public synchronized Integer GetNumberOfPublications(Integer topic)
	{
		try{
			return this.myPollManager.getPublications(topic).size();
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	public synchronized Integer GetMailboxNumberOfPublications(Integer topic)
	{
		try
		{
			return this.homeNode.buckets.getPublications(topic).size();
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	public synchronized Integer getClientSubscriptionSize()
	{
		return this.debugSubscribedTopics.size();
	}
	public synchronized Integer getNumberOfClientsMailboxes()
	{
		Set<Node> mailboxes = new HashSet<Node>();
		for (Integer topic : debugSubscribedTopics) {
			mailboxes.addAll(myPollManager.getMailboxesForTopic(topic));
		}
		return mailboxes.size();
	}
	public synchronized Integer getNumberOfClientPublications()
	{
		return myPollManager.getTotalNumberOfPublications();
	}
	public String PrintClientState()
	{
		String res = "";
		Set<Node> mailboxes = new HashSet<Node>();

		for (Integer topic : debugSubscribedTopics) {
			mailboxes.addAll(myPollManager.getMailboxesForTopic(topic));
		}
		try {
			res = "Client: "
		+ this.myname 
		+" Total Topics: "+debugSubscribedTopics.size()
		+ " Total Mailboxes: "+ mailboxes.size()
		+" Total Polls: "+this.myPollManager.numberOfRounds
		+ " Total Subscriptions: "+ this.myPollManager.numberOfMailboxesContacted + "\n";
			
			//BufferedWriter out = new BufferedWriter(new FileWriter("test"+this.myId+".txt",true));
			//out.write("Client: "+ homeNode.myData.chordID +" Total Topics: "+debugSubscribedTopics.size()+ " Total Mailboxes: "+ mailboxes.size());
			//out.newLine();
			//out.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		//System.out.println("Client: "+ homeNode.myData.chordID +" Total Topics: "+debugSubscribedTopics.size()+ " Total Mailboxes: "+ mailboxes.size());
		//System.out.println("Total Topics: "+debugSubscribedTopics.size());
		return res;





		//System.out.println("Total Mailboxes: " +mailboxes.size());
		//		System.out.println("*************Per Topic Details**************");
		//		for (Integer topic : debugSubscribedTopics) {
		//			System.out.println("Topic: "+topic +" Mailboxes: " +myPollManager.getMailboxes(topic).size() +" Publications: "+myPollManager.getPublications(topic).size());
		//			Set<Node> boxes = myPollManager.getMailboxes(topic);
		//			for (Node node : boxes) {
		//				System.out.println(node);
		//			}
		//		}
		//		System.out.println("*******************End**********************");
	}


	
	public List<Event> getMyEvents() {
		return myEvents;
	}
	public ClientApplication setMyEvents(List<Event> myEvents) {
		this.myEvents = myEvents;
		return this;
	}
	public List<Integer> getMysubscriptions() {
		return mysubscriptions;
	}
	public ClientApplication setMysubscriptions(List<Integer> mysubscriptions) {
		this.mysubscriptions = mysubscriptions;
		return this;
	}


	


	
	// first thing upon login we do a poll round. 
	public void doLogin() {
		this.active.set(true);
		this.myPollManager.PollRound();
	}


	public void doLogout() {
		//this.active.set(false);
	}


	public ClientEventManager getEventManager() {
		return EventManager;
	}


	public ClientApplication StartEventManager() {
		EventManager = new ClientEventManager(this);
		return this;
	}


	public Integer getMyId() {
		return myId;
	}


	public ClientApplication setMyId(Integer myId) {
		this.myId = myId;
		return this;
	}


	public Double getFrom() {
		return from;
	}


	public ClientApplication setFrom(Double from) {
		this.from = from;
		return this;
	}


	public synchronized int getMisses() {
		// TODO Auto-generated method stub
		return misses;
	}
	public synchronized int getTotal() {
		// TODO Auto-generated method stub
		return total;
	}


	

}
