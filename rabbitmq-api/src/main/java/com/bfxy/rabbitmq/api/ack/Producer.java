package com.bfxy.rabbitmq.api.ack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.bfxy.rabbitmq.api.util.ConnectionUtil;
import com.rabbitmq.client.*;

public class Producer {

	/**
	 * 消息的ack和unack机制，以及在unack的情况下重回队列
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Connection connection = ConnectionUtil.getConnection();

		Channel channel = connection.createChannel();
		
		String exchange = "test_ack_exchange";
		String routingKey = "ack.save";
		
		
		
		for(int i =0; i<5; i ++){
			
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("num", i);
			
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.headers(headers)
					.build();
			String msg = "Hello RabbitMQ ACK Message " + i;
			channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes());
			channel.addConfirmListener(new ConfirmListener() {
				@Override
				public void handleAck(long l, boolean b) throws IOException {
					System.out.println("===消息ID===="+l+"====消费成功===");
				}

				@Override
				public void handleNack(long l, boolean b) throws IOException {
					System.out.println("===消息ID===="+l+"====消费失败===");
				}
			});
		}
		
	}
}
