package commontrade.es.service.Impl;

import commontrade.commonyh.pojo.entity.Good;
import commontrade.es.esMapper.ItemESMapper;
import commontrade.es.service.itemESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class itemESServiceImpl implements itemESService {
    @Autowired
    private ItemESMapper itemESMapper;

    @Override
    public List<Good> getGoods(String keyword, String table) {
        return itemESMapper.searchByKeyword(keyword, table);
    }

}
