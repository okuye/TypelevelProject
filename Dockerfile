FROM ubuntu:latest

# Set DNS to Google DNS to avoid potential DNS issues
RUN echo "nameserver 8.8.8.8" > /etc/resolv.conf

# Update the package list and install dependencies
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget gnupg curl zip unzip && \
    apt-get clean

# Download Jenkins WAR file
RUN wget -q -O /usr/share/jenkins.war https://get.jenkins.io/war-stable/2.387.2/jenkins.war

# Verify jenkins.war file exists
RUN ls -al /usr/share/jenkins.war

# Set up necessary environment for Jenkins and SDKMAN
RUN curl -fsSL https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    sdk install sbt 1.9.9 && \
    sdk install scala 3.3.1 && \
    sdk install java 17.0.10-amzn"

# Add sbt to PATH
ENV PATH="/root/.sdkman/candidates/sbt/current/bin:/root/.sdkman/candidates/scala/current/bin:${PATH}"

# Set up Jenkins user and home directory
RUN useradd -d /var/jenkins_home -m -s /bin/bash jenkins

# Switch to Jenkins user
USER jenkins

# Define default command
CMD ["java", "-jar", "/usr/share/jenkins.war"]
