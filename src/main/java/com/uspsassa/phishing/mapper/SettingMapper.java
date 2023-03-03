package com.uspsassa.phishing.mapper;

import com.uspsassa.phishing.entity.Setting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingMapper {
    Setting getSetting();

    int updateSetting(Setting setting);

    void addVisitCount();

    void clearVisitCount();
}