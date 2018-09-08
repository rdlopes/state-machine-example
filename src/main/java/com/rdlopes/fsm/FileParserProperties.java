package com.rdlopes.fsm;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@ConfigurationProperties("parser")
public class FileParserProperties {
    private Resource input;
}
