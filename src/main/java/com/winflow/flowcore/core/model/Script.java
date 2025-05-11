package com.winflow.flowcore.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class Script {
    private String path;
    private List<String> args;
}
