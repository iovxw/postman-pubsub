package Mailbox.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Test;

import Mailbox.Messages.PollMessage;
import Mailbox.Messages.PollReplayMessage;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TopicRequest;
import Multicast.Algorithm.PerTopicFinger;

public class bucketManagerTester {
	@Test
	public void SimpleTopicFunctionalityTest() throws Exception
	{
		BucketManager bm = new BucketManager();
		TopicRequest req = new TopicRequest(1);

//		req.bucketID = -1;
//		req.trId = 1;
		Assert.assertFalse(bm.rawTopics.contains(1));
		bm.consumeTopicRequest(req);
		Assert.assertTrue(bm.rawTopics.contains(1));
		
		PublicationMessage p = new PublicationMessage();
		p.content = "Hello world";
		p.topics = new ArrayList<Integer>();
		p.topics.add(1);
		bm.handlePublish(p);
		
		List<PublicationMessage> result = bm.consumeTopicRequest(req);
		Assert.assertTrue(result.contains(p));
		result = bm.consumeTopicRequest(req);
		Assert.assertTrue(result.contains(p));
		
		PublicationMessage Other = new PublicationMessage();
		
		Other.content = "My world";
		Other.topics = new ArrayList<Integer>();
		Other.topics.add(2);
		bm.handlePublish(Other);
		result = bm.consumeTopicRequest(req);
		Assert.assertFalse(result.contains(Other));
		// knowing the internal structure... a bit dirty but verify that the correct bucket was created. 
		Assert.assertTrue((bm.TopicBuckets.get(1).interectingtopics.contains(1)));
		
		PublicationMessage p2 = new PublicationMessage();
		p2.content = "Hello world2";
		p2.topics = new ArrayList<Integer>();
		p2.topics.add(1);
		bm.handlePublish(p2);
		
		PublicationMessage p3 = new PublicationMessage();
		p3.content = "Hello world2";
		p3.topics = new ArrayList<Integer>();
		p3.topics.add(1);
		bm.handlePublish(p3);
		req.publicationNumber = 1;
		result = bm.consumeTopicRequest(req);
		Assert.assertFalse(result.contains(p));
		Assert.assertTrue(result.contains(p2));
		Assert.assertTrue(result.contains(p3));
		
	}
	@Test
	public void HandlePollFunctionalityTest()
	{
//		BucketManager bm = new BucketManager();
//		
//		TopicRequest req = new TopicRequest(1);
//
//		req.bucketID = -1;
//		req.trId = 1;
//		
//		TopicRequest req2 = new TopicRequest(2);
//
//		req2.bucketID = -1;
//		req2.trId = 2;
//		
//		TopicRequest req3 = new TopicRequest(3);
//
//		req3.bucketID = -1;
//		req3.trId = 3;
//		
//		PollMessage pollm = new PollMessage(req);
//		//pollm.requestedTopics.add(req);
//		pollm.requestedTopics.add(req2);
//		pollm.requestedTopics.add(req3);
//		
//		PollReplayMessage result = bm.handlePoll(pollm,new ConcurrentHashMap<Integer,PerTopicFinger>());
//		List<PublicationMessage> reqRes = result.Content.get(1);
//		List<PublicationMessage> reqRes2 = result.Content.get(2);
//		List<PublicationMessage> reqRes3 = result.Content.get(3);
//		Assert.assertNotNull(reqRes);
//		Assert.assertNotNull(reqRes2);
//		Assert.assertNotNull(reqRes3);
//		Assert.assertTrue(result.Content.containsKey(2));
//		Assert.assertFalse(result.Content.containsKey(4));
//		
//		PublicationMessage pm = new PublicationMessage();
//		pm.content = "Hell";
//		pm.topics.add(2);
//		bm.handlePublish(pm);
//		
//		result = bm.handlePoll(pollm,new ConcurrentHashMap<Integer,PerTopicFinger>());
//		reqRes = result.Content.get(1);
//		reqRes2 = result.Content.get(2);
//		reqRes3 = result.Content.get(3);
//		Assert.assertNotNull(reqRes);
//		Assert.assertNotNull(reqRes2);
//		Assert.assertNotNull(reqRes3);
//		Assert.assertTrue(result.Content.containsKey(2));
//		Assert.assertFalse(result.Content.containsKey(4));
//		Assert.assertTrue(reqRes2.contains(pm));
//		Assert.assertFalse(reqRes.contains(pm));
//		
		
	}

	
}
