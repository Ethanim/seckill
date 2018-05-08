package site.enoch.seckill.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//import site.enoch.seckill.rabbitmq.MQSender;
import site.enoch.seckill.result.Result;

@Controller
@RequestMapping("/demo")
public class SimpleController {

	//@Autowired
	//private MQSender sender;

	@RequestMapping("/hello")
	@ResponseBody
	public Result<String> home() {
		return Result.success("Hello，world");
	}
	
//	@RequestMapping("/mq/header")
//	@ResponseBody
//	public Result<String> header(){
//		sender.sendHeader("hello,h");
//		return Result.success("hello world");
//	}
//	
//	@RequestMapping("/mq/fanout")
//	@ResponseBody
//	public Result<String> fanout(){
//		sender.sendFanout("hello,fanout");
//		return Result.success("hello world");
//	}
//	
//	@RequestMapping("/mq/topic")
//	@ResponseBody
//	public Result<String> topic(){
//		sender.sendTopic("hello,world");
//		return Result.success("hello topic");
//	}
//
//	@RequestMapping("/mq")
//	@ResponseBody
//	public Result<String> mq() {
//		sender.send("hello mq");
//		return Result.success("hello world");
//	}
}
