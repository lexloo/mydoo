package com.open.capacity.autoconfigure.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

import com.open.capacity.annotation.datasource.DataSource;
import com.open.capacity.db.config.util.DataSourceHolder;
import com.open.capacity.db.config.util.DataSourceKey;

import lombok.extern.slf4j.Slf4j;

/**
 * 切换数据源Advice
 */
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
@Slf4j
public class DataSourceAspect {
    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, DataSource ds) throws Throwable {
        String dsId = ds.name();
        try {
            DataSourceKey dataSourceKey = DataSourceKey.valueOf(dsId);
            DataSourceHolder.setDataSourceKey(dataSourceKey);
        } catch (Exception e) {
            log.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        }


    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DataSource ds) {
        log.debug("Revert DataSource : {transIdo} > {}", ds.name(), point.getSignature());
        DataSourceHolder.clearDataSourceKey();
    }

}