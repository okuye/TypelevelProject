#!/bin/bash

# Function to deploy to a specific environment
deploy_to_env() {
    ENV=$1
    echo "Starting deployment to $ENV environment"

    # Check which environment is being deployed to and set variables accordingly
    case $ENV in
        dev)
            DEPLOY_DIR="/var/www/dev"
            ;;
        staging)
            DEPLOY_DIR="/var/www/staging"
            ;;
        qa)
            DEPLOY_DIR="/var/www/qa"
            ;;
        production)
            DEPLOY_DIR="/var/www/production"
            ;;
        *)
            echo "Unknown environment: $ENV"
            exit 1
            ;;
    esac

    # Always pull from the main branch
    echo "Pulling the latest code from the main branch for $ENV"
    git checkout main
    git pull origin main

    # Build the application (if required)
    echo "Building the application for $ENV"
    sbt clean compile

    # Package the application
    echo "Packaging the application for $ENV"
    sbt package

    # Copy files to the deployment directory
    echo "Copying files to $DEPLOY_DIR"
    rsync -av --exclude='.git' target/scala-3.3.1/typelevel-project_3-0.1.0-SNAPSHOT.jar $DEPLOY_DIR

    # Restart the service (this step might need adjustment based on your deployment environment)
    echo "Restarting services for $ENV"
    # Example restart command, adjust as needed or remove if not required
    # systemctl restart myapp-$ENV

    echo "Deployment to $ENV environment completed"
}

# Check if environment is provided
if [ -z "$1" ]; then
    echo "Usage: $0 {dev|staging|qa|production}"
    exit 1
fi

# Deploy to the specified environment
deploy_to_env $1
