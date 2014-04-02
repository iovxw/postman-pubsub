package Mailbox.Algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import junit.framework.Assert;
import Client.MultipleTopicContainer.TopologyDataStructure;
import Configuration.Configuration;
import Mailbox.Messages.PollMessage;
import Mailbox.Messages.PollReplayMessage;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TopicRequest;

public class BucketManager {

	public Set<Integer> rawTopics;
	Map<Integer, Bucket> TopicBuckets;
	List<Bucket> ComplexBuckets;


	/*
	 * class is lock free to prevent deadlock problems. 
	 * notice that adding complex buckets require copying of all the known buckets. 
	 * so we assume it is rare operation. if it prove to be to costly, we will use locks. 
	 * (Currently it seems to be less debug time to ignore locking and just use lock-free classes.) 
	 */
	public BucketManager()
	{
		TopicBuckets = new ConcurrentHashMap<Integer, Bucket>();
		ComplexBuckets = new CopyOnWriteArrayList<Bucket>();
		rawTopics = new ConcurrentSkipListSet<Integer>();
	}
	private boolean isRequriting()
	{
		return this.rawTopics.size()< Configuration.TOPICS_PER_MB;
	}

	public Integer getPublicationNumber()
	{
		Collection<Bucket> buckets = TopicBuckets.values();
		Integer result = 0;
		for (Bucket bucket : buckets) {
			result+=bucket.content.size();
		}
		return result;
	}
	public List<PublicationMessage> getPublications(Integer Topic)
	{
		Bucket b =this.getBucket(new TopicRequest(Topic));
		if(b== null)
		{
			return new ArrayList<PublicationMessage>();
		}
		Assert.assertNotNull(b);
		// <0 means return all the publications!.
		return b.GetPublicationMessages(-1);
	}

	/*
	 * Generate a PollReplay message from PollMessage
	 */
	public PollReplayMessage handlePoll(PollMessage req, TopologyDataStructure finger)
	{
		PollReplayMessage res = new PollReplayMessage();

		for (TopicRequest topicreq : req.requestedTopics) {
			List<PublicationMessage> pubs = consumeTopicRequest(topicreq);
			if(pubs == null)
				continue;
			res.addContent(pubs,topicreq.topicID);
			try{
				res.addNodes(finger.getMatchingNodes(topicreq.topicID), topicreq.topicID);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		return res;
	}
	/*
	 * This function is to be used while the mailbox is collecting topics. 
	 * it it will create a new bucket if necessary. 
	 */
	List<PublicationMessage> consumeTopicRequest(TopicRequest req)
	{
		if(isRequriting())
			register(req);

		return handleTopicRequest(req);

	}

	/*
	 * this method is to be used when the mailbox is not collecting new topics. 
	 * it won't create new buckets. 	
	 */
	private List<PublicationMessage> handleTopicRequest(TopicRequest req)
	{
		Bucket b= null;

		b = getBucket(req);
		if(b == null)
			return null;

		return b.GetPublicationMessages(req.publicationNumber);


	}

	/*
	 * return if the publication message was relevant. add a publication message to all rellevant topics. 
	 */
	public boolean handlePublish(PublicationMessage pm)
	{

		// if the publication don't match my raw topics I can safely throw it away.
		if(rawTopics.containsAll(pm.topics))
		{
			// for simple topics no need to scan array.
			if(pm.topics.size() ==1)
			{

				Bucket b = this.TopicBuckets.get(pm.topics.get(0));
				return b.AddIfMatch(pm);

			}
			else
			{
				for (Bucket bucket : ComplexBuckets) {
					bucket.AddIfMatch(pm);
				}
				// intersection topic should also add to all related raw topics. 
				for (Integer topic : pm.topics) {
					Bucket b = this.TopicBuckets.get(topic);
					b.AddIfMatch(pm);

				}
			}

			return true;
		}
		return false;
	}


	public void register(TopicRequest req) {

		Bucket b = getBucket(req);
		if(b == null)
		{
			if(req.isComplexTopic())
			{
				addComplexTopicBucket(req);
			}
			else
			{
				addRawTopicBucket(req.getMyTopic().get(0));
			}
		}
	}
	private void addComplexTopicBucket(TopicRequest req) {
		Bucket b;
		for (Integer topic : req.getMyTopic()) {
			this.addRawTopicBucket(topic);
		}
		// sainity check.
		if(rawTopics.containsAll(req.getMyTopic()))
		{
			b = new Bucket(req.getMyTopic());
			ComplexBuckets.add(b);
		}
	}
	private void addRawTopicBucket(int topic) {
		if(this.rawTopics.add(topic))
		{
			Bucket b = new Bucket(topic);
			TopicBuckets.put(topic, b);
		}
	}
	private Bucket getBucket(TopicRequest req) {
		Bucket b = null;
		Assert.assertNotNull(req);
		Assert.assertNotNull(req.getMyTopic());
		Assert.assertTrue(req.getMyTopic().size() >0);
		//ssert.assertTrue(req.trId >0);

		if(req.isComplexTopic())
		{
			b = handleComplexTopic(req);
		}
		else
		{
			int topic = req.getMyTopic().get(0);
			b = TopicBuckets.get(topic);
		}
		return b;
	}
	private Bucket handleComplexTopic(TopicRequest req) {
		return null;
		//		Bucket b = null;
		//		if(req.bucketID > 0 )
		//		{
		//			if(req.bucketID < ComplexBuckets.size())
		//			{
		//				b = ComplexBuckets.get(req.bucketID);
		//			}
		//		}else
		//		{
		//			for (Bucket bucket : ComplexBuckets) {
		//				if(bucket.interectingtopics.containsAll(req.getMyTopic()) && req.getMyTopic().containsAll(bucket.interectingtopics))
		//				{
		//					b = bucket;
		//					break;
		//				}
		//
		//			}
		//		}
		//		return b;
	}

}
