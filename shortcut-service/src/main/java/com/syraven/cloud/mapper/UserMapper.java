package com.syraven.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syraven.cloud.domain.UserRoot;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-29 16:02
 */
@Mapper
public interface UserMapper extends BaseMapper<UserRoot> {
}
