package Mailbox.Algorithm;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.Node;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import Client.MultipleTopicContainer.TopologyDataStructure;
import Configuration.Configuration;
import Mailbox.Messages.PublicationMessage;
import Mailbox.Messages.TopicRequest;
import MembershipService.Algorithm.MembershipServiceNode;
import Multicast.Algorithm.ChangeType;
import Multicast.Algorithm.ChordData;
import Multicast.Algorithm.PerTopicFinger;
import ProbobalisticLookup.Messages.HintMessage;
import ProbobalisticLookup.Messages.LookupMessage;
import Tools.MyBloomFilter;

public abstract class PubSubNode extends MembershipServiceNode{

	private MyBloomFilter topicFilter;


	public PubSubNode(ChordData chordId,KeybasedRouting kbr,TopologyDataStructure TopologyData) throws IOException {
		super(chordId,kbr, TopologyData);

		topicFilter = new MyBloomFilter(Configuration.BLOOM_FILTER_SIZE);
		// TODO Auto-generated constructor stub
	}
	abstract boolean isSubscribed(int Topic);
	public void Subscribe(int Topic,BucketManager b,List<PublicationMessage> publications)
	{
		if(!isSubscribed(Topic))
		{

			try {
				int oldState = this.getState();
				b.consumeTopicRequest(new TopicRequest(Topic));
				for (PublicationMessage publicationMessage : publications) {
					b.handlePublish(publicationMessage);
				}
				this.TopologyData.AddTopic(this.myData.eWolfNode, Topic);
				// need to redeploys the hints.
				topicFilter.add(Topic);
				if(oldState == MailboxState.Inactive )
					refreshHints();
				if(oldState == MailboxState.Recruting)
				{
					int newState = this.getState();
					if(newState == MailboxState.Full)
						refreshHints();
				}
				// notify deterministically.
				this.NotifyTopologyChange(this.myData, Topic, ChangeType.Join);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	// need to deploy the filter to other nodes. 
	protected void refreshHints() {



		HintMessage message = new HintMessage();
		message.owner = this.myKbr.getLocalNode();
		if(getState() == MailboxState.Full)
		{
			message.predicate = this.topicFilter;
		}
		else
		{
			message.predicate = this.topicFilter.getFullFilter();
		}
		message.TTL = Configuration.ADV_RAD;
		this.handleHintMessage(this.myKbr.getLocalNode(), message);

	}
	public void Publish(PublicationMessage content,int topic)
	{
		try {

			//this.ConsumeContent(this.myData.eWolfNode, content);
			// publish deterministically to known nodes. this thing require publication message.
			//System.out.println("Mailbox: "+ this.myData.chordID + " Publish on topic:" + topic);
			this.TopicMulticast(topic, content);


			{
				LookupMessage lookup = new LookupMessage();
				lookup.Content = content;
				lookup.sender = this.myKbr.getLocalNode();
				lookup.topic = topic;
				lookup.TTL = Configuration.PUB_TTL+1;
				lookup.alreadyVisited.add(this.myKbr.getLocalNode());
				lookup.hits.add(this.myData);
				// don't want to reach the mailboxes I know.
				Set<Node> Table = this.TopologyData.getMatchingNodes(topic);

				for (Node Data : Table) {
					lookup.alreadyVisited.add(Data);
					lookup.hits.add(new ChordData(Data));
				}
				this.handleLookupMessage(this.myKbr.getLocalNode(), lookup);



			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




}
