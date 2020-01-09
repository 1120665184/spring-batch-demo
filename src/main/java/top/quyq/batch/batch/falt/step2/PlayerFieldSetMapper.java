package top.quyq.batch.batch.falt.step2;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import top.quyq.batch.entity.*;

/**
 * set 和对象转换
 */
public class PlayerFieldSetMapper implements FieldSetMapper<Player> {
    @Override
    public Player mapFieldSet(FieldSet fieldSet) throws BindException {

        if(fieldSet == null){
            return  null;
        }

        Player player = new Player();
        player.setID(fieldSet.readString(0))
                .setLastName(fieldSet.readString(1))
                .setFirstName(fieldSet.readString(2))
                .setPosition(fieldSet.readString(3))
                .setBirthYear(fieldSet.readInt(4))
                .setDebutYear(fieldSet.readInt(5));


        return player;
    }
}
