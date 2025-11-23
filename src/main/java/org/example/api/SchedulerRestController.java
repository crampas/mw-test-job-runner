package org.example.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.scheduler.Job;
import org.example.scheduler.Run;
import org.example.service.SchedulerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SchedulerRestController {
    private final SchedulerService schedulerService;

    @GetMapping("/api/jobs")
    public ApiResponse<List<Job>> getJobs() {
        return ApiResponse.ofData(schedulerService.getJobs());
    }

    @GetMapping("/api/job/{jobName}/runs")
    public ApiResponse<List<Run>> getJobRuns(@PathVariable String jobName) {
        return ApiResponse.ofData(schedulerService.getRunsByJobName(jobName));
    }

    @PostMapping("/api/job/{jobName}/start")
    public ApiResponse<Run> startJobRun(@PathVariable String jobName,
                                        @RequestParam Map<String, String> queryParams) {
        JsonNode jsonParams = new ObjectMapper().convertValue(queryParams, JsonNode.class);
        Job job = schedulerService.getJobByName(jobName).orElse(null);
        if (job == null) {
            return ApiResponse.ofError("Job %s not found".formatted(jobName));
        }

        Object params = new ObjectMapper().treeToValue(jsonParams, job.getParamsTypeReference());
        log.info("startJobRun with params {}", params);

        return schedulerService.getJobByName(jobName)
                .map(item -> item.submitRun(params))
                .map(ApiResponse::ofData)
                .orElse(ApiResponse.ofError("not found"));
    }

    @GetMapping("/api/job/{jobName}/run/{runId}")
    public ApiResponse<Run> getRun(@PathVariable String jobName,
                                           @PathVariable String runId) {
        return schedulerService.getRunByJobNameAndId(jobName, runId)
                .map(item -> ApiResponse.ofData(item))
                .orElse(ApiResponse.ofError("job or run not found"));
    }

    @GetMapping("/api/job/{jobName}/run/{runId}/logStream")
    public ApiResponse<ROLogStream> getLogStreamOfRun(@PathVariable String jobName,
                                  @PathVariable String runId,
                                  @RequestParam(required = false, defaultValue = "0") int last) {
        return schedulerService.getRunByJobNameAndId(jobName, runId)
                .map(item -> item.getEnvironment().getOutLines())
                .map(item -> new ROLogStream().setLines(item.subList(Math.min(last, item.size()), item.size())).setNext(item.size()))
                .map(item -> ApiResponse.ofData(item))
                .orElse(ApiResponse.ofError("job or run not found"));
    }
}
