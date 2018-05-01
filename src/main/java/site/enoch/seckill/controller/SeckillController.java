package site.enoch.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.enoch.seckill.entity.OrderInfo;
import site.enoch.seckill.entity.SeckillOrder;
import site.enoch.seckill.entity.User;
import site.enoch.seckill.result.CodeMsg;
import site.enoch.seckill.service.GoodsService;
import site.enoch.seckill.service.OrderService;
import site.enoch.seckill.service.SeckillService;
import site.enoch.seckill.vo.GoodsVo;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SeckillService seckillService;

	@RequestMapping(value="/seckill.do")
	public String list(Model model, User user, @RequestParam("goodsId") long goodsId) {
		
		//判断用户是否登录
		if(user == null) {
			return "login";
		}
		
		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		Integer stock = goods.getStockCount();
		if(stock <= 0) {
			model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
			return "seckill_fail";
		}
		
		//判断是否已经秒杀到了
		SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			model.addAttribute("errmsg", CodeMsg.REPEATE_SECKILL.getMsg());
			return "seckill_fail";
		}
		//减库存，下订单，写入秒杀订单
		OrderInfo orderInfo = seckillService.seckill(user, goods);
		
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("goods", goods);
		return "order_detail";
	}
}
