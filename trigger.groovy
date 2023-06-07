import groovy.json.JsonSlurper

def services = []

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
                    def modifiedArr = slurper.parseText(modified)
                    def removedArr = slurper.parseText(removed)

                    def changes = addedArr + modifiedArr + removedArr;
                    for (final def item in changes) {
                        if (item.contains("/")) {
                            def service = item.split("/").get(0);
                            println(service)
                            if (!services.contains(service)) {
                                services.add(service)
                            }
                        }
                    }

                    def refParts = ref.split("/");
                    //def branch = refParts.get(refParts.size() - 1);

                    println(refParts)
                    println(services)
                }
            }
        }
    }
}