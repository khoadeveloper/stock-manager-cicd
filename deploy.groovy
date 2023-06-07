versions = fetchVersions(service)

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

    properties([
            parameters([
                    [
                            $class: 'CascadeChoiceParameter',
                            name: "version",
                            choiceType: 'PT_SINGLE_SELECT',
                            description: "Version to deploy",
                            filterable: false,
                            script: [
                                    $class: 'GroovyScript',
                                    fallbackScript: [
                                            classpath: [],
                                            sandbox: false,
                                            script:
                                                    'return[\'Could not get Env\']'
                                    ],
                                    script: [
                                            classpath: [],
                                            sandbox: false,
                                            script:
                                                    'return["Dev","QA","Stage","Prod"]'
                                    ]
                            ]
                    ]
            ])
    ])

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