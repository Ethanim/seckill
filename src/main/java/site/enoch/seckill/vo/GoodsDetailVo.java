package site.enoch.seckill.vo;

import site.enoch.seckill.entity.User;

public class GoodsDetailVo {

	private int seckillStatus = 0; // 秒杀状态
	private int remainSeconds = 0; // 剩余秒数
	private User user;
	private GoodsVo goods;

	public int getSeckillStatus() {
		return seckillStatus;
	}

	public void setSeckillStatus(int seckillStatus) {
		this.seckillStatus = seckillStatus;
	}

	public int getRemainSeconds() {
		return remainSeconds;
	}

	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public GoodsVo getGoods() {
		return goods;
	}

	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}

}
