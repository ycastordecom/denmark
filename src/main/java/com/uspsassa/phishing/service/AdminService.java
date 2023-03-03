package com.uspsassa.phishing.service;

import cn.hutool.core.convert.Convert;
import com.uspsassa.phishing.common.utils.TokenUtil;
import com.uspsassa.phishing.entity.Fish;
import com.uspsassa.phishing.exception.BusinessException;
import com.uspsassa.phishing.request.admin.LoginRequest;
import com.uspsassa.phishing.vo.AdminVo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.uspsassa.phishing.mapper.AdminMapper;
import com.uspsassa.phishing.entity.Admin;import java.util.List;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;


    public int deleteByPrimaryKey(Integer id) {
        return adminMapper.deleteByPrimaryKey(id);
    }


    public int insert(Admin record) {
        return adminMapper.insert(record);
    }


    public int insertSelective(Admin record) {
        return adminMapper.insertSelective(record);
    }


    public Admin selectByPrimaryKey(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(Admin record) {
        return adminMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(Admin record) {
        return adminMapper.updateByPrimaryKey(record);
    }

    public AdminVo login(LoginRequest loginRequest) {
        Admin convert = Convert.convert(Admin.class, loginRequest);
        Admin admin = adminMapper.findOneByAll(convert);
        if (admin == null) {
            throw new BusinessException("用户名或密码错误") ;
        }
        AdminVo adminVo = Convert.convert(AdminVo.class, admin);
        String token = TokenUtil.getToken(adminVo);
        adminVo.setToken(token);
        return adminVo;
    }

    public List<Admin> findByAll(Admin admin) {
        return adminMapper.findByAll(admin);
    }

    public Admin findOneByAll(Admin admin) {
        return adminMapper.findOneByAll(admin);
    }

}

