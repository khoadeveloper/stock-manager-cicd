@Library("shared-library") _

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
                                        sandbox: false,
                                        classpath: [],
                                        script: '''
                                            import groovy.json.JsonSlurper
                                            
                                            def response = new URL('http://139.99.72.55:5000/v2/khuyenstore/' + service + '/tags/list')
                                            return new JsonSlurper().parseText(response.getText()).tags
                                        ''']
                        ]
                ],
                [
                        $class: 'CascadeChoiceParameter',
                        choiceType: 'PT_SINGLE_SELECT',
                        description: 'Port to deploy?',
                        filterable: false,
                        name: 'port',
                        randomName: 'choice-parameter-port',
                        referencedParameters: 'service',
                        script: [
                                $class: 'GroovyScript',
                                script: [
                                        sandbox: false,
                                        classpath: [],
                                        script: '''
                                            return common.getAppPort(service)
                                        ''']
                        ]
                ]
        ])
])

pipeline {
    agent any

    stages {
        stage("Stopping container") {
            steps {
                buildName "${service}|${version}"

                sshagent(credentials: ['ssh stock-manager-dev']) {
                    sh '''
                       ssh -o StrictHostKeyChecking=no ubuntu@139.99.72.34 "
                        docker ps | grep  -E 'khuyenstore/${service}'  |  awk '{print \\\$1}' | xargs -r docker stop
                       "
                    '''
                }
            }
        }
        stage("Deleting images") {
            steps {
                sshagent(credentials: ['ssh stock-manager-dev']) {
                    sh '''
                       ssh -o StrictHostKeyChecking=no ubuntu@139.99.72.34 "
                        docker images | grep -E 'khuyenstore/${service}' | awk '{print \\\$3}' | xargs -r docker rmi -f
                       "
                    '''
                }
            }
        }
        stage("Pulling image") {
            steps {
                sshagent(credentials: ['ssh stock-manager-dev']) {
                    sh '''
                          ssh -o StrictHostKeyChecking=no ubuntu@139.99.72.34 "
                            docker pull 139.99.72.55:5000/khuyenstore/${service}:${version};
                          "
                      '''
                }
            }
        }
        stage("Deploying") {
            steps {
                sshagent(credentials: ['ssh stock-manager-dev']) {
                    sh '''
                          ssh -o StrictHostKeyChecking=no ubuntu@139.99.72.34 "
                            docker run -d -p${port}:${port} 139.99.72.55:5000/khuyenstore/${service}:${version};
                          "
                      '''
                }
            }
        }
    }
}