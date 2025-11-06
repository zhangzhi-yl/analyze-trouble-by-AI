package org.yy;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 说明：启动类 
 * 作者：YY 356703572
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)//去除冲突 
@MapperScan("org.yy.mapper")
@EnableCaching
public class MP03Application implements CommandLineRunner  {

	public static void main(String[] args) {
		SpringApplication.run(MP03Application.class, args);
	}
	@Async
    @Override
    public void run(String... args) throws Exception {
//		new BootNettyServer().bind(8073);
    }
	
	@Primary
	@Bean
	public TaskExecutor primaryTaskExecutor() {
	    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	    return executor;
	}
}