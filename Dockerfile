# Use the latest Ubuntu image
FROM ubuntu:latest

# Update the package list and install dependencies
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget gnupg2 ca-certificates git make && \
    rm -rf /var/lib/apt/lists/*

# Download Jenkins WAR file
RUN wget -q -O /usr/share/jenkins.war https://get.jenkins.io/war-stable/2.387.2/jenkins.war

# Verify jenkins.war file exists
RUN ls -al /usr/share/jenkins.war

# Expose the necessary ports
EXPOSE 8080 50000

# Start Jenkins
CMD ["java", "-jar", "/usr/share/jenkins.war"]
