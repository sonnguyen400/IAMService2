package com.sonnguyen.iamservice2.config;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class StatementInspectorConfig implements StatementInspector {
    @Override
    public String inspect(String sql) {
        System.out.println(sql);
        return sql;
    }
}
