package com.bfxy.rabbitmq.api.confirm;

import java.io.IOException;

import com.bfxy.rabbitmq.api.util.ConnectionUtil;
import com.rabbitmq.client.*;

public class Producer {

	/**
	 * 消息的confirm机制，当消费消费完成后，生产者会进行Confirm监听，监听消息的消费状态并进行下一步处理
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		

		//2 获取C	onnection
		Connection connection = ConnectionUtil.getConnection();
		//Connection connection = ConnectionUtil.getConnection("192.168.146.251",5672,"/","guest","guest");
		
		//3 通过Connection创建一个新的Channel
		Channel channel = connection.createChannel();
		
		
		//4 指定我们的消息投递模式: 消息的确认模式 
		channel.confirmSelect();
		
		String exchangeName = "test_confirm_exchange";
		String routingKey = "confirm.save";
		
		//5 发送一条消息
		String msg = "Hello RabbitMQ Send confirm message!";
		channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
		
		//6 添加一个确认监听
		channel.addConfirmListener(new ConfirmListener() {
			//确认消息消费
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {

				System.err.println(deliveryTag+"--confirm确认消息-----no ack!-----------");
			}
			//消息没有消费
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.err.println("---confirm确认消息----ack!-----------");
			}
		});


		//添加returnListener
		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {

			}
		});

		//添加shutdownListener
		channel.addShutdownListener(new ShutdownListener() {
			@Override
			public void shutdownCompleted(ShutdownSignalException e) {

			}
		});
	}
}
