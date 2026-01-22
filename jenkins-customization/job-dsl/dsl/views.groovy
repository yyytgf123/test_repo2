def ROOT_FOLDER = "monorepo"
def SERVICES_FOLDER = "services"

folder(ROOT_FOLDER) {
    views {
        listView("All") {
            description("All jobs under ${ROOT_FOLDER}")
            jobs { regex("^${ROOT_FOLDER}/.*") }
            columns {
                status()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
        }

        listView("Services") {
            description("Service multibranch jobs under ${ROOT_FOLDER}/${SERVICES_FOLDER}")
            jobs { regex("^${ROOT_FOLDER}/${SERVICES_FOLDER}/.*") }
            columns {
                status()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
        }
    }
}
