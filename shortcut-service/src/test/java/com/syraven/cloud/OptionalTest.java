package com.syraven.cloud;

import com.syraven.cloud.domin.User;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author syrobin
 * @version v1.0
 * @description: Optiona类测试
 * @date 2022-02-25 4:17 PM
 */
public class OptionalTest extends SpringTest {

    @Test(expected = NoSuchElementException.class)
    public void whenCreateEmptyOptional_thenNull() {
        Optional<User> emptyOpt = Optional.empty();
        emptyOpt.get();
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateOfEmptyOptional_thenNullPointerException() {
        User user = new User();
        Optional<User> opt = Optional.of(user);
    }
}
