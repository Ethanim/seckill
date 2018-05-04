package site.enoch.seckill.vo;

import site.enoch.seckill.entity.Goods;
import site.enoch.seckill.entity.OrderInfo;

public class OrderDetailVo {
	private Goods goods;
	private OrderInfo order;

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}

}
