def SEED_JOB_NAME = "seed"

// ✅ 네 레포/브랜치/크레덴셜로 수정
def REPO_URL = "https://github.com/GroomCloudTeam2/e_commerce_V2.git"
def BRANCH   = "main"
def GITHUB_CRED_ID  = "github-token"

pipelineJob(SEED_JOB_NAME) {
    description("Seed job: applies Job DSL scripts under job-dsl/dsl/**")

    definition {
        cps {
            sandbox(true)
            script("""
pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        git url: '${REPO_URL}', branch: '${BRANCH}', credentialsId: '${GITHUB_CRED_ID}'
      }
    }

    stage('Apply Job DSL') {
      steps {
        jobDsl(
          targets: 'job-dsl/dsl/**/*.groovy',
          removedJobAction: 'DELETE',
          removedViewAction: 'DELETE',
          sandbox: true
        )
      }
    }
  }
}
""")
        }
    }
}
