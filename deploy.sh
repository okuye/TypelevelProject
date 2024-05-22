#!/bin/bash

# Function to deploy to a specific environment
deploy_to_env() {
    ENV=$1
    echo "Starting deployment to $ENV environment"

    # Check which environment is being deployed to and set variables accordingly
    case $ENV in
        dev)
            # Set environment-specific variables for Dev
            DEPLOY_DIR="/var/www/dev"
            ;;
        staging)
            # Set environment-specific variables for Staging
            DEPLOY_DIR="/var/www/staging"
            ;;
        qa)
            # Set environment-specific variables for QA
            DEPLOY_DIR="/var/www/qa"
            ;;
        production)
            # Set environment-specific variables for Production
            DEPLOY_DIR="/var/www/production"
            ;;
        *)
            echo "Unknown environment: $ENV"
            exit 1
            ;;
    esac

    # Example deployment steps:
    # 1. Pull the latest code
    echo "Pulling the latest code for $ENV"
    git pull origin $ENV

    # 2. Build the application (if required)
    echo "Building the application for $ENV"
    sbt clean compile

    # 3. Package the application
    echo "Packaging the application for $ENV"
    sbt package

    # 4. Copy files to the deployment directory
    echo "Copying files to $DEPLOY_DIR"
    rsync -av --exclude='.git' target/scala-3.3.1/typelevel-project_3-0.1.0-SNAPSHOT.jar $DEPLOY_DIR

    # 5. Restart the service (if required)
    echo "Restarting services for $ENV"
    # Example restart command, adjust as needed
    systemctl restart myapp-$ENV

    echo "Deployment to $ENV environment completed"
}

# Check if environment is provided
if [ -z "$1" ]; then
    echo "Usage: $0 {dev|staging|qa|production}"
    exit 1
fi

# Deploy to the specified environment
deploy_to_env $1
