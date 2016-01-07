package ChuangAo.WebSite.repository;

import ChuangAo.WebSite.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FirstDemoRepository extends CrudRepository<Item, String> {
    @Query("SELECT b FROM Item b WHERE b.name like %?1%")
    Iterable<Item> findByTitle(String name);
}
