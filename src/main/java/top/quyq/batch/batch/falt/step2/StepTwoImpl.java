package top.quyq.batch.batch.falt.step2;

import lombok.Setter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import top.quyq.batch.base.StepBuild;
import top.quyq.batch.entity.Player;
import top.quyq.batch.service.IPlayerService;

/**
 * 步骤二：
 *  从 output.txt 文件中读取值，并解析成 Player对象，
 *  然后保存到数据库中
 */
public class StepTwoImpl implements StepBuild {

    @Setter
    private IPlayerService playerService;

    @Setter
    private StepBuilderFactory stepBuilderFactory;

    @Setter
    private PlatformTransactionManager transactionManager;


    @Override
    public Step getStep() {
        return this.stepBuilderFactory.get("faltStepTwo")
                .transactionManager(transactionManager)
                //指定提交事务之前，执行的步骤
                .chunk(3)
                .reader(createReader())
                .writer(createWriter())
                .build();
    }

    @Override
    public ItemProcessor createProcessor() {
        return null;
    }

    @Override
    public ItemStreamReader createReader() {

        //构建默认的行转换Mapper
        DefaultLineMapper<Player> lineMapper = new DefaultLineMapper<>();
        //配置tokenizer, 该tokenizer默认以 ',' 分割文件内容
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer(":"));
        lineMapper.setFieldSetMapper(new PlayerFieldSetMapper());

        return new FlatFileItemReaderBuilder<Player>()
                .name("flatStepTwoReader")
                .resource(new ClassPathResource("/output.txt"))
                .lineMapper(lineMapper)
                .build();
    }

    @Override
    public ItemStreamWriter createWriter() {
        PlayerDbItemWriter writer = new PlayerDbItemWriter(playerService);
        return writer;
    }
}
