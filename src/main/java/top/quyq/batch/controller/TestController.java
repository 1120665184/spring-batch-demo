package top.quyq.batch.controller;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.quyq.batch.base.JobGetter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("jobFlatTest")
    private JobGetter faltJobExample;

    @GetMapping("/flatTest")
    public void handle() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("nowDate",new JobParameter(new Date()));

        jobLauncher.run(faltJobExample.getJob(),new JobParameters(parameters));
    }

}
