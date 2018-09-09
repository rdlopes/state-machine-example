package com.rdlopes.fsm;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;

@Data
@ConfigurationProperties("parser")
public class FileParserProperties {
    private Resource inputResource;

    private Charset inputCharset = Charset.defaultCharset();
}
