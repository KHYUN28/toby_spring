package com.kkh;

import org.springframework.context.annotation.Import;

@Import(value=SqlServiceContext.class)
public @interface EnableSqlService {
}
