package Client.MultipleTopicContainer;

import il.technion.ewolf.kbr.Node;

import java.util.Set;

import Tools.Topic;

public interface TopologyDataStructure {
	public  Set<Node> getMatchingNodes(Topic t);
	public  boolean AddTopic(Node d, Integer topic);
	public  boolean AddTopic(Node d, Topic t);
	public  void RemoveNode(Node d);
	public Set<Node> getMatchingNodes(Integer topic);
	public Set<Integer> getTopics();
	public boolean AddNode(Node d, Integer topic);
	public void Reset();
	
}
