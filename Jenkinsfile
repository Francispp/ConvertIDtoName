pipeline {
  agent any

  environment {
    AWS_REGION = 'ap-northeast-2'     // 替换为你的 AWS 区域
    ECR_REPO = '816069167970.dkr.ecr.ap-northeast-2.amazonaws.com/demo/demo1'
    IMAGE_TAG = "latest"
    FULL_IMAGE = "${ECR_REPO}:${IMAGE_TAG}"
    SSH_HOST = 'ec2-user@<13.124.218.231>'
    SSH_CREDENTIAL_ID = 'aws-ec2-key'  // Jenkins 配置的 SSH 私钥
    AWS_CREDENTIAL_ID = 'AWS_CREDENTIAL_ID'   // Jenkins 配置的 AWS 凭证（accessKeyId + secretKey）
  }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/Francispp/ConvertIDtoName.git'
      }
    }

    stage('Maven Build') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Docker Build') {
      steps {
        script {
          docker.build("${FULL_IMAGE}", ".")
        }
      }
    }

    stage('Login to ECR') {
      steps {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: "${AWS_CREDENTIAL_ID}"]]) {
          sh '''
            aws ecr get-login-password --region $AWS_REGION | \
            docker login --username AWS --password-stdin $ECR_REPO
          '''
        }
      }
    }

    stage('Push to ECR') {
      steps {
        sh "docker push ${FULL_IMAGE}"
      }
    }

    stage('Deploy on EC2 via SSH') {
      steps {
        sshagent(credentials: ["${SSH_CREDENTIAL_ID}"]) {
          sh """
            ssh -o StrictHostKeyChecking=no ${SSH_HOST} '
              aws ecr get-login-password --region ${AWS_REGION} | \
              docker login --username AWS --password-stdin ${ECR_REPO} &&
              docker stop my-springboot-app || true &&
              docker rm my-springboot-app || true &&
              docker pull ${FULL_IMAGE} &&
              docker run -d --name my-springboot-app -p 8080:8080 ${FULL_IMAGE}
            '
          """
        }
      }
    }
  }

  post {
    success {
      echo "✅ 部署成功：http://15.164.163.123:8080"
    }
    failure {
      echo "❌ 构建或部署失败，请查看控制台日志"
    }
  }
}
