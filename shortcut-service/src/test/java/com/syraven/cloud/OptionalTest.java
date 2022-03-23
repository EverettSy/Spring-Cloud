package com.syraven.cloud;

import com.syraven.cloud.domin.User;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author syrobin
 * @version v1.0
 * @description: Optional类测试
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

    //访问 Optional 对象的值
    @Test
    public void whenCreateOfNullableOptional_thenOk() {
        String name = "John";
        Optional<String> opt = Optional.ofNullable(name);

        assertEquals("John", opt.get());
    }

    @Test
    public void whenCheckIfPresent_thenOk() {
        User user = new User(1,"john@gmail.com", "1234",1,1);
        Optional<User> opt = Optional.ofNullable(user);
        assertTrue(opt.isPresent());
        opt.ifPresent(u -> assertEquals(user.getUsername(), opt.get().getUsername()));

        assertEquals(user.getUsername(), opt.get().getUsername());


    }


    @Test
    public void whenEmptyValue_thenReturnDefault() {
        User user = null;
        User user2 = new User(1,"john@gmail.com", "1234",1,1);
        User result = Optional.ofNullable(user).orElse(user2);

        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    public void whenValueNotNull_thenIgnoreDefault() {
        User user = new User(1,null, "1234",1,1);
        User user2 = new User(1,"anna@gmail.com", "1234",1,1);
        User result = Optional.ofNullable(user).orElse(user2);

        User result2 = Optional.ofNullable(user).orElseGet( () -> user2);

        assertEquals(null, result.getUsername());
    }
}
