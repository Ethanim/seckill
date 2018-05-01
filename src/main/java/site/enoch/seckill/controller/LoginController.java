package site.enoch.seckill.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import site.enoch.seckill.result.Result;
import site.enoch.seckill.service.UserService;
import site.enoch.seckill.vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;

	@RequestMapping("toLogin.do")
	public String toLogin() {
		return "login";
	}
	
	@RequestMapping("login.do")
	@ResponseBody
	public Result<Boolean> login(HttpServletResponse response,@Valid LoginVo loginVo) {
		log.info(loginVo.toString());
		//登录
		userService.login(response, loginVo);
		return Result.success(true);
	}
}
