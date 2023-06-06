pipeline {
    agent any

    stages {
        def remote = [:]
        remote.name = 'stock-manager-dev'
        remote.host = '139.99.72.34'
        remote.user = 'ubuntu'
        remote.allowAnyHosts = true

        stage("Prepage image") {
            sshCommand remote: remote, command: "ls -lrt"
        }
    }
}