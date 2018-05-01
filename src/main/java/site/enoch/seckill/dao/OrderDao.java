package site.enoch.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import site.enoch.seckill.entity.OrderInfo;
import site.enoch.seckill.entity.SeckillOrder;

@Mapper
public interface OrderDao {

	@Select("select * from s_order where user_id = #{userId} and goods_id = #{goodsId}")
	public SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

	@Insert("insert into order_info (user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date) "
			+ "values(#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
	@SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
	public long insert(OrderInfo orderInfo);

	@Insert("insert into s_order (user_id,order_id,goods_id) values(#{userId},#{orderId},#{goodsId})")
	public void insertSeckillOrder(SeckillOrder seckillOrder);

}
