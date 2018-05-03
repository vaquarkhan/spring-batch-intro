#!/usr/bin/env bash

java -cp target/spring-batch-intro-1.0-SNAPSHOT.jar \
  org.springframework.batch.core.launch.support.CommandLineJobRunner \
  io.github.benas.sbi.JobConfiguration \
  job \
  $*
