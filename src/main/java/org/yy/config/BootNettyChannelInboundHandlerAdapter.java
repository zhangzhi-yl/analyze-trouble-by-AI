package org.yy.config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.yy.util.SpringUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ServerEndpoint("/websocket")
@Component
public class BootNettyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception, IOException {
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIp = insocket.getAddress().getHostAddress();
		StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
		stringRedisTemplate.opsForValue().set(clientIp, msg.toString());
		// 2、把信息通过socket传递给给前端
		
		this.sendAll(msg.toString());

	}

	// websocket数据
	private static Map<String, Session> clients = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		
		// 将新用户存入在线的组
		clients.put(session.getId(), session);
	}

	/**
	 * 客户端关闭
	 * 
	 * @param session
	 *            session
	 */
	@OnClose
	public void onClose(Session session) {
		
		// 将掉线的用户移除在线的组里
		clients.remove(session.getId());
	}

	/**
	 * 发生错误
	 * 
	 * @param throwable
	 *            e
	 */
	@OnError
	public void onError(Throwable throwable) {
		
		throwable.printStackTrace();
	}

	/**
	 * 收到客户端发来消息
	 * 
	 * @param message
	 *            消息对象
	 */
	@OnMessage
	public void onMessage(String message) {
	
		this.sendAll(message);
	}

	private void sendAll(String message) {
		for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
			sessionEntry.getValue().getAsyncRemote().sendText(message);
		}
	}

	// websocket数据
	/**
	 * 从客户端收到新的数据、读取完成时调用
	 *
	 * @param ctx
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
	
		ctx.write("OKKK" + "\r\n");
		ctx.flush();
	}

	/**
	 * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
	 *
	 * @param ctx
	 * @param cause
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException {
		
		cause.printStackTrace();
		ctx.close();// 抛出异常，断开与客户端的连接
	}

	/**
	 * 客户端与服务端第一次建立连接时 执行
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception, IOException {
		super.channelActive(ctx);
		ctx.channel().read();
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIp = insocket.getAddress().getHostAddress();
		// 此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
		System.out.println("channelActive:" + clientIp + ctx.name());
	}

	/**
	 * 客户端与服务端 断连时 执行
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
		super.channelInactive(ctx);
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIp = insocket.getAddress().getHostAddress();
		ctx.close(); // 断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
	
	}

	/**
	 * 服务端当read超时, 会调用这个方法
	 *
	 * @param ctx
	 * @param evt
	 * @throws Exception
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception, IOException {
		super.userEventTriggered(ctx, evt);
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIp = insocket.getAddress().getHostAddress();
		ctx.close();// 超时时断开连接
		
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
	
	}

}