package ChuangAo.WebSite.service;

import ChuangAo.WebSite.model.Item;
import ChuangAo.WebSite.repository.FirstDemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FirstDemoService {
    @Autowired
    private FirstDemoRepository firstDemoRepository;

    public Iterable<Item> findByTitle(String title) {
        return firstDemoRepository.findByTitle(title);
    }
}
