package com.zzq.seckill.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * 1、@EnableScheduling 开启定时任务
 * 2、@Scheduled开启一个定时任务
 * 3、自动配置类 TaskSchedulingAutoConfiguration
 *
 * 异步任务
 * 1、@EnableAsync:开启异步任务
 * 2、@Async：给希望异步执行的方法标注
 * 3、自动配置类 TaskExecutionAutoConfiguration
 *
 * 思考异步编排和异步任务的区别，这种异步任务不好管理好像也是个Executor
 * 觉得可以把他当成异步编排，该配置去yaml配。一种手动写代码一种直接注解到方法
 */

@Slf4j
@Component
@EnableAsync
@EnableScheduling
public class HelloSchedule {

    /**
     * 与Quarz Cron的两点区别：
     * 1、在Spring中表达式是6位组成，不允许第七位的年份
     * 2、在周几的的位置,1-7代表周一到周日  MON-SUN（英文标识也行）
     *
     * 定时任务不该阻塞。（默认是阻塞的）
     * 1）、可以让业务以异步的方式，自己提交到线程池
     *      CompletableFuture.runAsync(() -> {
     *      },execute);
     *
     * 2）、支持定时任务线程池；设置 TaskSchedulingProperties （size 默认是1 所以才会阻塞）
     *      spring.task.scheduling.pool.size: 5  【雷神说这个不好使，有bug】
     *
     * 3）、让定时任务异步执行
     *      异步任务
     *
     * 解决：使用异步任务 + 定时任务来完成定时任务不阻塞的功能
     * 注意：这两者都有其线程池，注意要配置
     */
    @Async
    @Scheduled(cron = "*/5 * * ? * 5")
    public void hello() throws InterruptedException {
        log.info("hello...");
        Thread.sleep(3000);
    }

}
