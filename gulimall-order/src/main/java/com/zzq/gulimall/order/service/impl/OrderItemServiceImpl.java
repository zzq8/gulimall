package com.zzq.gulimall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbitmq.client.Channel;
import com.zzq.common.utils.PageUtils;
import com.zzq.common.utils.Query;
import com.zzq.gulimall.order.dao.OrderItemDao;
import com.zzq.gulimall.order.entity.OrderItemEntity;
import com.zzq.gulimall.order.entity.OrderReturnReasonEntity;
import com.zzq.gulimall.order.service.OrderItemService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;


@RabbitListener(queues = {"hello-java-queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * queues:声明需要监听的所有队列
     * 三个参数会自动注入：
     * 1）org.springframework.amqp.core.Message
     * 2）T<发送的消息的类型> OrderReturnReasonEntity   就不用我们手动转换（JSONToObject），Spring会帮我们自动转换
     * 3）Channel:当前传输数据的通道
     *
     * Queue: 可以很多人都来监听.只要收到消息,队列删除消息,而且只能有一个收到此消息场景:
     * 1)、订单服务启动多个;同一个消息,只能有一个客户端收到
     * 2)、只有一个消息完全处理完,方法运行结束,我们就可以接收到下一个消息
     */
//    @RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void receiveMessage(Message message, OrderReturnReasonEntity content, Channel channel) {
        //拿到主体内容
        byte[] body = message.getBody();
        //拿到的消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println("接受到的消息...内容" + message + "===内容：" + content);

    }
}