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
                                fallbackScript: [
                                        classpath: [],
                                        sandbox: false,
                                        script: 'return ["error"]'
                                ],
                                script: [
                                        classpath: [],
                                        sandbox: false,
                                        script: "if (service == 'eureka') {return ['1', '2']} else {return ['?', ':']}"
                                ]
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
                echo "${params.choice2}"
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