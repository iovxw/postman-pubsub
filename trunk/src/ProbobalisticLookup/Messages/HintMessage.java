package ProbobalisticLookup.Messages;

import java.io.Serializable;

import Tools.MyBloomFilter;
import il.technion.ewolf.kbr.Node;

public class HintMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	public int TTL;
	public Node owner;
	public MyBloomFilter predicate;
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		HintMessage other = (HintMessage) obj;
		
		return this.owner.equals(other.owner);
		
		
	}
	@Override
	public int hashCode()
	{
		return this.owner.hashCode();
	}
}
