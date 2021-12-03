package com.syraven.cloud.event;

import com.syraven.cloud.dto.UserDto;
import lombok.Data;

/**
 * @ClassName: UserRegisterEvent
 * @Description: Event 事件对象
 * @Author syrobin
 * @Date 2021-11-23 10:41 上午
 * @Version V1.0
 */
@Data
public class UserRegisterEvent {

    private UserDto userDto;
}
