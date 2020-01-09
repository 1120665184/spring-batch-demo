package top.quyq.batch.batch.falt.step1;

import org.springframework.batch.item.file.transform.LineAggregator;
import top.quyq.batch.entity.Player;

/**
 * 聚合器
 */
public class PlayerLineAggregator implements LineAggregator<Player> {

    private final String SPLIT = ":";

    @Override
    public String aggregate(Player item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getID()).append(SPLIT)
                .append(item.getLastName()).append(SPLIT)
                .append(item.getFirstName()).append(SPLIT)
                .append(item.getPosition()).append(SPLIT)
                .append(item.getBirthYear()).append(SPLIT)
                .append(item.getDebutYear());
        return sb.toString();
    }
}
