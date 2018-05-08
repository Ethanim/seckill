package site.enoch.seckill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.enoch.seckill.entity.OrderInfo;
import site.enoch.seckill.entity.SeckillOrder;
import site.enoch.seckill.entity.User;
import site.enoch.seckill.redis.RedisService;
import site.enoch.seckill.redis.SeckillKey;
import site.enoch.seckill.vo.GoodsVo;

@Service
public class SeckillService {
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RedisService redisService;

	@Transactional
	public OrderInfo seckill(User user, GoodsVo goods) {
		//减库存
		boolean success = goodsService.reduceStock(goods);
		if(success) {
			//下订单，写入秒杀订单
			//操作 order_info s_order 两张表
			return orderService.createOrder(user, goods);
		}else {
			setGoodsOver(goods.getId());
			return null;
		}
		
	}

	private void setGoodsOver(Long goodsId) {
		redisService.set(SeckillKey.isGoodsOver, ""+goodsId, true);
		
	}

	public long getSeckillResult(Long userId, long goodsId) {
		SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
		if(order != null) {//秒杀成功
			return order.getOrderId();
		}else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}

	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(SeckillKey.isGoodsOver, ""+goodsId);
	}

	public void reset(List<GoodsVo> goodsList) {
		goodsService.resetStock(goodsList);
		orderService.deleteOrders();
	}

}
