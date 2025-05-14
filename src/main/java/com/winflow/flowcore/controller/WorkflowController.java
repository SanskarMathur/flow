package com.winflow.flowcore.controller;

import com.winflow.flowcore.core.model.ManualTriggerRequest;
import com.winflow.flowcore.service.WorkflowService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("workflows")
@AllArgsConstructor
public class WorkflowController {
    private WorkflowService workflowService;

    @PostMapping("/trigger")
    public ResponseEntity<?> triggerWorkflow(@RequestBody ManualTriggerRequest requestPayload) {
        workflowService.triggerManualWorkflow(requestPayload);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
