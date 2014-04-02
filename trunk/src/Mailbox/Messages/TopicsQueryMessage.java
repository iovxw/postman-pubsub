package Mailbox.Messages;

import java.io.Serializable;
import java.util.Set;


// when I find a mailbox, I'll ask it for the rest of it's topics. 
public class TopicsQueryMessage implements Serializable
{
	private static final long serialVersionUID = 1816928072794977390L;
	private boolean response =false;
	private Set<Integer> topics;
	


	public boolean isResponse() {
		return response;
	}

	public TopicsQueryMessage setResponse() {
		this.response = true;
		return this;
	}

	public Set<Integer> getTopics() {
		return topics;
	}

	public TopicsQueryMessage setTopics(Set<Integer> topics) {
		this.topics = topics;
		return this;
	}
}
