//package top.quyq.batch.task;
//
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.job.builder.FlowBuilder;
//import org.springframework.batch.core.job.flow.Flow;
//import org.springframework.batch.core.job.flow.FlowExecutionStatus;
//import org.springframework.batch.core.job.flow.JobExecutionDecider;
//import org.springframework.batch.core.job.flow.support.SimpleFlow;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.dao.DeadlockLoserDataAccessException;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
//
//import javax.xml.bind.ValidationException;
//import java.io.FileNotFoundException;
//
//@Configuration
//public class TestBatch {
//
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    //正常的一步一步的执行
//    @Bean(value = "endOfDay")
//    public Job endOfDay() {
//        return this.jobBuilderFactory.get("endOfDay")
//                //start表示开始执行一个 step
//                .start(step1())
//                //next 表示执行下一个步骤 ,如果上一个步骤执行异常则停止。
//                .next(step2())
//                .build();
//    }
//
//    //有条件的执行步骤
//    public Job endOfDay2(){
//        return this.jobBuilderFactory.get("endOfDay2")
//                //执行步骤1 如果 FAILD 则结束 end()表示正常结束 BatchStatus 为 COMPLETED
//                .start(step1()).on("FAILD").end()
//                //执行步骤1 如果 FAILD 则结束 fail() 表示异常 BatchStatus  为 FAILED
//                .start(step1()).on("FAILD").fail()
//                //执行步骤1 如果 FAILD 则停止，BatchStatus 为 STOPPED ，再次执行时从 step2开始
//                .start(step1()).on("FAILD").stopAndRestart(step2())
//                .start(step1())
//                //自定义步骤 的返回结果并根据其结果做出下一步骤
//                .next(new MyDecider()).on(ExitStatus.UNKNOWN.getExitCode()).to(step2())
//                //如果是 COMPLETED WITH SKIPS ，执行  。。。
//                .from(step1()).on("COMPLETED WITH SKIPS").to(step2())
//                //如果是其他结果，执行 。。。
//                .from(step1()).on("*").to(step2()).end()
//                .build();
//    }
//
//    //并行执行作业
//    public Job splitFlowJob(){
//        Flow flow1 =  new FlowBuilder<SimpleFlow>("flow1")
//                .start(step1())
//                .next(step2())
//                .build();
//        Flow flow2 = new FlowBuilder<SimpleFlow>("flow2")
//                .start(step1())
//                .build();
//
//        return this.jobBuilderFactory.get("test")
//                .start(flow1)
//                //表示异步执行
//                .split(new SimpleAsyncTaskExecutor())
//                .add(flow2)
//                .end()
//                .build();
//    }
//
//    @Bean
//    public Step step1() {
//        return this.stepBuilderFactory.get("step1")
//                .tasklet((contribution, chunkContext) -> null)
//                //相同的jobInstance，表示即使上次执行成功，再次执行时任然执行该步骤，默认成功后的步骤下次执行时跳过。
//               // .allowStartIfComplete(true)
//                //同一个jobInstance中,表示该步骤最多可以运行多少次，超出后报 StartLimitExceededException 异常
//                //.startLimit(1)
//                .build();
//    }
//
//    @Bean
//    public Step step2(){
//
//        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
//        //配置事务传播行为
//        // REQUIRED：支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。 
//        // SUPPORTS：支持当前事务，如果当前没有事务，就以非事务方式执行。 
//        // MANDATORY：支持当前事务，如果当前没有事务，就抛出异常。 
//        // REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起。 
//        // NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。 
//        // NEVER：以非事务方式执行，如果当前存在事务，则抛出异常。 
//        // NESTED：支持当前事务，如果当前事务存在，则执行一个嵌套事务，如果当前没有事务，就新建一个事务。 
//        attribute.setPropagationBehavior(Propagation.REQUIRED.value());
//        //隔离级别
//        //未提交读取（Read Uncommitted）：Spring标识：ISOLATION_READ_UNCOMMITTED。允许脏读取，但不允许更新丢失。如果一个事务已经开始写数据，则另外一个事务则不允许同时进行写操作，但允许其他事务读此行数据。该隔离级别可以通过“排他写锁”实现。
//        //已提交读取（Read Committed）：Spring标识：ISOLATION_READ_COMMITTED。允许不可重复读取，但不允许脏读取。这可以通过“瞬间共享读锁”和“排他写锁”实现。读取数据的事务允许其他事务继续访问该行数据，但是未提交的写事务将会禁止其他事务访问该行。
//        //可重复读取（Repeatable Read）：Spring标识：ISOLATION_REPEATABLE_READ。禁止不可重复读取和脏读取，但是有时可能出现幻读数据。这可以通过“共享读锁”和“排他写锁”实现。读取数据的事务将会禁止写事务（但允许读事务），写事务则禁止任何其他事务。
//        //序列化（Serializable）：Spring标识：ISOLATION_SERIALIZABLE。提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
//        //Spring中同时提供一个标识：ISOLATION_DEFAULT。表示使用后端数据库默认的隔离级别。大多数数据库默认的事务隔离级别是Read committed，比如Sql Server , Oracle。MySQL的默认隔离级别是Repeatable read。
//        attribute.setIsolationLevel(Isolation.DEFAULT.value());
//        //设置超时
//        attribute.setTimeout(30);
//
//
//        return this.stepBuilderFactory.get("step2")
//                .<String,String>chunk(10)
//                //.reader()
//                //.processor()
//                //.writer()
//                //表示该步骤允许容错
//                .faultTolerant()
//                //允许跳过的次数
//                .skipLimit(10)
//                //发生指定异常时跳过
//                .skip(Exception.class)
//                //指定不跳过的异常，发生改异常后，步骤执行失败
//                .noSkip(FileNotFoundException.class)
//                //遇到该异常后执行重试
//                .retry(DeadlockLoserDataAccessException.class)
//                //允许重试的次数
//                .retryLimit(3)
//                //默认情况下，无论时重试还是跳过，冲抛出的任何异常都会导致该步骤控制的事务回滚，
//                //如果想要从itemWriter抛出的异常不回滚，则配置一个不应该导致回滚的异常列表
//                .noRollback(ValidationException.class)
//                //如果itemReader构建在事务性资源上，事务回滚后，队列提取的消息会重新放回，因此，步骤可以配置为不缓冲。
//                .readerIsTransactionalQueue()
//                //设置事务
//                .transactionAttribute(attribute)
//                .build();
//    }
//
//
//    public class MyDecider implements JobExecutionDecider {
//        @Override
//        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
//
//            String status;
//            //@TODO 做一些判断
//
//            return new FlowExecutionStatus(ExitStatus.UNKNOWN.getExitCode());
//        }
//    }
//
//}
