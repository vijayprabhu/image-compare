package me.venkataraman.vijay.rasp.imagecompare;

import java.net.Inet4Address;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.qpid.jms.JmsConnectionFactory;

import me.venkataraman.vijay.rasp.queuewrapper.model.ImageMessage;

/**
 * Hello world!
 *
 */
public class App 
{
    @SuppressWarnings("unused")
	public static void main( String[] args ) throws Exception
    {
       
    		ImageMessage tMinus2Image = null;
    		ImageMessage tMinus1Image = null;
    		ImageMessage tZeroImage = null;    		
    		
    		String user = "admin";
	        String password = "password";
	        
			int port = 5672;
	        
	        String connectionURI = "amqp://192.168.1.16:" + port;
		
	        JmsConnectionFactory connectionFactory = new JmsConnectionFactory(connectionURI);
			
	        System.out.println("Connection Factory -> "+connectionFactory);
	        
			//ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://192.168.1.16");
	        Connection connection = connectionFactory.createConnection(user, password);
			String clientID = Inet4Address.getLocalHost().getHostName();
			
			System.out.println("Created Connection : "+connection +" on host "+clientID);

			connection.start();

	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
			System.out.println("Started Connection : "+connection.getMetaData().getJMSProviderName());

			Destination destination = session.createQueue("q.captured.image");
			
			MessageConsumer consumer = session.createConsumer(destination);
			
			while(true) {
				System.out.println("Waiting for Message!!!");
				
				Message message = consumer.receive();
				
				if(message!=null) {
					System.out.println("Got Message. Processing Now...");
				}
				
				if(message instanceof ObjectMessage) {
					ObjectMessage objMsg = (ObjectMessage)message;
					
					tMinus2Image = tMinus1Image;
					tMinus1Image = tZeroImage;
					
					tZeroImage = (ImageMessage) objMsg.getObject();
					
					if(tMinus1Image!=null) {
						
						double percentChange = ImageComparator.compareImages(tMinus1Image.getMessageStream(), tZeroImage.getMessageStream());
						System.out.println("Difference between "+tMinus1Image.getMessageDateTime()+" & "+tZeroImage.getMessageDateTime()+" is "+percentChange+"%");
					}
					
				}
			}
    	
    	
    }
}
