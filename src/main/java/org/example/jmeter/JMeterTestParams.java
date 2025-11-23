package org.example.jmeter;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JMeterTestParams {
    private String microservice;
    private String users;
    private String duration;
    private String url;
}
