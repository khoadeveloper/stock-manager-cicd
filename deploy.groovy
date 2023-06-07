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
                ]
        ])
])

pipeline {
    agent any

    stages {
        stage("Cleaning environment") {
            steps {
                sshagent(credentials: ['ssh stock-manager-dev']) {
                    sh '''
                          ssh -o StrictHostKeyChecking=no ubuntu@139.99.72.34 "
                            docker stop $(docker ps -s | awk -v i="khuyenstore/${service}.*" '{if($2~i){print$1}}');
                            docker rmi -f $(docker images -q khuyenstore/${service})
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
                            docker run -d 139.99.72.55:5000/khuyenstore/${service}:${version};
                          "
                      '''
                }
            }
        }
    }
}