package top.quyq.batch.service;

import top.quyq.batch.entity.Player;

import java.util.List;

public interface IPlayerService {

    void insertBatch(List<? extends Player> items);

}
