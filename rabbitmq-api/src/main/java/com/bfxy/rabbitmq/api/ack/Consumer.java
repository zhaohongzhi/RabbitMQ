package com.bfxy.rabbitmq.api.ack;

import com.bfxy.rabbitmq.api.util.ConnectionUtil;
import com.rabbitmq.client.*;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import java.io.IOException;

public class Consumer {

	/**
	 * 消息的ACK和Unack机制，必须关闭自动ACK,进行手动ACK
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		Connection connection = ConnectionUtil.getConnection();
		Channel channel = connection.createChannel();
		
		
		String exchangeName = "test_ack_exchange";
		String queueName = "test_ack_queue";
		String routingKey = "ack.#";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		// 手工签收 必须要关闭 autoAck = false 第二个参数设置为false
		channel.basicConsume(queueName, false, new MyConsumer(channel));

	}
}
