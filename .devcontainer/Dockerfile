FROM mcr.microsoft.com/vscode/devcontainers/java:0-11

ARG GRAALVM_VERSION="21.1.0.r11-grl"

RUN DEBIAN_FRONTEND=noninteractive apt-get update \
  && apt-get install -qq -y --no-install-recommends build-essential libz-dev zlib1g-dev \
  && apt-get autoremove -y \
  && apt-get clean -y \
  && rm -rf /var/lib/apt/lists/*

RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven"
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install java \"${GRAALVM_VERSION}\""
RUN su vscode -c "/usr/local/sdkman/candidates/java/current/bin/gu install native-image"
