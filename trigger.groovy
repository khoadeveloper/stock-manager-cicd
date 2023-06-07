import groovy.json.JsonSlurper

def services = []

pipeline {
    agent any
    stages {
        stage("Trigger") {
            steps {
                script {
                    def slurper = new JsonSlurper();

                    def addedArr = slurper.parseText(added).flatten()
                    def modifiedArr = slurper.parseText(modified).flatten()
                    def removedArr = slurper.parseText(removed).flatten()

                    def changes = addedArr + modifiedArr + removedArr;
                    for (final def item in changes) {
                        if (item.contains("/")) {
                            List<String> service = item.split("/")
                            if (!services.contains(service[0])) {
                                services.add(service)
                            }
                        }
                    }

                    def refParts = ref.split("/");

                    println(refParts[0])
                    println(services)
                    //def branch = refParts.get(refParts.size() - 1);
                }
            }
        }
    }
}