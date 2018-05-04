package site.enoch.seckill.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import site.enoch.seckill.entity.User;
import site.enoch.seckill.redis.GoodsKey;
import site.enoch.seckill.redis.RedisService;
import site.enoch.seckill.result.Result;
import site.enoch.seckill.service.GoodsService;
import site.enoch.seckill.vo.GoodsDetailVo;
import site.enoch.seckill.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;

	@RequestMapping(value = "toList.do", produces = "text/html")
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
		model.addAttribute("user", user);

		// 取缓存
		String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
		if (!StringUtils.isEmpty(html)) {
			return html;
		}

		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		model.addAttribute("goodsList", goodsList);

		// return "goods_list";

		SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(),
				model.asMap(), applicationContext);
		// 手动渲染
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsList, "", html);
		}
		return html;
	}
	
	@RequestMapping(value = "/detail/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user,
			@PathVariable("goodsId") long goodsId) {
		
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

		long startAt = goods.getStartDate().getTime();// 开始时间
		long endAt = goods.getEndDate().getTime();// 结束时间
		long now = System.currentTimeMillis();// 当前时间

		int seckillStatus = 0; // 秒杀状态
		int remainSeconds = 0; // 剩余秒数

		if (now < startAt) {// 秒杀还没有开始
			seckillStatus = 0;
			remainSeconds = (int) ((startAt - now) / 1000);
		} else if (now > endAt) {// 秒杀已经结束
			seckillStatus = 2;
			remainSeconds = -1;
		} else {// 秒杀进行中
			seckillStatus = 1;
			remainSeconds = 0;
		}
		
		GoodsDetailVo vo = new GoodsDetailVo();
		vo.setGoods(goods);
		vo.setUser(user);
		vo.setSeckillStatus(seckillStatus);
		vo.setRemainSeconds(remainSeconds);
		
		return Result.success(vo); 
	}

	@RequestMapping(value = "toDetail2.do/{goodsId}", produces = "text/html")
	@ResponseBody
	public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, User user,
			@PathVariable("goodsId") long goodsId) {
		model.addAttribute("user", user);
		
		//取缓存
		String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
		if(!StringUtils.isEmpty(html)) {
			return html;
		}

		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

		model.addAttribute("goods", goods);

		long startAt = goods.getStartDate().getTime();// 开始时间
		long endAt = goods.getEndDate().getTime();// 结束时间
		long now = System.currentTimeMillis();// 当前时间

		int seckillStatus = 0; // 秒杀状态
		int remainSeconds = 0; // 剩余秒数

		if (now < startAt) {// 秒杀还没有开始
			seckillStatus = 0;
			remainSeconds = (int) ((startAt - now) / 1000);
		} else if (now > endAt) {// 秒杀已经结束
			seckillStatus = 2;
			remainSeconds = -1;
		} else {// 秒杀进行中
			seckillStatus = 1;
			remainSeconds = 0;
		}

		model.addAttribute("seckillStatus", seckillStatus);
		model.addAttribute("remainSeconds", remainSeconds);

		//return "goods_detail";
		
		SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(),
				model.asMap(), applicationContext);
		//手动渲染
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
		if (!StringUtils.isEmpty(html)) {
			redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
		}
		return html;
	}
}
