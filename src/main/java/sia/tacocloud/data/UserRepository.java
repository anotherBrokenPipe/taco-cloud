package sia.tacocloud.data;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.TacoCloudUser;

public interface UserRepository extends CrudRepository<TacoCloudUser, Long> {

    TacoCloudUser findByUsername(String username);

}
