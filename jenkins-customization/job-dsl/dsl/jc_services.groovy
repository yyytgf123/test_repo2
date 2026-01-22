// jc_folders.groovy랑 동일한 파일명
def ROOT_FOLDER = "monorepo"
def SERVICES_FOLDER = "services"

// ✅ GitHub 정보
def GITHUB_OWNER   = "GroomCloudTeam2"
def GITHUB_REPO    = "e_commerce_v2"
def GITHUB_CRED_ID = "github-token"

// ✅ 네 모노레포 구조에 맞게 Jenkinsfile 위치만 정확히
def SERVICES = [
        [job: "mb-user",    display: "user",    scriptPath: "Jenkinsfile"],
        [job: "mb-order",   display: "order",   scriptPath: "services/order/Jenkinsfile"],
        [job: "mb-payment", display: "payment", scriptPath: "services/payment/Jenkinsfile"],
        [job: "mb-product", display: "product", scriptPath: "services/product/Jenkinsfile"],
        [job: "mb-review",  display: "review",  scriptPath: "services/review/Jenkinsfile"]
]

SERVICES.each { svc ->
    multibranchPipelineJob("${ROOT_FOLDER}/${SERVICES_FOLDER}/${svc.job}") {
        displayName(svc.display)
        description("Service multibranch: ${GITHUB_OWNER}/${GITHUB_REPO} -> ${svc.scriptPath}")

        branchSources {
            branchSource {
                source {
                    github {
                        id("${svc.job}-${GITHUB_OWNER}-${GITHUB_REPO}".replaceAll('[^A-Za-z0-9_.-]', '_'))
                        repoOwner(GITHUB_OWNER)
                        repository(GITHUB_REPO)
                        credentialsId(GITHUB_CRED_ID)
                    }
                }
                traits {
                    gitHubBranchDiscovery { strategyId(3) }
                    // 필요하면 PR도(플러그인/DSL API 차이로 실패하면 주석 유지)
                    // gitHubPullRequestDiscovery { strategyId(1) }
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
