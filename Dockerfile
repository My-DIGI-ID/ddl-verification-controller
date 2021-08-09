FROM maven:3-openjdk-11-slim as docker_build

ENTRYPOINT ["/bin/bash", "-c", "./mvnw -Pprod jib:dockerBuild"]
