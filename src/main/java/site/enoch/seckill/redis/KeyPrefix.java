package site.enoch.seckill.redis;

public interface KeyPrefix {
	/**
	 * 获取过期时间
	 * @return
	 */
	public int expireSeconds();

	/**
	 * 获取前缀
	 * @return
	 */
	public String getPrefix();
}
