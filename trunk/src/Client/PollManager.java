package Client;

import il.technion.ewolf.kbr.Node;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.Assert;
import Client.MultipleTopicContainer.TopologyDataStructure;
import Mailbox.Algorithm.BucketManager;
import Mailbox.Messages.PollReplayMessage;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TopicRequest;

public class PollManager {
	Map<Integer, TopicRequest> topicCache;


	public PollManager()
	{
		topicCache = new ConcurrentHashMap<Integer, TopicRequest>();


	}

	public synchronized TopicRequest  getNextTopicRequest(Integer t,BucketManager bm)
	{
		Assert.assertNotNull(t);
		//		Assert.assertNotNull(t.getMyTopic());
		//		Assert.assertTrue(t.getMyTopic().size()>0);
		//get according to T

		TopicRequest tr = topicCache.get(t);

		if(null == tr)
		{
			//System.out.println("didn't find topic in cache, creating a new request");
			//TODO: bug!
			tr = createNewTopicRequest(t);
		}


		List<PublicationMessage> list = bm.getPublications(t);
		if(list == null|| list.size()<1)
			return tr;
		//Collections.shuffle(list);
		tr.setGossip(list);


		return tr;	
	}

	private synchronized TopicRequest createNewTopicRequest(Integer t) {
		TopicRequest tr;

		tr = new TopicRequest(t);
		tr.topicID =t;
		tr.publicationNumber=0;

		topicCache.put(t, tr);

		return tr;
	}

	public void StorePollReplayInformation(Node from, PollReplayMessage prm, BucketManager buckets, TopologyDataStructure mailboxes)
	{
		Set<Integer> ids = prm.Content.keySet();
		// update Topic request on each step.
		for (Integer topic : ids) {


			TopicRequest tr = topicCache.get(topic);
			Assert.assertNotNull(tr);
			//System.out.println("Topic: Result: ");
			List<PublicationMessage> results = prm.Content.get(topic);
			Assert.assertNotNull(results);
			//tr.publicationNumber += results.size();
			this.topicCache.remove(topic);
			this.topicCache.put(topic, tr);
			Set<Node> OtherNodes = prm.otherMailboxes.get(topic);
			if(OtherNodes!= null)
			{
				for (Node node : OtherNodes) {
					mailboxes.AddTopic(node, topic);
				}
			}


			for (Integer otherTopic : prm.otherTopics) {
				// if I am... then I just learned about a wonderful new use for this mailbox!.
				mailboxes.AddTopic(from, otherTopic);

			}

			for (PublicationMessage pm : results) {
				buckets.handlePublish(pm);
			}



		}
	}




}
