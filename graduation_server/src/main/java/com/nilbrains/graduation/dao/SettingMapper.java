package com.nilbrains.graduation.dao;

import com.nilbrains.graduation.pojo.Setting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SettingMapper {

    @Insert("INSERT INTO setting(`key`,`value`) VALUES(#{key},#{value})")
    void saveSetting(Setting setting);

    @Select("SELECT * FROM setting WHERE `key` like #{key}")
    Setting querySettingByKey(String key);

    @Insert("INSERT INTO setting(`key`,`value`) VALUES(#{key},#{value})")
    void saveSettingKeyAndValue(String key, String value);

    @Update("UPDATE `setting` SET `value` = #{value} WHERE `key` = #{key}")
    void updateSetting(Setting setting);
}
