pipeline {
    agent any

    environment {
        // Your Docker Hub repository
        DOCKER_REPOSITORY = 'nibinrj/library-app'

        // env.GIT_COMMIT is automatically provided by Jenkins (equivalent to github.sha)
        IMAGE_TAG = "${env.GIT_COMMIT}"
    }

    tools {
        // These names match EXACTLY what you configured in Jenkins
        jdk 'jdk-24'
        maven 'maven-3'
    }

    stages {
        stage('Checkout') {
            steps {
                // Pulls the code from GitHub
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                // Compiles and packages the Java application
                sh 'mvn -B package --file pom.xml'
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Logs into Docker Hub using the credentials stored in Jenkins
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-creds') {

                        // Builds the Docker image locally
                        def appImage = docker.build("${env.DOCKER_REPOSITORY}:${env.IMAGE_TAG}")

                        // Pushes the image with the Git commit SHA tag
                        appImage.push()

                        // Pushes the same image with the 'latest' tag
                        appImage.push("latest")
                    }
                }
            }
        }
    }
}