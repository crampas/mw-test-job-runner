package org.example.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ROLogStream {
    private int next;
    private List<String> lines;
}
