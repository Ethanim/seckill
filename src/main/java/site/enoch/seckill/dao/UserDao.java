package site.enoch.seckill.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import site.enoch.seckill.entity.User;

@Mapper
public interface UserDao {

	@Select("select * from s_user where id = #{id}")
	public User getById(@Param("id") long id);
}
