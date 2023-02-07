package com.zzq.gulimall.order;

import com.zzq.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallOrderApplicaitonTest {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void createExchange(){

        DirectExchange directExchange = new DirectExchange("hello-java-exchange");
        amqpAdmin.declareExchange(directExchange);

        log.info("directExchange[{}]创建成功：","hello-java-exchange");
    }


    @Test
    public void testCreateQueue() {
        Queue queue = new Queue("hello-java-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功：","hello-java-queue");
    }


    @Test
    public void createBinding() {

        Binding binding = new Binding("hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-exchange",
                "hello.java",
                null);
        amqpAdmin.declareBinding(binding);
        log.info("Binding[{}]创建成功：","hello-java-binding");

    }

    /**
     * 发消息：也可以发对象     万物皆可序列化 :)
     */
    @Test
    public void sendMessageTest(){
        OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
        reasonEntity.setId(1L);
        reasonEntity.setCreateTime(new Date());
        reasonEntity.setName("reason");
        reasonEntity.setStatus(1);
        reasonEntity.setSort(2);
        String msg = "Hello World";
        //1、发送消息,如果发送的消息是个对象，会使用序列化机制，将对象写出去，对象必须实现Serializable接口

        //2、发送的对象类型的消息，可以是一个json
        rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",reasonEntity);
        log.info("消息发送完成:{}",reasonEntity);
    }

}
