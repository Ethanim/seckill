package site.enoch.seckill.rabbitmq;

import site.enoch.seckill.entity.User;

public class SeckillMessage {
	private User user;
	private long goodsId;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

}
