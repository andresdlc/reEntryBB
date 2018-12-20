package bolivariano.offline.reentry.repo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import redis.clients.jedis.Jedis;

@PropertySource("classpath:application.properties")
public class LogReentryDAOImpl implements LogReentryDAO{
	 
	@Value("${redis.server}")
	private String redisServer;	
	
	private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private String user;
    private String pass;
    private String url; 
    private String driver;

	private Jedis jedis;
    
    private void parameterRedis() {
    	jedis = new Jedis(redisServer);
    	this.user=jedis.get("spring.datasource.username");
    	this.pass=jedis.get("spring.datasource.password");
    	this.url=jedis.get("spring.datasource.url");
    	this.driver=jedis.get("spring.datasource.driver");
    }
  	
    public void setDataSource(DataSource dataSource) {
    	DataSourceBuilder builder= DataSourceBuilder.create();
    	  parameterRedis();
    	  builder.url(url);
    	  builder.username(user);
    	  builder.password(pass);
    	  builder.driverClassName(driver);
    	builder.build();
	    this.dataSource = builder.build();
    	//this.dataSource =dataSource;
    }

    public void insert(LogReentry log){
    	        String sql = "insert into dbo.bv_l_reentry" +
    	            " values (?, ?, ?,?,?,?,?)";
    	        System.out.println("SQL : "+sql);
    	     
    	        
    	        try {
					jdbcTemplate = new JdbcTemplate(dataSource);		    
					jdbcTemplate.update(sql, new Object[] { log.getRe_fecha(),log.getRe_canal(), log.getRe_request(), log.getRe_response(), log.getRe_estado(),

						log.getRe_codigo(), log.getRe_mensaje() 
  	
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
    	        
    	        
    	
    	    }

}
