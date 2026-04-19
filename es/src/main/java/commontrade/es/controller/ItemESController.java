package commontrade.es.controller;

import commontrade.commonyh.pojo.entity.Good;
import commontrade.es.pojo.Type;
import commontrade.es.service.itemESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class ItemESController {

    @Autowired
    private itemESService itemESService;

    @PostMapping("/good")
    public List<Good> getGoods(@RequestBody Type type) {
        return itemESService.getGoods(type.getKeyword(), type.getType());
    }
}
