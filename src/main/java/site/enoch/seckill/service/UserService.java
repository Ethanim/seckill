package site.enoch.seckill.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.enoch.seckill.dao.UserDao;
import site.enoch.seckill.entity.User;
import site.enoch.seckill.exception.GlobalException;
import site.enoch.seckill.redis.RedisService;
import site.enoch.seckill.redis.UserKey;
import site.enoch.seckill.result.CodeMsg;
import site.enoch.seckill.util.MD5Util;
import site.enoch.seckill.util.UUIDUtil;
import site.enoch.seckill.vo.LoginVo;

@Service
public class UserService {
	
	public static final String COOKIE_NAME_TOKEN = "token";

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RedisService redisService;
	
	public User getById(long id) {
		return userDao.getById(id);
	}
	
	public User getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		User user = redisService.get(UserKey.token, token, User.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}

	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		User user = getById(Long.valueOf(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成 cookie
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
		return true;
	}
	
	private void addCookie(HttpServletResponse response, String token, User user) {
		redisService.set(UserKey.token, token, user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
