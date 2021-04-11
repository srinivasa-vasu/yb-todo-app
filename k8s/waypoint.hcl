project = "todo"

# An application to deploy.
app "todo" {
    labels = {
        "service" = "todo",
        "env"     = "dev"
    }

    build {
        use "pack" {
            builder     = "paketobuildpacks/builder:base"
            disable_entrypoint = false
        }
        registry {
            use "docker" {
                image = "humourmind/todo"
                tag   = "1.0"
//                local = true
            }
        }
    }

    deploy {
        use "kubernetes" {
//            probe_path = "/actuator/health/readiness"
            replicas = 1
            service_port = 8080
        }
    }

    release {
        use "kubernetes" {
        }
    }
}
