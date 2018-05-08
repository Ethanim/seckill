package site.enoch.seckill.redis;

public class GoodsKey extends BasePrefix {

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
	public static GoodsKey getSeckillGoodsStrock = new GoodsKey(0, "gs");

}
