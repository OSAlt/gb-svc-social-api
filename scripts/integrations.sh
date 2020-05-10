#!/usr/bin/env bash
set -e
FLYWAY_VERSION=6.3.3
DOCKER_COMPOSE_VERSION=1.25.4

## Validates code and publish image for tagged branch
#function bootstrapdb() {
#  echo "this doesn't work in travis for some reason.  So... that's that. "
#  docker-compose -f docker-compose.yml -f docker-compose.testing.yml up -d social_db
#  docker-compose ps
#  until nc -z -v -w30 127.0.0.1 5432; do
#    echo "Waiting a second until the database is receiving connections..."
#    sleep 5
#  done
#  bootstrapflyway
#
#}

function bootstrapflyway() {
  cd flyway
  flyway -configFiles=conf/flyway.conf migrate
  cd ..
}

function integration_test() {
  bootstrapflyway
  sudo update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
  sudo update-alternatives --set javac /usr/lib/jvm/java-8-openjdk-amd64/bin/javac
  export PATH=/usr/lib/jvm/java-8-openjdk-amd64/bin/:$PATH
  export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/

  mvn clean install
  cd social-api
  docker build --tag=geekbeacon/social-api:latest .

  if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    docker images
    publish_image
  fi
}

## Publish image to our docker hub repository
function publish_image() {
  echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
  echo docker push geekbeacon/social-api:latest
  docker push geekbeacon/social-api:latest
}

function setupFlyway() {
  wget -qO- https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/${FLYWAY_VERSION}/flyway-commandline-${FLYWAY_VERSION}-linux-x64.tar.gz | tar xvz && sudo ln -s $(pwd)/flyway-${FLYWAY_VERSION}/flyway /usr/local/bin

}

# Sets up docker compose for regression testing
function setupDocker() {
  sudo rm /usr/local/bin/docker-compose
  curl -L https://github.com/docker/compose/releases/download/${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m) >docker-compose
  chmod +x docker-compose
  sudo mv docker-compose /usr/local/bin
  ln -s developy.yml docker-compose.yml
}

## Brings up the dashboard and executes the regression tests
function cron_regression() {
  if [ "$TRAVIS_EVENT_TYPE" = "cron" ]; then
    echo "Cron not supported"
  fi
}

function setupEnv
{
  source .env
  export $(cut -d= -f1 .env)
}

# Entry point
function main() {
  setupEnv
  setupDocker
  setupFlyway

  if [[ "$TRAVIS_EVENT_TYPE" == "cron" ]]; then
    cron_regression
  else
    integration_test
  fi

}

main
