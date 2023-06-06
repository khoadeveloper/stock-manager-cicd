def remote = [:]
remote.name = '139.99.72.34'
remote.host = '139.99.72.34'
remote.allowAnyHosts = true

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