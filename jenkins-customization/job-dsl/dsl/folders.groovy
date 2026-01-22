def ROOT_FOLDER = "e_commerce_v2"
def SERVICES_FOLDER = "services"

// Jenkins에 Folder Item 생성
folders(ROOT_FOLDER) {
    displayName("Monorepo")
    decsription("Auto-generated folder for monorepo jobs (Job DSL)")
}

// 하위 폴더로 monorepo/services 생성
folders("${ROOT_FOLDER}/${SERVICES_FOLDER}") {
    displayName("Services")
    decsription("Auto-generated folder for service multibranch jobs (Job DSL)")
}
