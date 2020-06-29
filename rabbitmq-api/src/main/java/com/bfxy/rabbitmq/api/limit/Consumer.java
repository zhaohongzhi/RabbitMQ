package com.bfxy.rabbitmq.api.limit;

import com.bfxy.rabbitmq.api.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {

	
	public static void main(String[] args) throws Exception {
		
		Connection connection = ConnectionUtil.getConnection();
		Channel channel = connection.createChannel();
		
		
		String exchangeName = "test_qos_exchange";
		String queueName = "test_qos_queue";
		String routingKey = "qos.#";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		//1 限流方式  第一件事就是 autoAck设置为 false

		//1.int prefetchSize   2.int prefetchCount 3.boolean global  参数的含义：
		// prefetchCount：当消息达到该数量没有ack
		//确认时，consumer会阻塞，直到有消息进行ack  global：是channel层面还是cosumer层面的设置
		channel.basicQos(0, 1, false);

		channel.basicConsume(queueName, false, new MyConsumer(channel));
		
		
	}
}
