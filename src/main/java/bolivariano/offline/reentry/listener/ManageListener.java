package bolivariano.offline.reentry.listener;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.Jedis;



@RestController
@PropertySource("classpath:application.properties")
public class ManageListener {
	static final Logger logger = Logger.getLogger(ManageListener.class);
	
	@Value("${redis.server}")
	private String redisServer;
	
	@Resource(name="jmsContainer")  
	private DefaultMessageListenerContainer listenerContainer;  
	
	@RequestMapping(value="disableListener", method = RequestMethod.GET)  
	@ResponseBody
	public String disableListener(ModelMap model, HttpSession session) {  
	    listenerContainer.stop(new Runnable() {  
	        public void run() {  
	        	System.out.println("JMS Listener stopped.");  
	            logger.log(Level.DEBUG, "JMS Listener stopped" );
	        }   
	    });  
	    
	    @SuppressWarnings("resource")
		Jedis jedis = new Jedis(redisServer);
		//String modoArbitroKeyName = context.resolvePropertyPlaceholders("{{redis.keyNameModoArbitro}}");
		jedis.set("arbitroAllow","false");
		
	    return "Listener Reentry Abajo";  
	}
	
	@RequestMapping(value="enableListener", method = RequestMethod.GET)  
	public String enableListener(ModelMap model, HttpSession session) {  
		
	    listenerContainer.start();	  
	    System.out.println("JMS Listener started.");	
	    logger.log(Level.DEBUG, "JMS Listener started" );
	    @SuppressWarnings("resource")
		Jedis jedis = new Jedis(redisServer);
		//String modoArbitroKeyName = context.resolvePropertyPlaceholders("{{redis.keyNameModoArbitro}}");
		jedis.set("arbitroAllow","true");
		
	    return "Listener Reentry Arriba"; 
	    
	} 
}

