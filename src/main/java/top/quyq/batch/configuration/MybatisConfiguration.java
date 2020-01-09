/*
 * Email:        quyongquan@qq.com
 * FileName:     MybatisConfiguration
 * CreationDate: 2019/12/3
 * Author:       渠永泉
 */
package top.quyq.batch.configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Quyq
 * @TODO
 **/
@Configuration
@MapperScan(basePackages = {"top.quyq.batch.mapper*"})
@Slf4j
public class MybatisConfiguration {

    public MybatisConfiguration(){log.info("mybatis plus info...");}

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
