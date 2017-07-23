package com.slarfsoft.springtaskcluster.config;

/**
 * @Auther Zhaoyu Chen @ Fuzhou
 * @Date 2017/7/21 0021.
 */
import java.sql.Types;

import org.hibernate.dialect.H2Dialect;

public class FixedH2Dialect extends H2Dialect {

    public FixedH2Dialect() {
        super();
        registerColumnType(Types.FLOAT, "real");
    }
}
