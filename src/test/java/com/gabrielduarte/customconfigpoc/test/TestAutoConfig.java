package com.gabrielduarte.customconfigpoc.test;

import com.gabrielduarte.customconfigpoc.CustomConfigPocApplication;
import com.gabrielduarte.customconfigpoc.domain.UserEntity;
import com.gabrielduarte.customconfigpoc.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CustomConfigPocApplication.class)
@EnableJpaRepositories
public class TestAutoConfig {

    private final UserRepository userRepository;

    @Autowired
    public TestAutoConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void saveUserAndReturnOk() {
        UserEntity entity = new UserEntity("18378");
        userRepository.save(entity);
    }

}
