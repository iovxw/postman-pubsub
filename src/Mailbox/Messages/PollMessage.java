package Mailbox.Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PollMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9055872541435908636L;
	public List<TopicRequest> requestedTopics;
	public String ClientName;
	// how do I request topic intersection?

	public PollMessage(TopicRequest req, String myname)
	{
		requestedTopics = new ArrayList<TopicRequest>();
		requestedTopics.add(req);
		ClientName = myname;
	}
}
