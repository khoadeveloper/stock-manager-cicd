versions = fetchVersions(service)

properties([
        parameters([
                [
                        $class: 'CascadeChoiceParameter',
                        choiceType: 'PT_SINGLE_SELECT',
                        description: 'Active Choices Reactive parameter',
                        filterLength: 1,
                        filterable: true,
                        name: 'choice2',
                        randomName: 'choice-parameter-7601237141171',
                        referencedParameters: 'choice1',
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
                                        script: 'if(choice1.equals("aaa")){return [\'a\', \'b\']} else {return [\'aaaaaa\',\'fffffff\']}'
                                ]
                        ]
                ]
        ])
])

pipeline {
    agent any

    parameters {
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
    }

    stages {
        stage("Pull image") {
            steps {
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