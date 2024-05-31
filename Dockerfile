# Use the latest Ubuntu image
FROM ubuntu:latest

# Update the package list and install dependencies
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget gnupg2 ca-certificates git make curl zip unzip && \
    rm -rf /var/lib/apt/lists/*

# Download Jenkins WAR file
RUN wget -q -O /usr/share/jenkins.war https://get.jenkins.io/war-stable/2.387.2/jenkins.war

# Verify jenkins.war file exists
RUN ls -al /usr/share/jenkins.war

# Set up necessary environment for Jenkins
RUN curl -fsSL https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    sdk install sbt 1.9.9 && \
    sdk install scala 3.3.1 && \
    sdk install java 17.0.10-amzn"

# Configure Git for signed commits
RUN git config --global user.signingkey 0F01AA4B71345BA5 && \
    git config --global commit.gpgsign true

# Expose the necessary ports
EXPOSE 8080 50000

# Start Jenkins
CMD ["java", "-jar", "/usr/share/jenkins.war"]
