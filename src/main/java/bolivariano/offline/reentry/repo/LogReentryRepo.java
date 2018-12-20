package bolivariano.offline.reentry.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogReentryRepo extends CrudRepository<LogReentry, Long> {
	
 
	

}
