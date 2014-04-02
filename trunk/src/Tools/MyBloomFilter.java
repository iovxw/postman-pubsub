
package Tools;


import java.util.BitSet;
public class MyBloomFilter extends BloomFilter<Integer> {
private int mysize;
	
	public MyBloomFilter getFullFilter()
	{
		MyBloomFilter result = new MyBloomFilter(mysize);
		result.bitset.set(0, mysize);
		return result;
	}
	
	// create a bloom filter of specifc size with default parameters.
	public MyBloomFilter(int size) {
		super(1.0,  size/3,  4);
		mysize = size;
	}
	 // I assume the blooms are the same. 
	public void Merge(MyBloomFilter other)
	{		
		this.bitset.or(other.bitset);
		this.numberOfAddedElements+= other.numberOfAddedElements;
	}
		
	private static final long serialVersionUID = 1L;
	@Override
	public String toString()
	{
		BitSet bitSet = this.getBitSet();
		int i =0;
		String result ="";
		for(i=0; i<bitSet.size();i++)
		{
			if(bitSet.get(i))
			{
				result = result+"1";
			}
			else
			{
				result = result+"0";
			}
		}
		return result;
	}
	


}
