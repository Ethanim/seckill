package site.enoch.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import site.enoch.seckill.entity.User;
import site.enoch.seckill.result.Result;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/info")
	@ResponseBody
	public Result<User> info(Model model, User user){
		return Result.success(user);
	}
}
