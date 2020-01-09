package top.quyq.batch.base;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.InitializingBean;

public abstract class DefaultJobGetter implements JobGetter, InitializingBean {

    private Job job;

    protected abstract Job doGetJob();

    @Override
    public Job getJob() {
        return this.job;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.job = doGetJob();
    }
}
