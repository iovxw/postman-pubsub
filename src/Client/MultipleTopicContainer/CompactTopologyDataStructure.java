package Client.MultipleTopicContainer;

import il.technion.ewolf.kbr.Node;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.google.inject.Inject;

import Tools.Topic;

public class CompactTopologyDataStructure implements TopologyDataStructure {
	private final Set<MailboxInformation> data;
	private final Set<Integer> topics;

	@Inject
	public CompactTopologyDataStructure()
	{
		data = new HashSet<MailboxInformation>();
		topics = new ConcurrentSkipListSet<Integer>();
	}

	@Override
	public synchronized Set<Node> getMatchingNodes(Topic t) {
		Set<Node> result = new HashSet<Node>();
		try{
		for (MailboxInformation mbi : data) {
			if(mbi.isInterested(t))
				result.add(mbi.getNode());
		}
		return result;
		}
		catch(Exception e)
		{
			System.err.println("Err in get matching nodes");
			return null;
		}
		
	}

	@Override
	public synchronized boolean AddTopic(Node d, Integer topic) {
		topics.add(topic);
		for (MailboxInformation mbi : data) {
			if(mbi.equals(d))
			{
				return mbi.AddTopic(topic);

			}
		}
		MailboxInformation mbi = new MailboxInformation().setMailbox(d);
		mbi.AddTopic(topic);
		data.add(mbi);
		return true;

	}

	@Override
	public synchronized boolean AddTopic(Node d, Topic t) {
		topics.addAll(t.getMyTopic());
		for (MailboxInformation mbi : data) {
			if(mbi.equals(d))
			{
				return mbi.AddTopic(t);

			}
		}
		MailboxInformation mbi = new MailboxInformation().setMailbox(d);
		mbi.AddTopic(t);
		data.add(mbi);
		return true;

	}

	@Override
	public synchronized void RemoveNode(Node d) {
		try{
			for (MailboxInformation mbi : data) {
				if(mbi.getNode().equals(d))
				{
					data.remove(mbi);
					break;
				}

			}
		}
		catch(Exception e)
		{
			System.err.println("Error in remove node");
		}

	}

	@Override
	public synchronized Set<Node> getMatchingNodes(Integer topic) {
		return getMatchingNodes(new Topic(topic));
	}

	@Override
	public Set<Integer> getTopics() {
		return topics;
	}

	@Override
	public boolean AddNode(Node d, Integer topic) {

		for (MailboxInformation mbi : data) {
			if(mbi.equals(d))
			{
				return mbi.AddTopic(topic) && topics.contains(topic);

			}
		}
		MailboxInformation mbi = new MailboxInformation().setMailbox(d);
		mbi.AddTopic(topic);
		data.add(mbi);
		return topics.contains(topic);
	}

	@Override
	public void Reset() {
		this.data.clear();
	}



}
