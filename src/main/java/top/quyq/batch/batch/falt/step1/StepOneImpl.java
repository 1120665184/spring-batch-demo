package top.quyq.batch.batch.falt.step1;

import lombok.Setter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import top.quyq.batch.base.StepBuild;
import top.quyq.batch.entity.Player;

/**
 * 步骤一：
 * 从 input.txt 文件中获取数据，转换成 Player 对象，
 * 以 ':'为分隔符，存储到 output.txt文件中
 */
public class StepOneImpl implements StepBuild {

    @Setter
    private StepBuilderFactory stepBuilderFactory;

    @Setter
    private PlatformTransactionManager transactionManager;

    @Override
    public Step getStep() {
        return this.stepBuilderFactory.get("faltStepOne")
                .allowStartIfComplete(true)
                .transactionManager(transactionManager)
                //指定提交事务之前，执行的步骤
                .chunk(3)
                .reader(createReader())
                .writer(createWriter())
                .processor(createProcessor())
                .build();
    }

    @Override
    public ItemProcessor createProcessor() {
        return new PlayerProcessor();
    }

    @Override
    public ItemStreamReader createReader() {

        //构建默认的行转换Mapper
        DefaultLineMapper<Player> lineMapper = new DefaultLineMapper<>();
        //配置tokenizer, 该tokenizer默认以 ',' 分割文件内容
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("ID","lastName","firstName","position","birthYear","debutYear");
        lineMapper.setLineTokenizer(tokenizer);
        //配置域 和 对象 转换 Mapper
        //lineMapper.setFieldSetMapper(new PlayerFieldSetMapper());
        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(Player.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        //使用 flat读取器
        return new FlatFileItemReaderBuilder<Player>()
                .name("faltStepOneReader")
                //配置读取资源路径
                .resource(new ClassPathResource("/input.txt"))
                //.resource(new FileSystemResource("C:\\Users\\PC\\Desktop\\demo\\batch_test\\src\\main\\java\\top\\quyq\\batch\\batch\\falt\\step1\\input.txt"))
                //跳到指定行
                .linesToSkip(1)
                //配置行 解析器
                .lineMapper(lineMapper)
                .build();
    }

    @Override
    public ItemStreamWriter createWriter() {
        return new FlatFileItemWriterBuilder<Player>()
                .name("faltStepOneWriter")
                //如果存在则删除，设置为否，默认 为是
                //.shouldDeleteIfExists(false)
                //设置为追加
                //.append(true)
                .resource(new ClassPathResource("/output.txt"))
                //.resource(new FileSystemResource("C:\\Users\\PC\\Desktop\\demo\\batch_test\\src\\main\\java\\top\\quyq\\batch\\batch\\falt\\step1\\output.txt"))
                .lineAggregator(new PlayerLineAggregator())
                .build();
    }


    public class PlayerProcessor implements ItemProcessor<Player , Player >{

        @Override
        public Player process(Player item) throws Exception {
            return item;
        }
    }

}
