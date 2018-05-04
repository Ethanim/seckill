package site.enoch.seckill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import site.enoch.seckill.vo.GoodsVo;

@Mapper
public interface GoodsDao {

	@Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date "
			+ "from s_goods sg left join goods g on sg.goods_id=g.id")
	public List<GoodsVo> listGoodsVo();

	@Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date "
			+ "from s_goods sg left join goods g on sg.goods_id=g.id where g.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

	@Update("update s_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
	public void reduceStock(@Param("goodsId") long goodsId);
}
