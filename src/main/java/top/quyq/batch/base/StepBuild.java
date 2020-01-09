package top.quyq.batch.base;

import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;


public interface StepBuild {

    Step getStep();

    ItemProcessor createProcessor();

    ItemStreamReader createReader();

    ItemStreamWriter createWriter();

}
