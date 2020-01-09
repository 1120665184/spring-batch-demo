package top.quyq.batch.batch.falt;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import top.quyq.batch.base.DefaultJobGetter;
import top.quyq.batch.batch.falt.step1.StepOneImpl;
import top.quyq.batch.batch.falt.step2.StepTwoImpl;
import top.quyq.batch.service.IPlayerService;

@Service("jobFlatTest")
public class FaltJobExampleImpl extends DefaultJobGetter {


    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private IPlayerService playerService;

    @Override
    protected Job doGetJob() {
        StepOneImpl stepOne = new StepOneImpl();
        stepOne.setStepBuilderFactory(stepBuilderFactory);
        stepOne.setTransactionManager(transactionManager);

        StepTwoImpl stepTwo = new StepTwoImpl();
        stepTwo.setStepBuilderFactory(stepBuilderFactory);
        stepTwo.setTransactionManager(transactionManager);
        stepTwo.setPlayerService(playerService);

        return jobBuilderFactory.get("flatTest")
                .start(stepOne.getStep())
                .next(stepTwo.getStep())
                .build();
    }


}
