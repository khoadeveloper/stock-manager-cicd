def remote = [:]
remote.name = 'stock-manager-dev'
remote.host = '139.99.72.34'
remote.user = 'ubuntu'
remote.allowAnyHosts = true
remote.passphrase = "123456"

pipeline {
    agent any

    stages {
        stage("Prepage image") {
            steps {
                script {
                    sshCommand remote: remote, command: "ls -lrt"
                }
            }
        }
    }
}