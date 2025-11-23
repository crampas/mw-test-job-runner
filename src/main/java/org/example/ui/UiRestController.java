package org.example.ui;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UiRestController {
    private final SchedulerService schedulerService;
    private Template jobsTemplate;
    private Template jobTemplate;
    private Template runTemplate;

    @SneakyThrows
    public UiRestController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
        Handlebars handlerBars = new Handlebars();
        jobsTemplate = handlerBars.compile("templates/jobs");
        jobTemplate = handlerBars.compile("templates/job");
        runTemplate = handlerBars.compile("templates/run");
    }

    @GetMapping("/jobs.html")
    public String viewJobs() throws Exception {
        List<Job> jobs = schedulerService.getJobs();
        return jobsTemplate.apply(Map.of("jobs", jobs));
    }

    @GetMapping("/job/{jobName}.html")
    public String viewJobRuns(@PathVariable String jobName) throws Exception {
        Job job = schedulerService.getJobByName(jobName).orElse(null);
        return jobTemplate.apply(Map.of("job", job));
    }

    @GetMapping("/job/{jobName}/run/{runId}.html")
    public String viewJobRun(@PathVariable String jobName, @PathVariable String runId) throws Exception {
        Job job = schedulerService.getJobByName(jobName).orElse(null);
        Run run = schedulerService.getRunByJobNameAndId(jobName, runId).orElse(null);

        return runTemplate.apply(Map.of("job", job, "run", run));
    }

    @PostMapping("/{jobName}/start.html/start")
    public ResponseEntity startJobRunByForm(@PathVariable String jobName,
                                            @RequestParam Map<String, String> queryParams) {
        JsonNode jsonParams = new ObjectMapper().convertValue(queryParams, JsonNode.class);
        Job job = schedulerService.getJobByName(jobName).orElse(null);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        Object params = new ObjectMapper().treeToValue(jsonParams, job.getParamsTypeReference());
        log.info("startJobRun with params {}", params);
        Run run = job.submitRun(params);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/job/%s/run/%s.html".formatted(jobName, run.getId()));
        return new ResponseEntity(headers, HttpStatus.FOUND);
    }

}
