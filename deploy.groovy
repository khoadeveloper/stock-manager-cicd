def remote = [:]
remote.name = '139.99.72.34'
remote.host = '139.99.72.34'
remote.allowAnyHosts = true

pipeline {
    agent any

    environment {
        SSH_CREDS = credentials('ssh stock-manager-dev')
    }


    stages {
        stage("Prepage image") {
            steps {
                script {
                    remote.identity = "${SSH_CREDS}"
                    remote.passphrase = "${SSH_CREDS_PSW}"
                    remote.user = "${SSH_CREDS_USR}"

                    echo "${SSH_CREDS}"
                    sshCommand remote: remote, command: "ls -lrt"
                }
            }
        }
    }
}