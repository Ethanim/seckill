package site.enoch.seckill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.enoch.seckill.entity.OrderInfo;
import site.enoch.seckill.entity.User;
import site.enoch.seckill.vo.GoodsVo;

@Service
public class SeckillService {
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private OrderService orderService;

	@Transactional
	public OrderInfo seckill(User user, GoodsVo goods) {
		//减库存
		goodsService.reduceStock(goods);
		
		//下订单，写入秒杀订单
		//操作 order_info s_order 两张表
		OrderInfo orderInfo = orderService.createOrder(user, goods);
		return orderInfo;
	}

}
