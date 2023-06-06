def remote = [:]
remote.name = '139.99.72.34'
remote.host = '139.99.72.34'
remote.user = 'ubuntu'
remote.allowAnyHosts = true
remote.passphrase = "123456"
remote.identity = "ubuntu@jenkins"

pipeline {
    agent any

    environment {
        SSH_CREDS = credentials('ssh stock-manager-dev')
    }


    stages {
        stage("Prepage image") {
            steps {
                //script {
                //    sshCommand remote: remote, command: "ls -lrt"
                //}
                echo "$SSH_CREDS_USR"
                echo "$SSH_CREDS"
                echo "$SSH_CREDS_PSW"
            }
        }
    }
}