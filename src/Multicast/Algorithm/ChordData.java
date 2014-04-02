package Multicast.Algorithm;

import il.technion.ewolf.kbr.Node;

import java.io.Serializable;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ChordData implements Comparable<ChordData>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer chordID;
	public Node eWolfNode;
	
	@Inject
	public ChordData(@Named("gil.param") int i, @Named("openkad.local.node") Node node) {
		// TODO Auto-generated constructor stub
		
		this.chordID = i;
		this.eWolfNode = node;
		}
	public ChordData()
	{
		
	}
	@Override
	public boolean equals(Object d)
	{
		if(d instanceof ChordData)
		{
			return ((ChordData) d).chordID == this.chordID;
		}
		return false;
		
	}
	@Override
	public int compareTo(ChordData d)
	{
		return this.chordID.compareTo(d.chordID);
		
		
	}
	public ChordData(Node node) {
		// TODO Auto-generated constructor stub
		//GIL: Modifiy it.
		this.chordID = node.getKey().getColor(4500);
		this.eWolfNode = node;
		}
	
}
