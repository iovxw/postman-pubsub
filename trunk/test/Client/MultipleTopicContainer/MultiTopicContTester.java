package Client.MultipleTopicContainer;

import il.technion.ewolf.kbr.Node;
import il.technion.ewolf.kbr.RandomKeyFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import Tools.Topic;

public class MultiTopicContTester {
	@Test
	public void basicFunctionalityTest()
	{
		TopologyDataStructure tds = new CompactTopologyDataStructure();
		try {
			RandomKeyFactory kgen = new RandomKeyFactory(10, new Random(),"SHA-256");
			kgen.generate();

			Node n = new Node(kgen.generate());
			Node n2 = new Node(kgen.generate());
			Node n3 = new Node(kgen.generate());
			tds.AddTopic(n, 1);
			tds.AddTopic(n2, 1);
			tds.AddTopic(n3, 2);
			tds.AddTopic(n, 2);
			
			Set<Node> res = tds.getMatchingNodes(new Topic(1));
			
			Assert.assertTrue( res.contains(n));
			Assert.assertTrue( res.contains(n2));
			Assert.assertTrue( !res.contains(n3));
			res = tds.getMatchingNodes(new Topic(2));
			Assert.assertTrue( res.contains(n));
			Assert.assertTrue( !res.contains(n2));
			Assert.assertTrue( res.contains(n3));
			res = tds.getMatchingNodes(new Topic(3));
			Assert.assertTrue(res.size()==0);
			tds.RemoveNode(n);
			res = tds.getMatchingNodes(new Topic(1));
			Assert.assertTrue( !res.contains(n));
			Assert.assertTrue( res.contains(n2));
			Assert.assertTrue( !res.contains(n3));
			res = tds.getMatchingNodes(new Topic(2));
			Assert.assertTrue( !res.contains(n));
			Assert.assertTrue( !res.contains(n2));
			Assert.assertTrue( res.contains(n3));
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
