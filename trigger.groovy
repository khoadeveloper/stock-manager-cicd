import groovy.json.JsonSlurper

pipeline {
    agent any
    stages {
        stage("Trigger") {
            steps {
                echo "${ref}"
                echo "${added}"
                echo "${modified}"
                echo "${removed}"

                script {
                    def slurper = new JsonSlurper();

                    def addedArr = slurper.parseText(added)
                    println(addedArr.flatten())
                }
            }
        }
    }
}