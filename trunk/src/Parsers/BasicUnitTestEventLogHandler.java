package Parsers;



public class BasicUnitTestEventLogHandler implements EventLogHandler{

	
	@Override
	public void HandleJoin(String time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HandleLeave(String time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void HandleTweet(String time, String tweet) {
		//myNode.publish(1, tweet);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
