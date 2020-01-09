package top.quyq.batch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.quyq.batch.entity.Player;

import java.util.List;

@Repository
public interface PlayerMapper extends BaseMapper<Player> {

    void insertBatch(@Param("items")List<? extends Player> items);

}
