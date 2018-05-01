package site.enoch.seckill.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.enoch.seckill.dao.GoodsDao;
import site.enoch.seckill.vo.GoodsVo;

@Service
public class GoodsService {
	
	@Autowired
	private GoodsDao goodsDao;

	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	public void reduceStock(GoodsVo goods) {
		goodsDao.reduceStock(goods.getId());
	}
}
