// -------------------------------------------------------------------------------------------------------
// -- Mantenimiento: César Garcia - cgarciam@tigo.com.gt
// -- Proyecto: Orquestador Network App
// -- Recursos: Cesar Garcia, Josue Barillas
// -- Empresa: CGM Soluciones
// -- Arquitecto: 
//
// -- Características:
//                     -- Obtiene el código del repositorio git
//                     -- Compila y Construye con Maven
//                     -- Almacena el artefacto de despliegue en Artifactory 
// -------------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------

pipeline {
  agent any 
  tools {nodejs "NodeJS_14.4.0"}
  stages {
  
		stage('Git') {
		  steps {
			script {	  
			  echo 'Clonando Repositorio de OrquestadorNetworkApp Backend ' + env.BRANCH_NAME
			  git(url: 'https://gitlabcorporateapps.tigo.com.gt/backoffice/home-b2c/instalaciones-tecnicas/networking/integracion-saleforce-orquestador/backend/src.git', branch: env.BRANCH_NAME, credentialsId: '47c6893c-a3cf-4190-9e9c-3eec2782d5d6')
			}
		  }
		}

		stage('Compilar') {
			steps {
				script {
					def mvnHome = tool 'MavenBSD'
					env.PATH = "${mvnHome}/bin:${env.PATH}"
					echo "var mvnHome='${mvnHome}'"
					echo "var env.PATH='${env.PATH}'"

					echo 'empaquetando OrquestadorNetwork-API...'

                    sh 'pwd'
                    sh 'mvn package'
                    sh "ls target/"
				}
			}
		}

		stage('SonarQube OrquestadorNetwork-API Analysis') {
			steps {
				script {
					// requires SonarQube Scanner 2.8+
					scannerHome = tool 'Sonar'
				}
				withSonarQubeEnv('SonarQube_BSD') {
				    sh "${scannerHome}/bin/sonar-scanner \
				    -Dsonar.projectKey=OrquestadorNetworkApi \
				    -Dsonar.projectName=OrquestadorNetworkApi \
				    -Dsonar.java.binaries=target/classes \
				    -Dsonar.projectVersion=1.0 \
				    -Dsonar.sources=."
				}
			}
		}		
		
		stage('Save Artifactory') {
			steps {
				script {
					def server = Artifactory.server 'ArtifactoryBSD'			
					
					if (env.BRANCH_NAME=='dev') {
						echo 'Almacenando el artefacto para despliegue de '+env.BRANCH_NAME

						def uploadBackendSpec = """{
							"files": [
								{
									"pattern": "/var/jenkins_home/workspace/Integrador-Tivoli-salesforce_dev/OrquestadorNetwork-api/target/*.war",
									"target": "BSD-VENTAS/"
								}
							]
						}"""
						
						server.upload(uploadBackendSpec)
					}
					
					if (env.BRANCH_NAME=='master') {
						echo 'Almacenando el artefacto para despliegue de '+env.BRANCH_NAME		   

						def uploadBackendSpec = """{
							"files": [
								{
									"pattern": "/var/jenkins_home/workspace/Integrador-Tivoli-salesforce_dev/backend/target/*.war",
									"target": "BSD-VENTAS/"
								}
							]
						}"""
						
						server.upload(uploadBackendSpec)
					}			   
				}
			}
		}



  }
	post {
		success {
			echo 'Exitoso!'
			mail to: 'cgarciam@tigo.com.gt,rsifontes@tigo.com.gt',
			subject: "Despliegue Exitoso: ${currentBuild.fullDisplayName}",
			body: "El despliegue en el ambiente de ${env.BRANCH_NAME} ha finalizado con éxito"			
		}
		unstable {
			echo 'I am unstable :/'
			mail to: 'cgarciam@tigo.com.gt,rsifontes@tigo.com.gt',
			subject: "unstable Pipeline: ${currentBuild.fullDisplayName}",
			body: "El despliegue en el ambiente ${env.BRANCH_NAME} ha generado un error"			
		}
		failure {
			emailext  body: '''$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:
                    $BUILD_LOG maxLines=8000, escapeHtml=true
                    Check console output at $BUILD_URL to view the results.''',
			to: 'cgarciam@tigo.com.gt,rsifontes@tigo.com.gt',
			subject: "failure Pipeline: ${currentBuild.fullDisplayName}"                    
			//body: "El despliegue en el ambiente ${env.BRANCH_NAME} del proyecto Integrador-Tivoli-salesforce ha generaro un error, favor repotarlo a cgarciam@tigo.com.gt o rsifontes@tigo.com.gt"				
		}
	}  
}
