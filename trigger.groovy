pipeline {
    stages {
        stage("Trigger") {
            steps {
                echo "${ref}"
                echo "${changes}"
            }
        }
    }
}