package com.bbs.remark.dao;


import com.bbs.remark.dto.AdminUserDTO;
import com.bbs.remark.dto.UserSearchDTO;
import com.bbs.remark.pojo.BaseAdminUser;
import tk.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseAdminUserMapper extends MyMapper<BaseAdminUser> {

    List<AdminUserDTO> getUserList(UserSearchDTO userSearchDTO);

    BaseAdminUser getUserByUserName(@Param("sysUserName")String sysUserName,@Param("id") Integer id);

    int updateUserStatus(@Param("id") Integer id,@Param("status") Integer status);

    int updateUser(BaseAdminUser user);

    BaseAdminUser findByUserName(@Param("userName") String userName);

    int updatePwd(@Param("userName") String userName,@Param("password") String password);

}