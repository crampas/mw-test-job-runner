package org.example.service;

import org.apache.commons.lang3.Strings;
import org.example.scheduler.Job;
import org.example.scheduler.Run;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SchedulerService {
    @Autowired
    List<Job> jobs;

    public List<Job> getJobs() {
        return List.copyOf(jobs);
    }

    public Optional<Job> getJobByName(String jobName) {
        return jobs.stream()
                .filter(item -> Strings.CS.equals(item.getName(), jobName))
                .findFirst();
    }

    public List<Run> getRunsByJobName(String jobName) {
        return getJobByName(jobName)
                .map(item -> List.copyOf(item.getRuns()))
                .orElse(List.of());
    }

    public Optional<Run> getRunByJobNameAndId(String jobName, String runId) {
        return getRunsByJobName(jobName)
                .stream().filter(item -> Strings.CI.equals(runId, item.getId()))
                .findFirst();
    }
}
