def ROOT_FOLDER = "monorepo"

// ✅ GitHub 정보
def GITHUB_OWNER   = "GroomCloudTeam2"
def GITHUB_REPO    = "e_commerce_v2"
def GITHUB_CRED_ID = "github-token"

multibranchPipelineJob("${ROOT_FOLDER}/mb-root") {
    displayName("e_commerce_v2 (root)")
    description("Root multibranch: ${GITHUB_OWNER}/${GITHUB_REPO} -> Jenkinsfile")

    branchSources {
        branchSource {
            source {
                github {
                    id("mb-root-${GITHUB_OWNER}-${GITHUB_REPO}".replaceAll('[^A-Za-z0-9_.-]', '_'))
                    repoOwner(GITHUB_OWNER)
                    repository(GITHUB_REPO)
                    credentialsId(GITHUB_CRED_ID)

                    repositoryUrl("https://github.com/${GITHUB_OWNER}/${GITHUB_REPO}.git")
                    configuredByUrl("https://github.com")
                }
            }
        }
    }

    factory {
        workflowBranchProjectFactory {
            scriptPath("Jenkinsfile")
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(30)
        }
    }

    // 웹훅 없으면 주기 스캔 켜두는 게 편함
    triggers {
        periodicFolderTrigger { interval("1h") }
    }
}
