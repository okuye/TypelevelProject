# Example Makefile for an SBT project

# Variables
SBT = sbt

# Default target
all: build

# Build target
build:
	@echo "Building the project..."
	@$(SBT) clean compile

# Test target
test:
	@echo "Running tests..."
	@$(SBT) test

# Deploy target
deploy:
	@echo "Deploying the project..."
	@./deploy.sh $(ENV)

.PHONY: all build test deploy
