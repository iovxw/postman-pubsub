package Client;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import Client.MultipleTopicContainer.TopologyDataStructure;
import Mailbox.Algorithm.BucketManager;
import Mailbox.Messages.PollMessage;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TopicRequest;

public class ClientPollManager {
	public PollManager poller;
	private KeybasedRouting homeNode;
	public BucketManager bm;
	TopologyDataStructure mailboxes;
	public Node last;
	private ClientApplication myClient;
	// for debug only.
	public int numberOfMailboxesContacted;
	public int numberOfRounds;
	
	public ClientPollManager(KeybasedRouting n,ClientApplication app,TopologyDataStructure boxes)
	{
		this.homeNode = n;
		this.bm = new BucketManager();
		this.poller = new PollManager();
		this.myClient = app;
		mailboxes = boxes;
	}
	public void forget()
	{
		this.mailboxes.Reset();
		this.PollRound();
	}

	public synchronized Integer getTotalNumberOfPublications()
	{
		return bm.getPublicationNumber();
	}

	// make bucket
	public void makebucket(int topic)
	{
		bm.register(new TopicRequest(topic));
	}

	// return the publication client received per topic.
	public  List<PublicationMessage> getPublications(int topic)
	{
		return this.bm.getPublications(topic);
	}
	// return the number of mailboxes the client registered to. 
	public  Set<Node> getMailboxesForTopic(Integer topic)
	{
		return this.mailboxes.getMatchingNodes(topic);
	}
	public synchronized void RemoveMailbox(Node mb)
	{
		this.mailboxes.RemoveNode(mb);
	}


	public synchronized void AddMailbox(Integer t, Node mb)
	{
		// assumptions for correctness.
		Assert.assertNotNull(mb);
		Assert.assertTrue(t>=0);
		mailboxes.AddTopic(mb, t);

	}
	public synchronized void PollRound()
	{
		this.numberOfRounds++;
		Set<Integer> keyset = myClient.debugSubscribedTopics;
		Set<Node> subscribedMailboxes = new HashSet<Node>();
		this.myClient.NeedAnotherPoll.set(false);
		numberOfMailboxesContacted =0;
		//System.out.println("Client: "+ this.myClient.myname +"Begin Poll round:");

		for (Integer topic : keyset) {
			boolean needSubscribe = true;
			Node Target = null;
			try{
				TopicRequest request = poller.getNextTopicRequest(topic, this.bm);



				Assert.assertNotNull(request);                    
				Assert.assertNotNull(request.getMyTopic());           
				Assert.assertTrue(request.getMyTopic().size() > 0);   
				Assert.assertTrue(request.topicID >= 0);           

				Set<Node> targets = mailboxes.getMatchingNodes(topic);
				// if I don't know a mailbox on this topic... 
				// subscribe and continue. (subscribing takes time.) 



				if(targets == null|| targets.size()<1)
				{
					this.myClient.SubscribeTopic(topic);
					this.myClient.NeedAnotherPoll.set(true);
					continue;
				}
				List<Node> victims = new ArrayList<Node>(targets);
				for (Node node : victims) {

					if(subscribedMailboxes.contains(node))
					{
						needSubscribe = false;
						break;
					}
				}
				if(needSubscribe)
				{
					numberOfMailboxesContacted++;
					myClient.sendTemporarySubscribe(victims.get(0));
					subscribedMailboxes.add(victims.get(0));
				}




				for (Node node : targets) {
					Target = node;
					DoSinglePoll(request, node);

				}

			}
			catch(Exception e)
			{
				// reached here because the mailbox don't answer.
				// I will throw it away... 
				if(Target == null)
					continue;
				this.mailboxes.RemoveNode(Target);

				//e.printStackTrace();
			}

		}
		this.myClient.calculateHitRate();
		//if I need to subscribe I definately need another poll.
		if(this.myClient.NeedAnotherPoll.get())
		{
			PollTask pt = new PollTask(this.myClient);
			this.myClient.homeNode.getWorker().AddOneShot(pt, 2, TimeUnit.MINUTES);
		}

		//System.out.println("Client: "+ this.myClient.myname +"End Poll round: Number of subscribed Mailboxes: "+subscribedMailboxes.size());

	}
	protected  void DoSinglePoll(TopicRequest request, Node Target)
			throws IOException, InterruptedException, ExecutionException,
			ClassNotFoundException {

		Assert.assertTrue(request.getMyTopic().size() >0);
		PollMessage pm = new PollMessage(request,myClient.myname);
		this.homeNode.sendMessage(Target, Configuration.Configuration.POLL_TAG, pm);

	}



	public synchronized void setMailboxes(TopologyDataStructure mailboxes) {
		this.mailboxes = mailboxes;
	}

	public synchronized TopologyDataStructure getMailboxes() {
		return mailboxes;
	}


	public Node getNode(Integer topic) {
		try{
			Set<Node> nodes = mailboxes.getMatchingNodes(topic);
			if((nodes == null) || nodes.isEmpty()){
				this.myClient.SubscribeTopic(topic);
				return null;
			}
			return mailboxes.getMatchingNodes(topic).iterator().next();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
