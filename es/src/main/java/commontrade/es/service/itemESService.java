package commontrade.es.service;

import commontrade.commonyh.pojo.entity.Good;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface itemESService {
    List<Good> getGoods(String keyword, String table);
}
