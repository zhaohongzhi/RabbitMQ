package com.bfxy.rabbitmq.api.returnlistener;

import java.io.IOException;

import com.bfxy.rabbitmq.api.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Producer {

	
	public static void main(String[] args) throws Exception {
		
		

		
		Connection connection = ConnectionUtil.getConnection();
		Channel channel = connection.createChannel();
		
		String exchange = "test_return_exchange";
		String routingKey = "return.save";
		String routingKeyError = "abc.save";
		
		String msg = "Hello RabbitMQ Return Message";
		//添加不可路由的消息的队列
		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange,
									 String routingKeyError, AMQP.BasicProperties properties, byte[] body) throws IOException {

				System.err.println("---------handle  return----------");
				System.err.println("replyCode: " + replyCode);
				System.err.println("replyText: " + replyText);
				System.err.println("exchange: " + exchange);
				System.err.println("routingKey: " + routingKeyError);
				System.err.println("properties: " + properties);
				System.err.println("body: " + new String(body));
			}
		});
		channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

	}
}
