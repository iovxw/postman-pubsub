package Multicast.Algorithm;

import java.io.Serializable;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class PerTopicFinger implements Serializable {
	
	Random rnd;
	
	private static final long serialVersionUID = -359916833694571683L;
	/**
	 * 
	 */
	
	ChordData myId;
	Integer myTopic;
	public TreeSet<ChordData> Table;
	public void Printtopic()
	{

/*
		for (ChordData d :Table) {
				System.out.print(d.chordID + " ");
		}
*/
	}
	public PerTopicFinger(ChordData id)
	{
		myId = id;
		Table = new TreeSet<ChordData>();
		Table.add(id);
		rnd = new Random();
	}

	public  ChordData GetNext(int id)
	{
		ChordData dummyID = new ChordData(id,null);
		return Table.higher(dummyID);
		

	}
	public  ChordData Get(int id)
	{
		ChordData dummyID = new ChordData(id,null);
		return Table.ceiling(dummyID);
		

	}
	public  boolean contains(int Id)
	{
		return Table.contains(new ChordData(Id, null));
	}
	public  boolean addNode(ChordData n) throws Exception
	{
		/*if(Table.contains(n))
			return true;*/
		if(n.eWolfNode == null)
		{
			System.err.println("ERROR: adding null node!");
			
		}
		//Table.remove(n);
		return Table.add(n);
	}
	public boolean removeNode(ChordData n)
	{
		if(Table.contains(n)){
			Table.remove(n);
			return false;
		}
		return true;

	}
	public  TreeSet<ChordData> merge(PerTopicFinger result)
	{
		
		if(result != null)
			Table.addAll(result.Table);
		return Table;
	}
	public  TreeSet<ChordData> getNewInformation(PerTopicFinger result)
	{
		boolean HasNext = true;
		ChordData prev = null;
		while(HasNext)
		{
		ChordData res = result.getNext();
		if (res.equals(prev))
			HasNext = false;
		else
		{
			prev = res;
		}
		System.out.println("Next " + res.chordID);
		}
		result.Table.removeAll(Table);
		return result.Table;
	}
	
	public ChordData getFirst()
	{
		return Table.first();
	}
	public ChordData getLast()
	{
		return Table.last();
	}
	public  ChordData getNext()
	{

	//	System.out.println("ID:" + this.myId + "Last: "+getLast().chordID + "Fitst: " + getFirst().chordID);
	//	for (ChordData da : Table) {
	//		System.out.print(" "+ da.chordID);
	//		
	//	}
		ChordData d = Table.higher(myId);
		if(d == null||d.chordID == this.myId.chordID)
		{
			d = getFirst();
		}
		return d; 
	}
	public ChordData getMiddle(int to)
	{
		int next = getNext().chordID;
		
		if(to > next )
		{
			if((to-next) == 1)
				return GetNext (to);
			
			return GetNext ((to + next)/2 );
		}
		else
		{
			
			
			int middle =-1;
			SortedSet<ChordData> BigNumbers = Table.tailSet(new ChordData(next,null));
			int bigger = BigNumbers.size();
			SortedSet<ChordData> SmallNumbers = Table.headSet(new ChordData(to,null));
			int smaller = SmallNumbers.size();
			
			if(bigger>smaller)
			{
				middle = next + (bigger+smaller)/2;
				return Table.ceiling(new ChordData(middle,null));
			}
			else
			{
				middle = (smaller - bigger)/2;
				return Table.ceiling(new ChordData(middle,null));
			}
	
		}
		
	}
	
	public ChordData getRandom()
	{
		int max = getLast().chordID;
		int min = getFirst().chordID;
		if(max == min)
		{
			return getFirst();
		}
		int offset = rnd.nextInt(max-min);
		return Get(min+offset);
	}
}
