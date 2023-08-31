import org.springframework.boot.buildpack.platform.build.PullPolicy.*

plugins {
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	id("java")
//	id("org.springframework.experimental.aot") version "0.11.3"
}

group = "io.humourmind"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	maven { url = uri("https://repo.spring.io/release") }
	mavenCentral()
}

tasks.bootJar {
	layered {
		isEnabled = true
	}
}

tasks.bootBuildImage {
	environment = mapOf("BP_JVM_VERSION" to "17.*")
//	environment = mapOf("BP_NATIVE_IMAGE" to "true")
	imageName = "humourmind/kns/${project.name}:${project.version}"
//	publish = true
	pullPolicy = IF_NOT_PRESENT
//	isVerboseLogging = true
//	builder = "paketobuildpacks/builder:tiny"
//	builder = "humourmind/paketo-java-builder-tiny@sha256:40be20ed070cce98f6cfa3b9b588919502cc5f4f7ee330d19ec28cddf7d985bb"
}

dependencies {
	compileOnly("org.projectlombok:lombok")
//	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")

//	implementation("org.springframework.boot:spring-boot-starter-data-cassandra")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")
	implementation("io.github.classgraph:classgraph:4.8.162")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
