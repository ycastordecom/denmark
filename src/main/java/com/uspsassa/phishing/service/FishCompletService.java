package com.uspsassa.phishing.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.uspsassa.phishing.entity.FishComplet;
import com.uspsassa.phishing.mapper.FishCompletMapper;import java.util.List;

@Service
public class FishCompletService {

    @Resource
    private FishCompletMapper fishCompletMapper;


    public int deleteByPrimaryKey(Integer id) {
        return fishCompletMapper.deleteByPrimaryKey(id);
    }


    public int insert(FishComplet record) {
        return fishCompletMapper.insert(record);
    }


    public int insertSelective(FishComplet record) {
        return fishCompletMapper.insertSelective(record);
    }

    public List<FishComplet> findByAll(FishComplet fishComplet) {
        return fishCompletMapper.findByAll(fishComplet);
    }

    public FishComplet findOneByAll(FishComplet fishComplet) {
        return fishCompletMapper.findOneByAll(fishComplet);
    }

    public FishComplet selectByPrimaryKey(Integer id) {
        return fishCompletMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(FishComplet record) {
        return fishCompletMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(FishComplet record) {
        return fishCompletMapper.updateByPrimaryKey(record);
    }

    public Integer count() {
        return fishCompletMapper.count();
    }
}


