pipeline {
    agent any
    stages {
        stage("Trigger") {
            steps {
                echo "${ref}"
                echo "${added}"
                echo "${modified}"
                echo "${removed}"
            }
        }
    }
}