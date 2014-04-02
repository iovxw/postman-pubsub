package Tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Topic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6954595821634602342L;
	private List<Integer> myTopic;
	public Topic(int t)
	{
		myTopic = new ArrayList<Integer>();
		myTopic.add(t);
	}
	public Topic(List<Integer> list)
	{
		myTopic = list;
	}
	@Override
	public boolean equals(Object other)
	{
		if(other == null  )
			return false;
		Topic topic = (Topic)other;
		boolean cond1 = this.myTopic.containsAll(topic.getMyTopic());
		boolean cond2 = topic.getMyTopic().containsAll(this.myTopic);
		if((cond1  && cond2))
			System.err.println("topics equal!");
		else
			System.err.println("topics not equal!");
		return (cond1  && cond2);
	}
	
	public List<Integer> getMyTopic() {
		return myTopic;
	}
	public boolean isComplexTopic()
	{
		if(myTopic == null)
			return false;
		else
		{
			if(myTopic.size()== 1)
				return false;
			else return true;
		}
	}
}
