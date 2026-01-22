def ROOT_FOLDER = "monorepo"

// ✅ 네 GitHub 정보로 수정
def GITHUB_OWNER   = "GroomCloudTeam2"
def GITHUB_REPO    = "e_commerce_V2"
def GITHUB_CRED_ID = "github-token"

multibranchPipelineJob("${ROOT_FOLDER}/mb-root") {
    displayName("mb-root")
    description("Root Jenkinsfile multibranch: ${GITHUB_OWNER}/${GITHUB_REPO} -> Jenkinsfile")

    branchSources {
        branchSource {
            source {
                github {
                    id("mb-root-${GITHUB_OWNER}-${GITHUB_REPO}".replaceAll('[^A-Za-z0-9_.-]', '_'))
                    repoOwner(GITHUB_OWNER)
                    repository(GITHUB_REPO)
                    credentialsId(GITHUB_CRED_ID)
                }
            }
            traits {
                gitHubBranchDiscovery { strategyId(3) }
                gitHubPullRequestDiscovery { strategyId(1) }
            }
        }
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath("Jenkinsfile")
        }
    }

    orphanedItemStrategy {
        discardOldItems { numToKeep(20) }
    }
}
