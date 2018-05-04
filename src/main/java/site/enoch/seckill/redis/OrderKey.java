package site.enoch.seckill.redis;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey getSeckillOrderByUidGid = new OrderKey("soug");
}
