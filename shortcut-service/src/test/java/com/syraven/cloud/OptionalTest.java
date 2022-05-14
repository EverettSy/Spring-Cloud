package com.syraven.cloud;

import com.syraven.cloud.domain.UserRoot;
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
        Optional<UserRoot> emptyOpt = Optional.empty();
        emptyOpt.get();
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateOfEmptyOptional_thenNullPointerException() {
        UserRoot userRoot = new UserRoot();
        Optional<UserRoot> opt = Optional.of(userRoot);
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
        UserRoot userRoot = new UserRoot(1,"john@gmail.com", "1234",1,1);
        Optional<UserRoot> opt = Optional.ofNullable(userRoot);
        assertTrue(opt.isPresent());
        opt.ifPresent(u -> assertEquals(userRoot.getUsername(), opt.get().getUsername()));

        assertEquals(userRoot.getUsername(), opt.get().getUsername());


    }


    @Test
    public void whenEmptyValue_thenReturnDefault() {
        UserRoot userRoot = null;
        UserRoot userRoot2 = new UserRoot(1,"john@gmail.com", "1234",1,1);
        UserRoot result = Optional.ofNullable(userRoot).orElse(userRoot2);

        assertEquals(userRoot.getUsername(), result.getUsername());
    }

    @Test
    public void whenValueNotNull_thenIgnoreDefault() {
        UserRoot userRoot = new UserRoot(1,null, "1234",1,1);
        UserRoot userRoot2 = new UserRoot(1,"anna@gmail.com", "1234",1,1);
        UserRoot result = Optional.ofNullable(userRoot).orElse(userRoot2);

        UserRoot result2 = Optional.ofNullable(userRoot).orElseGet( () -> userRoot2);

        assertEquals(null, result.getUsername());
    }
}
