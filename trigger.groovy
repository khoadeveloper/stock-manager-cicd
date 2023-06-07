pipeline {
    agent any
    stages {
        stage("Trigger") {
            steps {
                echo "${ref}"
                echo "${changes}"
            }
        }
    }
}