package org.example.jmeter;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JMeterTestData {
    private String link;
}
