// jc_folders.groovy랑 동일한 파일명
def ROOT_FOLDER = "monorepo"
def SERVICES_FOLDER = "services"

// GitHub 정보
def GITHUB_OWNER = "GroomCloudTeam2"
def GITHUB_REPO = "e_commerce_v3"
def GITHUB_CRED_ID = "github-token"

// job : jenkins에서 생성될 Job 이름(식별자)
// display : jenkins UI에 보이는 표시 이름
// scriptPath : 해당 멀티브랜치가 "브랜치마다" 찾을 Jenkinsfile 경로
def SERVICES = [
        [job: "mb-user", display: "user", scriptPath: "Jenkinsfile"],
        [job: "mb-order", display: "order", scriptPath: "services/order/Jenkinsfile"],
        [job: "mb-payment", display: "payment", scriptPath: "services/payment/Jenkinsfile"],
        [job: "mb-product", display: "product", scriptPath: "services/product/Jenkinsfile"],
        [job: "mb-review", display: "review", scriptPath: "services/review/Jenkinsfile"]
]

// SERVICE 목록을 순회하며 멀티브랜치 Job 생성
// "${ROOT_FOLDER}/${SERVICES_FOLDER}/${svc.job}" = "monorepo/services/mb-order" 같은 "전체 경로"로 생성됨
SERVICES.each { svc ->
    multibranchPipelineJob("${ROOT_FOLDER}/${SERVICES_FOLDER}/${svc.job}") {
        displayName(svc.display)
        description("Service multibranch: ${GITHUB_OWNER}/${GITHUB_REPO} -> ${svc.scriptPath}")

        // 이 부분은 "jc_root.groovy"와 동일
        branchSources {
            branchSource {
                source {
                    github {
                        id("${svc.job}-${GITHUB_OWNER}-${GITHUB_REPO}".replaceAll('[^A-Za-z0-9_.-]', '_'))
                        repoOwner(GITHUB_OWNER)
                        repository(GITHUB_REPO)
                        credentialsId(GITHUB_CRED_ID)
                        repositoryUrl("https://github.com/${GITHUB_OWNER}/${GITHUB_REPO}")
                        configuredByUrl(false)   // owner/repo 기반으로 쓰겠다
                    }
                }
            }
        }

        factory {
            workflowBranchProjectFactory {
                scriptPath(svc.scriptPath)
            }
        }

        orphanedItemStrategy {
            discardOldItems { numToKeep(20) }
        }

        triggers {
            periodicFolderTrigger { interval("1h") }
        }
    }
}
