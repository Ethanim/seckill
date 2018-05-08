package site.enoch.seckill.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.enoch.seckill.entity.OrderInfo;
import site.enoch.seckill.entity.SeckillOrder;
import site.enoch.seckill.entity.User;
import site.enoch.seckill.redis.RedisService;
import site.enoch.seckill.redis.SeckillKey;
import site.enoch.seckill.util.MD5Util;
import site.enoch.seckill.util.UUIDUtil;
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

	public String createSeckillPath(User user, long goodsId) {
		if(user == null || goodsId <= 0) {
			return null;
		}
		String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
		redisService.set(SeckillKey.getSeckillPath, ""+user.getId()+"_"+goodsId, str);
		return str;
	}

	public boolean checkPath(User user, long goodsId, String path) {
		if(user== null || path == null) {
			return false;
		}
		String pathOld = redisService.get(SeckillKey.getSeckillPath, ""+user.getId()+"_"+goodsId, String.class);
		return path.equals(pathOld);
	}

	public BufferedImage createVerifyCode(User user, long goodsId) {
		if(user == null || goodsId <= 0) {
			return null;
		}
		int width = 80;
		int height = 32;
		//create the image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calc(verifyCode);
		redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+"_"+goodsId, rnd);
		//输出图片	
		return image;
	}
	
	private static char[] ops = new char[]{'+', '-', '*'};
	/**
	 * @param rdm
	 * @return
	 */
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
		int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		String exp = "" + num1 + op1 + num2 + op2 + num3;
		return exp;
	}

	/**
	 * 利用 js 引擎计算结果
	 * @param verifyCode
	 * @return
	 */
	private int calc(String exp) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		try {
			return (Integer) engine.eval(exp);
		} catch (ScriptException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
		if(user == null || goodsId <= 0) {
			return false;
		}
		Integer codeOld = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId()+"_"+goodsId, Integer.class);
		if(codeOld == null || codeOld - verifyCode != 0) {
			return false;
		}
		redisService.delete(SeckillKey.getSeckillVerifyCode, user.getId()+"_"+goodsId);
		return true;
	}

}
