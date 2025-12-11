pipeline {
    agent any
    environment {
        DB_CREDS = credentials('my-db-credentials-id')

        DB_URL = "jdbc:mysql://10.33.9.41:3306/mybatisPlus"

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
                git(
                    url: 'http://git@10.33.9.41:3000/hengyuTeam/lab-platform.git',
                    credentialsId: 'jenkins-gitea-token',
                    branch: 'main'
                )
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
            steps {
                echo '正在执行数据库迁移'
                // --- ⚠️ 修改点 5: Flyway 改为 classpath 加载 ---
                // 1. -pl lab-start: 只运行启动模块的插件
                // 2. compile: 必须先编译，把 resources 复制到 target 目录，Flyway 才能读到
                // 3. locations: 改为 classpath。因为 SQL 散落在各个 jar 包里，filesystem 读不到 jar 包内的文件
                sh """
                    ./mvnw -pl lab-start compile flyway:migrate \
                    -Dflyway.url=${DB_URL} \
                    -Dflyway.user=\$DB_CREDS_USR \
                    -Dflyway.password=\$DB_CREDS_PSW \
                    -Dflyway.locations=classpath:db/migration \
                    -Dflyway.baselineOnMigrate=true
                """
            }
        }
    }
}
