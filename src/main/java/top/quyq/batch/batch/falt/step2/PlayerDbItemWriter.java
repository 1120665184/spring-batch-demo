package top.quyq.batch.batch.falt.step2;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import top.quyq.batch.entity.Player;
import top.quyq.batch.service.IPlayerService;

import java.util.List;

public class PlayerDbItemWriter implements ItemStreamWriter<Player> {

    private IPlayerService playerService;

    public PlayerDbItemWriter(IPlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }

    @Override
    public void write(List<? extends Player> items) throws Exception {
        playerService.insertBatch(items);
    }
}
