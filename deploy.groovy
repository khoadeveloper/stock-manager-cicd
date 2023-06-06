pipeline {
    agent any

    def remote = [:]
    remote.name = 'stock-manager-dev'
    remote.host = '139.99.72.34'
    remote.user = 'ubuntu'
    remote.allowAnyHosts = true
    remote.passphrase = "123456"

    stages {
        stage("Prepage image") {
            sshCommand remote: remote, command: "ls -lrt"
        }
    }
}