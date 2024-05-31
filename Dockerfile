# Use the latest Ubuntu image
FROM ubuntu:latest

# Update the package list and install dependencies
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget gnupg curl zip unzip && \
    rm -rf /var/lib/apt/lists/*

# Download and set up Jenkins
RUN wget -q -O /usr/share/jenkins.war https://get.jenkins.io/war-stable/2.387.2/jenkins.war
RUN ls -al /usr/share/jenkins.war

# Set up Jenkins user and home directory
RUN useradd -d /var/jenkins_home -m -s /bin/bash jenkins

# Switch to Jenkins user
USER jenkins
WORKDIR /var/jenkins_home

# Set up SDKMAN and install sbt, Scala, and Java for Jenkins user
RUN curl -fsSL https://get.sdkman.io | bash && \
    bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && \
    sdk install sbt 1.9.9 && \
    sdk install scala 3.3.1 && \
    sdk install java 17.0.10-amzn"

# Add sbt and other SDKMAN tools to PATH for Jenkins user
RUN echo 'source "$HOME/.sdkman/bin/sdkman-init.sh"' >> ~/.bashrc && \
    echo 'export PATH="$HOME/.sdkman/candidates/sbt/current/bin:$HOME/.sdkman/candidates/scala/current/bin:$PATH"' >> ~/.bashrc

# Define default command
CMD ["java", "-jar", "/usr/share/jenkins.war"]
