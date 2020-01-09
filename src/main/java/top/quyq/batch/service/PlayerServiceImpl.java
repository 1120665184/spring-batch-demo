package top.quyq.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.quyq.batch.entity.Player;
import top.quyq.batch.mapper.PlayerMapper;

import java.util.List;

@Service
public class PlayerServiceImpl implements IPlayerService {

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public void insertBatch(List<? extends Player> items) {
        playerMapper.insertBatch(items);
    }
}
