FROM maven:3.6.3-jdk-11 as docker_build

ENTRYPOINT ["/bin/bash", "-c", "./mvnw -Pprod jib:dockerBuild"]
