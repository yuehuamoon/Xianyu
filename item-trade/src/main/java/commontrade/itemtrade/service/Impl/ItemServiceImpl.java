package commontrade.itemtrade.service.Impl;

import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.entity.Good;
import commontrade.commonyh.pojo.entity.User;
import commontrade.commonyh.pojo.vo.UserVO;
import commontrade.itemtrade.mapper.ItemMapper;
import commontrade.itemtrade.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<Good> selectAll(int page) {
        return itemMapper.selectAll(page);
    }

    @Override
    public GoodDTO selectByID(int id) {
        return itemMapper.selectById(id);
    }
    @Override
    public List<GoodDTO> selectByLabel(String label) {
        return itemMapper.selectByLabel(label);
    }

    @Override
    public List<GoodDTO> search(String keyword){
        return itemMapper.search(keyword);
    }
    @Override
    public int InsertGood(GoodDTO good) {
        if (good.getSellerId() == null){
            return 0;
        }
        return itemMapper.insertGood(good);
    }

    @Override
    public int deleteByID(GoodDTO good){
        return itemMapper.deleteByID(good.getId());
    }
    @Override
    public int updateItemSelective(GoodDTO good){
        return itemMapper.updateItemSelective(good);
    }
    @Override
    public int updateGood(int starus, int id){
        return itemMapper.updateGood(starus, id);
    }
}
