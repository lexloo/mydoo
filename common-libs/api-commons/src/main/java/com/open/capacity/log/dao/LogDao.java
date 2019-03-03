package com.open.capacity.log.dao;

import com.open.capacity.model.log.SysLog;

public interface LogDao {

    // @Insert("insert into sys_log(username, module, params, remark, flag, createTime) values(#{username}, #{module},
    // #{params}, #{remark}, #{flag}, #{createTime})")
    int save(SysLog log);

}
