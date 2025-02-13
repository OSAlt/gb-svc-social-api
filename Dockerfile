FROM golang:1.24.0  AS build-stage

LABEL app="build-gb-svc-social-api"
LABEL REPO="https://github.com/OSAlt/gb-svc-social-api"

ENV PROJPATH=/go/src/github.com/OSAlt/gb-svc-social-api

# Because of https://github.com/docker/docker/issues/14914
ENV PATH=$PATH:$GOROOT/bin:$GOPATH/bin

ADD . /go/src/github.com/OSAlt/gb-svc-social-api
WORKDIR /go/src/github.com/OSAlt/gb-svc-social-api

RUN make linux

# Final Stage
FROM golang:1.24.0 

ARG GIT_COMMIT
ARG VERSION
LABEL REPO="github.com/OSAlt/gb-svc-social-api"
LABEL GIT_COMMIT=$GIT_COMMIT
LABEL VERSION=$VERSION

ENV PATH=$PATH:/opt/gb-svc-social-api/bin:/opt/gb-svc-social-api/

WORKDIR /opt/gb-svc-social-api/

COPY --from=build-stage /go/src/github.com/OSAlt/gb-svc-social-api/social_svc_linux /opt/gb-svc-social-api/
RUN \
    apt-get update && \
    apt install -y dumb-init  && \
    apt-get clean autoclean && \
    apt-get autoremove --yes && \
    rm -rf /var/lib/{apt,dpkg,cache,log}/ && \
    chmod +x /opt/gb-svc-social-api/social_svc_linux

# Create appuser
RUN useradd -m  gb-svc-social-api
USER gb-svc-social-api

ENTRYPOINT ["/usr/bin/dumb-init", "--"]

CMD ["/opt/gb-svc-social-api/social_svc_linux"]