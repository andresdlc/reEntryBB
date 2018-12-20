
package bolivariano.offline.reentry.listener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.ibm.mq.jms.MQConnectionFactory;

import bolivariano.offline.reentry.repo.LogReentry;
import bolivariano.offline.reentry.repo.LogReentryDAOImpl;

@PropertySource("classpath:application.properties")
public class JmsQueueListener implements MessageListener {
	
	@Value("${uri.bus}")
	private String uriBus;
		
	@Autowired
	LogReentryDAOImpl LogReentryDAOImpl;

	@Resource(lookup = "cf")
	private MQConnectionFactory connectionFactory;
	
	@Resource(name="jmsContainer")  
	private DefaultMessageListenerContainer listenerContainer;  
	
	public void onMessage(final Message message) {

		System.out.println("LLEGO" + message);
		if (message instanceof TextMessage) {

			try {
				SOAPConnectionFactory connFactory = SOAPConnectionFactory.newInstance();
				SOAPConnection conn = connFactory.createConnection();
		      //String uri = uriBus+ message.getStringProperty("CamelHttpUri");
				String uri = uriBus+ message.getStringProperty("OffHttpUri");
				java.net.URL url = new java.net.URL(uri);
				String msj = ((TextMessage) message).getText();
				InputStream is = new java.io.ByteArrayInputStream(msj.getBytes());
				SOAPMessage request = MessageFactory.newInstance().createMessage(null, is);
				is.close();
				SOAPMessage response = conn.call(request, url);
				
				ByteArrayOutputStream baos = null;
				String result="";
	            try 
	            {
	                baos = new ByteArrayOutputStream();
	                response.writeTo(baos); 
	                result = baos.toString();
	            } 
	            catch (Exception e) 
	            {
	            } 
	            finally 
	            {
	                if (baos != null) 
	                {
	                    try 
	                    {
	                        baos.close();
	                    } 
	                    catch (IOException ioe) 
	                    {
	                    }
	                }
	            }
				
			
				int inicial = msj.indexOf("<canal>");
				int fnal = msj.indexOf("</canal>");
				String canal = msj.substring(inicial+7, fnal);
				System.out.println("CANAL:" + canal);
				int inicialC = result.indexOf("<codigoError>");
				int fnalC = result.indexOf("</codigoError>");
				
				String codigo = result.substring(inicialC+13, fnalC);
				System.out.println("codigo" + codigo);
				try {
					LogReentry log = new LogReentry();
					log.setRe_canal(canal);
					log.setRe_codigo(codigo);
					if (codigo.equals("0")) {
						log.setRe_estado("0");
					} else {
						log.setRe_estado("1");
					}
					int inicialM = result.indexOf("<mensajeUsuario>");
					int fnalM = result.indexOf("</mensajeUsuario>");
					String mensaje = result.substring(inicialM + 16 , fnalM);
					System.out.println("mensaje" + mensaje);
					log.setRe_fecha(Calendar.getInstance());
					log.setRe_mensaje(mensaje);
					log.setRe_request(msj);
					log.setRe_response(result);
					LogReentryDAOImpl.insert(log);
					
				} catch (Exception e) {
					// TODO: PENDIENTE GRABAR EN ARCHIVO
				} 
				
				//System.out.println("connectionFactory"+connectionFactory);
				
				
				 Connection connection = connectionFactory.createConnection ();
				 connection.start ();
				 
				Session session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue("OFFLINE_REQ");
				
				System.out.println("Browse through the elements in queue");
				QueueBrowser browser = session.createBrowser(queue);
				@SuppressWarnings("rawtypes")
				Enumeration e = browser.getEnumeration();
				if(!e.hasMoreElements()) {
					listenerContainer.stop();
					System.out.println("No hay mas mensajes en cola");
					System.out.println("JMS Listener stopped.");  
				}				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}