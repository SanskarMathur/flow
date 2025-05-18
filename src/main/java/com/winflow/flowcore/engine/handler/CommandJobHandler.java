package com.winflow.flowcore.engine.handler;

import com.winflow.flowcore.core.model.ExecutionContext;
import com.winflow.flowcore.core.model.Job;
import com.winflow.flowcore.core.model.JobExecutionResult;
import com.winflow.flowcore.exception.JobExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CommandJobHandler implements JobHandler {
    private final Map<String, Process> runningProcesses = new ConcurrentHashMap<>();

    @Override
    public JobExecutionResult execute(Job job, ExecutionContext context) throws JobExecutionException {
        log.info("Executing Job id: {}", job.getId());

        // Process Initialisation
        ProcessBuilder processBuilder = new ProcessBuilder(job.getCommand());
        List<String> commands = parseCommands(job.getCommand());
        processBuilder.command(commands);

        // Variables
        Process process = null;
        StringBuilder stdOut = new StringBuilder();
        StringBuilder stdErr = new StringBuilder();
        int exitCode = -1;
        Instant startTime = Instant.now();

        try {
            process = processBuilder.start();
            runningProcesses.put(job.getId(), process);

            // Reading Output Messages
            try (BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = stdOutReader.readLine()) != null) {
                    stdOut.append(line).append("\n");
                }
            }

            // Reading error messages
            try (BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = stdErrReader.readLine()) != null) {
                    stdErr.append(line).append("\n");
                }
            }

            // Wait for process to complete within given timeout
            boolean isCompleted = process.waitFor(job.getTimeout(), TimeUnit.MILLISECONDS);
            if (!isCompleted) {
                process.destroyForcibly();
                throw new JobExecutionException(job.getId(), "Process Timed out");
            }

            exitCode = process.exitValue();

            if (exitCode == 0) log.info("Command completed successfully");
            else log.warn("Command completed with non-zero exit code: {}", exitCode);

        } catch (IOException e) {
            throw new JobExecutionException(job.getId(), "Failed to executeSequentially command, reason: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JobExecutionException(job.getId(), "Command execution interrupted: " + e.getMessage());
        } catch (Exception e) {
            throw new JobExecutionException(job.getId(), e.getMessage());
        } finally {
            if (process != null) {
                process.destroyForcibly();
                runningProcesses.remove(job.getId());
            }
        }
        Instant endTime = Instant.now();

        return JobExecutionResult.builder()
                .jobId(job.getId())
                .success(exitCode == 0)
                .successMessage(stdOut.toString())
                .errorMessage(stdErr.toString())
                .exitCode(exitCode)
                .startTime(startTime)
                .endTime(endTime)
                .executionTime(Duration.between(startTime, endTime))
                .build();
    }

    private List<String> parseCommands(String command) {
        List<String> commands = new ArrayList<>();
        return commands;
    }

    @Override
    public boolean validateExecution(Job job) {
        return job != null && job.getCommand() != null && !job.getCommand().trim().isEmpty();
    }

    @Override
    public boolean stopExecution(String jobId) {
        Process process = runningProcesses.get(jobId);
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
            runningProcesses.remove(jobId);
            log.info("Stopped process with jobId: {}, processId: {}", jobId, process.pid());
            return true;
        }
        return false;
    }
}
