pipeline {

	agent any
	stages {
		   
		stage('Build and Publish data-notification-subscription Docker Image to ECR') {
                        steps {
                                withCredentials([string(credentialsId: 'EKS-Region', variable: 'REGION'), string(credentialsId: 'Registry-Name', variable: 'REGISTRY_NAME')]) {

					sh './build.sh'
					sh 'ls'
					sh 'docker login -u AWS -p $(aws ecr get-login-password --region $REGION) $REGISTRY_NAME'
					sh 'echo $REGISTRY_NAME"/consent-manager/data-notification-subscription:${BUILD_NUMBER}"'
					sh 'docker build  . -t  $REGISTRY_NAME"/consent-manager/data-notification-subscription:${BUILD_NUMBER}"'
					sh 'docker push $REGISTRY_NAME"/consent-manager/data-notification-subscription:${BUILD_NUMBER}"'
				}

                        }
                        post {
                                success {
                                        echo "Published data-notification-subscription Docker Image to ECR"
                                }
                        }
                }
 
                stage('Deploy data-notification-subscription Application in EKS') {
                        steps {
				sh 'kubectl apply -f kubernetes/deployment.yml'
                                sh 'kubectl apply -f kubernetes/service.yml'
							
				withCredentials([string(credentialsId: 'Registry-Name', variable: 'REGISTRY_NAME')]) {
                                        sh 'kubectl set image deployment/data-notification-subscription-test  data-notification-subscription-test=$REGISTRY_NAME"/consent-manager/data-notification-subscription:${BUILD_NUMBER}" -n consent-manager'
                                }
                        }
                        post {
                                success {
                                        echo "Deployed data-notification-subscription to EKS"
                                }
                        }
                }

				
	}
}

