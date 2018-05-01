package site.enoch.seckill.redis;

public class BasePrefix implements KeyPrefix {
	private int expireSeconds;
	private String prefix;

	public BasePrefix(String prefix) {// 0 代表永不过期
		this(0, prefix);
	}

	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}

	@Override
	public int expireSeconds() {// 默认 0 代表永不过期
		return expireSeconds;
	}

	@Override
	public String getPrefix() {
		String className = this.getClass().getSimpleName();
		return className + ":" + prefix;
	}

}
