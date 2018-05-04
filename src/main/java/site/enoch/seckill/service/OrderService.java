package site.enoch.seckill.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.enoch.seckill.dao.OrderDao;
import site.enoch.seckill.entity.OrderInfo;
import site.enoch.seckill.entity.SeckillOrder;
import site.enoch.seckill.entity.User;
import site.enoch.seckill.redis.OrderKey;
import site.enoch.seckill.redis.RedisService;
import site.enoch.seckill.vo.GoodsVo;

@Service
public class OrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private RedisService redisService;

	public SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, long goodsId) {
		//return orderDao.getSeckillOrderByUserIdGoodsId(userId, goodsId);
		return redisService.get(OrderKey.getSeckillOrderByUidGid, ""+userId+"_"+goodsId, SeckillOrder.class);
	}

	public OrderInfo createOrder(User user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getSeckillPrice());
		orderInfo.setOrderChannel(1);//1在线下单
		orderInfo.setStatus(0);//0未支付
		orderInfo.setUserId(user.getId());
		
		long orderId = orderDao.insert(orderInfo);
		
		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setGoodsId(goods.getId());
		seckillOrder.setOrderId(orderId);
		seckillOrder.setUserId(user.getId());
		
		orderDao.insertSeckillOrder(seckillOrder);
		
		//放入 Redis
		redisService.set(OrderKey.getSeckillOrderByUidGid, ""+user.getId()+"_"+goods.getId(), seckillOrder);

		return orderInfo;
	}

	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}


}
