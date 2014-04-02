package Parsers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class EventLogParser {
	private static final int JOIN = 0;
	private static final int TWEET = 2;
	private static final int LEAVE = 1;
	EventLogHandler handler;

	public EventLogParser(EventLogHandler a)
	{
		handler = a;
		
	}
	
	public void Parse(String filePath)
	{
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream(filePath);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   {
			  String[] arr = strLine.split(" ");
			  if(arr.length<2)
			  {
				  continue;
			  }
			  int Action = Integer.parseInt(arr[0]);
			  switch(Action)
			  {
			  case JOIN:
				  this.handler.HandleJoin(arr[1]);
				  break;
			  case LEAVE:
				  this.handler.HandleLeave(arr[1]);
				  break;
			  case TWEET:
				  String tweet = "";
				  for(int i =2; i<arr.length;i++)
				  {
					tweet= tweet+" "+ arr[i]; 
				  }
				  this.handler.HandleTweet(arr[1], tweet);
				  break;
			  }
				 
			  // Print the content on the console
			  //System.out.println (strLine);
			  }
			  //Close the input stream
			  in.close();
			    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}
}
