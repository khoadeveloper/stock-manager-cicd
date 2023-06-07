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
                            String service = item.split("/")[0]
                            if (!services.contains(service)) {
                                services.add(service)
                            }
                        }
                    }

                    def refParts = ref.split("/")
                    env.branch = refParts[refParts.size() - 1]
                    env.services = services

                    for (final def ser in services) {
                        println(ser)

                    }
                }

                services.each{service ->
                    build job: "stock-manager/${branch}",
                            parameters: [string(name: 'service', value: "${service}")]
                }
            }
        }
    }
}