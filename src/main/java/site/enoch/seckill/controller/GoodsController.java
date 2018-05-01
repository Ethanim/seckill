package site.enoch.seckill.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import site.enoch.seckill.entity.User;
import site.enoch.seckill.redis.RedisService;
import site.enoch.seckill.service.GoodsService;
import site.enoch.seckill.service.UserService;
import site.enoch.seckill.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	private UserService UserService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping(value="toList.do")
	public String list(Model model, User user) {
		model.addAttribute("user", user);
		
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);
		
		return "goods_list";
	}
	
	@RequestMapping(value="toDetail.do/{goodsId}")
	public String detail(Model model, User user, @PathVariable("goodsId")long goodsId) {
		model.addAttribute("user", user);
		
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		
		model.addAttribute("goods", goods);
		
		long startAt = goods.getStartDate().getTime();//开始时间
		long endAt = goods.getEndDate().getTime();//结束时间
		long now = System.currentTimeMillis();//当前时间
		
		int seckillStatus = 0; //秒杀状态
		int remainSeconds = 0; //剩余秒数
		
		if(now < startAt) {//秒杀还没有开始
			seckillStatus = 0;
			remainSeconds = (int) ((startAt - now)/1000);
		}else if(now > endAt) {//秒杀已经结束
			seckillStatus = 2;
			remainSeconds = -1;
		}else {//秒杀进行中
			seckillStatus = 1;
			remainSeconds = 0;
		}
		
		model.addAttribute("seckillStatus", seckillStatus);
		model.addAttribute("remainSeconds", remainSeconds);
		
		return "goods_detail";
	}
}
