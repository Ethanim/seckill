package site.enoch.seckill.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.enoch.seckill.entity.SeckillOrder;
import site.enoch.seckill.entity.User;
import site.enoch.seckill.rabbitmq.MQSender;
import site.enoch.seckill.rabbitmq.SeckillMessage;
import site.enoch.seckill.redis.GoodsKey;
import site.enoch.seckill.redis.OrderKey;
import site.enoch.seckill.redis.RedisService;
import site.enoch.seckill.redis.SeckillKey;
import site.enoch.seckill.result.CodeMsg;
import site.enoch.seckill.result.Result;
import site.enoch.seckill.service.GoodsService;
import site.enoch.seckill.service.OrderService;
import site.enoch.seckill.service.SeckillService;
import site.enoch.seckill.vo.GoodsVo;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements  InitializingBean{
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SeckillService seckillService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private MQSender sender;
	
	private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

	/**
	 * 系统初始化，把秒杀商品库存，加载到 Redis 里
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		if(goodsList == null) {
			return;
		}
		for(GoodsVo goods : goodsList) {
			redisService.set(GoodsKey.getSeckillGoodsStrock, ""+goods.getId(), goods.getStockCount());
			localOverMap.put(goods.getId(), false);
		}
	}
	
	@RequestMapping(value="/seckill.do", method=RequestMethod.POST)
	@ResponseBody
	public Result<Integer> list(Model model, User user, @RequestParam("goodsId") long goodsId) {
		
		//判断用户是否登录
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		
		// 内存标记，减少 redis 访问
		Boolean over = localOverMap.get(goodsId);
		if(over) {
			return Result.error(CodeMsg.SECKILL_OVER);
		}
		//预减库存
		Long stock = redisService.decr(GoodsKey.getSeckillGoodsStrock, ""+goodsId);
		if(stock < 0) {
			localOverMap.put(goodsId, true);
			return Result.error(CodeMsg.SECKILL_OVER);
		}
		//判断是否已经秒杀到了
		SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_SECKILL);
		}
		// 入队
		SeckillMessage sm = new SeckillMessage();
		sm.setUser(user);
		sm.setGoodsId(goodsId);
		sender.sendSeckillMessage(sm);
		return Result.success(0);//排队中
		
		/*//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		Integer stock = goods.getStockCount();
		if(stock <= 0) {
			model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
			return Result.error(CodeMsg.SECKILL_OVER);
		}
		
		//判断是否已经秒杀到了
		SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			model.addAttribute("errmsg", CodeMsg.REPEATE_SECKILL.getMsg());
			return Result.error(CodeMsg.REPEATE_SECKILL);
		}
		//减库存，下订单，写入秒杀订单
		OrderInfo orderInfo = seckillService.seckill(user, goods);
		
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("goods", goods);
		return Result.success(orderInfo);*/
	}

	/**
	 * orderId: 成功
	 * -1: 秒杀失败
	 * 0: 排队中
	 * 
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value="/result", method=RequestMethod.GET)
	@ResponseBody
	public Result<Long> SeckillResult(Model model, User user, @RequestParam("goodsId") long goodsId){
		model.addAttribute("user", user);
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		long result = seckillService.getSeckillResult(user.getId(), goodsId);
		return Result.success(result);
		
	}

	@RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		for(GoodsVo goods : goodsList) {
			goods.setStockCount(10);
			redisService.set(GoodsKey.getSeckillGoodsStrock, ""+goods.getId(), 10);
			localOverMap.put(goods.getId(), false);
		}
		redisService.delete(OrderKey.getSeckillOrderByUidGid);
		redisService.delete(SeckillKey.isGoodsOver);
		seckillService.reset(goodsList);
		return Result.success(true);
	}
}
