package top.quyq.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {


    @Bean
    protected JobRepository createJobRegistry(DataSource dataSource,
                                              PlatformTransactionManager transactionManager) throws Exception{
        JobRepositoryFactoryBean bean = new JobRepositoryFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTablePrefix("SYSTEM.TEST_");

        bean.setTransactionManager(transactionManager);
        return bean.getObject();
    }


    @Bean
    protected JobLauncher createJobLauncher(JobRepository jobRepository) throws Exception{
        SimpleJobLauncher laucher = new SimpleJobLauncher();

        laucher.setJobRepository(jobRepository);
        laucher.afterPropertiesSet();
        return laucher;
    }

}
