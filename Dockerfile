# Use the latest Ubuntu image
FROM ubuntu:latest

# Update the package list and install dependencies including git, make, and PostgreSQL
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget gnupg curl zip unzip git make postgresql postgresql-contrib && \
    rm -rf /var/lib/apt/lists/*

# Download and set up Jenkins
RUN wget -O /usr/share/jenkins.war https://get.jenkins.io/war-stable/2.387.2/jenkins.war || \
    { echo "Failed to download Jenkins war file. Exiting."; exit 1; }

# Check if the file was downloaded successfully
RUN ls -al /usr/share/jenkins.war && \
    echo "Downloaded Jenkins war file successfully"

# Set up Jenkins user and home directory
RUN useradd -d /var/jenkins_home -m -s /bin/bash jenkins

# Install Jenkins Plugin CLI
RUN curl -fsSL -o /usr/local/bin/jenkins-plugin-manager-2.13.0.jar https://github.com/jenkinsci/plugin-installation-manager-tool/releases/download/2.13.0/jenkins-plugin-manager-2.13.0.jar && \
    mv /usr/local/bin/jenkins-plugin-manager-2.13.0.jar /usr/local/bin/jenkins-plugin-cli && \
    chmod +x /usr/local/bin/jenkins-plugin-cli

# Install Jenkins plugins
RUN java -jar /usr/local/bin/jenkins-plugin-cli --plugins git:5.2.2

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

# Switch back to root user to initialize PostgreSQL
USER root

# Initialize PostgreSQL and set the default password for the postgres user
RUN service postgresql start && \
    su - postgres -c "psql -c \"ALTER USER postgres PASSWORD 'docker';\""

# Set the PostgreSQL environment variables
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=docker
ENV POSTGRES_DB=jenkins

# Expose PostgreSQL port
EXPOSE 5432

# Expose Jenkins ports
EXPOSE 8080
EXPOSE 50000

# Define default command
CMD service postgresql start && java -jar /usr/share/jenkins.war
