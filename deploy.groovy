def fetchVersion(service) {
    def response = httpRequest 'http://139.99.72.55:5000/v2/khuyenstore/' + service + '/tags/list'

    return new JsonSlurper().parseText(response.content).tags
}

properties([
        parameters([
                choice(choices: [
                        "admin",
                        "api",
                        "common",
                        "config",
                        "eureka",
                        "flickr",
                        "gateway",
                        "kiotviet",
                        "lazada",
                        "mailer",
                        "scheduler",
                        "sendo",
                        "tiki"
                ], description: "Which service to deploy?", name: "service"),
                [
                        $class: 'CascadeChoiceParameter',
                        choiceType: 'PT_SINGLE_SELECT',
                        description: 'Version to deploy?',
                        filterable: false,
                        name: 'version',
                        randomName: 'choice-parameter-7601237141171',
                        referencedParameters: 'service',
                        script: [
                                $class: 'GroovyScript',
                                script: [
                                        sandbox: true,
                                        classpath: [],
                                        script: '''
                                            return fetchVersion(service)
                                        ''']
                        ]
                ]
        ])
])

pipeline {
    agent any

    /*parameters {
        choice(choices: [
                "admin",
                "api",
                "common",
                "config",
                "eureka",
                "flickr",
                "gateway",
                "kiotviet",
                "lazada",
                "mailer",
                "scheduler",
                "sendo",
                "tiki"
        ], description: "Which service to deploy?", name: "service")
    }*/

    stages {
        stage("Pull image") {
            steps {
                script {
                    println(fetchVersion(service))
                }
                sshagent(credentials: ['ssh stock-manager-dev']) {
                    sh '''
                          ssh -o StrictHostKeyChecking=no ubuntu@139.99.72.34 "
                            docker pull 139.99.72.55:5000/khuyenstore/${service}:0.0.1-SNAPSHOT
                          "
                      '''
                }
                /*script {
                    remote.identity = "${SSH_CREDS}"
                    remote.passphrase = "${SSH_CREDS_PSW}"
                    remote.user = "${SSH_CREDS_USR}"

                    echo "${SSH_CREDS}"
                    sshCommand remote: remote, command: "ls -lrt"
                }*/
            }
        }
    }
}