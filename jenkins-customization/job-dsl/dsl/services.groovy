def ROOT_FOLDER = "monorepo"
def SERVICES_FOLDER = "services"

// ✅ 네 GitHub 정보로 수정
def GITHUB_OWNER   = "GroomCloudTeam2"
def GITHUB_REPO    = "e_commerce_V2"
def GITHUB_CRED_ID = "github-token" // Jenkins Credentials ID

def SERVICES = [
        [job: "mb-user",    display: "user",    scriptPath: "services/user/Jenkinsfile"],
        [job: "mb-order",   display: "order",   scriptPath: "services/order/Jenkinsfile"],
        [job: "mb-payment", display: "payment", scriptPath: "services/payment/Jenkinsfile"],
        [job: "mb-product", display: "product", scriptPath: "services/product/Jenkinsfile"],
        [job: "mb-review", display: "review", scriptPath: "services/review/Jenkinsfile"]
]

SERVICES.each { svc ->

    multibranchPipelineJob("${ROOT_FOLDER}/${SERVICES_FOLDER}/${svc.job}") {
        displayName(svc.display)
        description("Service multibranch: ${GITHUB_OWNER}/${GITHUB_REPO} -> ${svc.scriptPath}")

        // 1) 브랜치/PR 소스(GitHub)
        branchSources {
            branchSource {
                source {
                    github {
                        // Jenkins 내부 식별자(중복 방지용)
                        id("${svc.job}-${GITHUB_OWNER}-${GITHUB_REPO}".replaceAll('[^A-Za-z0-9_.-]', '_'))
                        repoOwner(GITHUB_OWNER)
                        repository(GITHUB_REPO)
                        credentialsId(GITHUB_CRED_ID)
                    }
                }

                // 2) 탐색 규칙(브랜치/PR)
                traits {
                    // 브랜치 전부 탐색(보통 3=All branches)
                    gitHubBranchDiscovery { strategyId(3) }

                    // (옵션) 브랜치 이름 필터 (플러그인/DSL 버전에 따라 없을 수 있음)
                    // headWildcardFilter {
                    //   includes("main develop feature/*")
                    //   excludes("")
                    // }
                }
            }
        }

        // 3) 이 멀티브랜치가 “어느 Jenkinsfile”을 사용할지
        factory {
            workflowBranchProjectFactory {
                scriptPath(svc.scriptPath)
            }
        }

        // 4) 오래된 브랜치 잡 정리
        orphanedItemStrategy {
            discardOldItems {
                numToKeep(20) // 최근 20개 브랜치만 유지
                // daysToKeep(14)
            }
        }

        // 5) 스캔 트리거(웹훅 쓰면 보통 끔)
        // triggers {
        //   periodicFolderTrigger { interval("12h") }
        // }
    }
}
