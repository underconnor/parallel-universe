rootProject.name = "parallel-universe"

val api = "${rootProject.name}-api"
val core = "${rootProject.name}-core"
val debug = "${rootProject.name}-plugin"
val publish = "${rootProject.name}-publish"

include(api, core, debug, publish)
