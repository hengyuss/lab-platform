pipeline {
    agent any
    environment {
        DB_CREDS = credentials('my-db-credentials-id')
        DB_HOST_DEV = credentials('DB_HOST_DEV')
        DB_HOST_PROD = credentials('DB_HOST_PROD')

        DB_URL_MAIN = "jdbc:mysql://10.33.9.41:3306/lab_platform_prod"  // 主库
        DB_URL_DEV  = "jdbc:mysql://10.33.9.41:3306/lab_platform_dev"   // 开发库

        // SonarQube 配置
        SONAR_URL = 'http://10.33.9.41:9000'
        SONAR_PROJECT_KEY = 'lab-platform'
        SONAR_PROJECT_NAME = 'lab-platform'
        SONAR_PROJECT_VERSION = "0.0.1-build-${BUILD_NUMBER}" // 使用双引号解析变量

        SONAR_JAVA_SOURCE = '21'
        // 排除规则保持不变
        SONAR_EXCLUSIONS = '**/src/test/**,**/target/**,**/*.xml'

    }

    stages {
        stage('Pull Code') {
            steps {
               checkout scm
            }
        }
        stage('Test & Build') {
            steps {

                script {
                    sh 'chmod +x mvnw'
                    // --- ⚠️ 修改点 2: 确保所有模块都编译 ---
                    // 运行 root 下的 install，确保子模块之间的依赖关系被正确解析
                    sh './mvnw clean install -DskipTests=false'
                }
                // --- ⚠️ 修改点 3: 递归收集测试报告 ---
                // 单模块是 target/*.xml，多模块必须加 **/ 才能找到子文件夹里的报告
                junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
            }
        }
        stage('SonarQube Analysis'){
            steps{
                script {
                    withSonarQubeEnv('sonar-server') {
                        sh """
                            ./mvnw sonar:sonar \
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                            -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                            -Dsonar.projectVersion=${SONAR_PROJECT_VERSION} \
                            -Dsonar.exclusions=${SONAR_EXCLUSIONS} \
                            -Dsonar.coverage.jacoco.xmlReportPaths=**/target/site/jacoco/jacoco.xml
                        """
                    }
                }
            }
        }
        stage('DB Migration') {
            when {
                        anyOf {
                            branch 'main'
                            branch pattern: 'dev*', comparator: 'GLOB'
                            branch pattern: 'feature/*', comparator: 'GLOB'
                        }
                    }
            steps {
              script {
                    // === 2. 动态决定使用哪个数据库 ===
                    def targetDbUrl = ""
                    def envName = ""
                    def db_host = ""

                    if (env.BRANCH_NAME == 'main') {
                        targetDbUrl = env.DB_URL_MAIN
                        db_host = env.DB_HOST_PROD
                        envName = "生产/开发环境 (Main)"
                    } else {
                        // 任何非 main 分支 (dev, feature/xxx) 都去开发库
                        targetDbUrl = env.DB_URL_DEV
                        db_host = env.DB_HOST_DEV
                        envName = "开发环境 (Dev)"
                    }

                    echo ">>> 当前分支: ${env.BRANCH_NAME}"
                    echo ">>> 目标环境: ${envName}"
                    echo ">>> 目标主机: ${db_host}"
                    echo ">>> 数据库地址: ${targetDbUrl}"

                    // === 3. 执行 Flyway (注意使用 targetDbUrl 变量) ===
                    // 加上 flyway:repair 是为了开发方便，允许修改 checksum
                    sh """
                        ./mvnw -pl lab-start compile flyway:repair flyway:migrate \
                        -Dflyway.url=${targetDbUrl} \
                        -Dflyway.user=\$DB_CREDS_USR \
                        -Dflyway.password=\$DB_CREDS_PSW \
                        -Dflyway.locations=classpath:db/migration \
                        -Dflyway.baselineOnMigrate=true
                    """
                }
        }
    }
}
}
