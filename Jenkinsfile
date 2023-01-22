import groovy.transform.Field

@Field
def analysisType = ''

@Field
def regularAnalysis = '-Dsonar.branch.name=${GIT_BRANCH_NAME}'

@Field
def pullRequestAnalysis = '-Dsonar.pullrequest.branch=${GIT_BRANCH_NAME} \
                           -Dsonar.pullrequest.base=master'


pipeline {
    agent {
        node {
            label 'master'
        }
    }

    environment{
    GIT_REPOSITORY = "https://gitlab.com/sebastiansoja/warehouse-web-app-v2.git"
    GIT_CREDENTIALS = ""
    GIT_BRANCH_NAME = "${env.GIT_BRANCH}"
    MAVEN_DEPLOY_NAME = "${GIT_BRANCH_NAME.replace('/', '-')}"
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {

    stage("Prepare") {
    steps {
    gitlabCommitStatus(name: 'Prepare', connection: gitLabConnection('GitLab'), builds: [[projectId: '21733589',
    revisionHash: "${GIT_BRANCH_NAME}"]]) {
    script {
    currentBuild.displayName = "${env.BUILD_NUMBER}:${GIT_BRANCH_NAME}"
    currentBuild.description = "Run tests on ${GIT_BRANCH_NAME}"
        }
      }
    }
   }


     stage('Checkout') {
                steps {
                gitlabCommitStatus(name: 'Checkout', connection: gitLabConnection('GitLab'), builds: [[projectId: '21733589',
                revisionHash: '${GIT_BRANCH_NAME}']]) {
                echo "Checkout ${GIT_BRANCH_NAME}"
                checkout([$class: 'GitSCM',
                branches: [[name: '**']],
                extensions: [],
                userRemoteConfigs: [[credentialsId: GIT_CREDENTIALS, url: GIT_REPOSITORY]]])
            }
        }
      }
        stage('Build') {
            steps {
            gitlabCommitStatus(name: 'Build', connection: gitLabConnection('GitLab'), builds: [[projectId: '21733589',
            revisionHash: "${GIT_BRANCH_NAME}"]]) {
            configFileProvider([configFile(fileId: 'inparcel-mvn-settings', variable: 'MAVEN_SETTINGS')]) {
            bat "mvn versions:set -DnewVersion=${MAVEN_DEPLOY_NAME}-SNAPSHOT"
            bat 'mvn compiler:compile'
           }
         }
       }
     }

        stage('Tests'){
            steps{
            gitlabCommitStatus(name: 'Tests', connection: gitLabConnection('GitLab'), builds: [[projectId: '21733589',
                        revisionHash: "${GIT_BRANCH_NAME}"]]) {
              configFileProvider([configFile(fileId: 'inparcel-mvn-settings', variable: 'MAVEN_SETTINGS')]) {
              bat 'mvn clean install'
            }
        }
     }
   }
        stage('Deploy') {
            steps {
                gitlabCommitStatus(name: 'Deploy', connection: gitLabConnection('GitLab'), builds: [[projectId: '21733589',
                                        revisionHash: "${GIT_BRANCH_NAME}"]]) {
                configFileProvider([configFile(fileId: 'inparcel-mvn-settings', variable: 'MAVEN_SETTINGS')]) {
                bat 'mvn clean deploy -DskipTests'
            }
        }
     }
  }
 }
}