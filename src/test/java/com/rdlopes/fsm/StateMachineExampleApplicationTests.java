package com.rdlopes.fsm;

import com.rdlopes.fsm.parser.ConfigurationFileParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StateMachineExampleApplicationTests {

    @Autowired
    private StateMachineExampleApplication stateMachineExampleApplication;

    @Autowired
    private ConfigurationFileParser configurationFileParser;

    @Test
    public void contextLoads() {
        assertThat(stateMachineExampleApplication).isNotNull();
        assertThat(configurationFileParser).isNotNull();
    }

}
