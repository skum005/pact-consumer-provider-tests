FROM maven:3-openjdk-11
WORKDIR /app
COPY src src
COPY pom.xml pom.xml
ENTRYPOINT mvn test -f pom.xml -Dgroups=$TESTS -Dallure.results.directory=target/allure-results
# build image command: docker build -t pact-tests .
# command to run the image: docker run -e TESTS=consumer-tests -v /Users/santosh/docker-volumemapping:/app/target pact-tests