plugins {
    kotlin("jvm") version "2.2.0"
}

tasks.jar {
    manifest.attributes["Implementation-Title"] = "PDF-AS PDFBOX 3 Backend"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":pdf-as-lib"))
    api("org.apache.pdfbox", "pdfbox", "3.0.6")
    implementation("org.apache.pdfbox", "pdfbox-tools", "3.0.6")
    implementation("org.apache.pdfbox", "xmpbox", "3.0.6")
    implementation("org.apache.pdfbox", "preflight", "3.0.6")

    testImplementation("ch.qos.logback", "logback-classic", project.ext["logbackVersion"] as String)
    testImplementation(project(":signature-standards:sigs-pades"))
    testImplementation(project(":signature-standards:sigs-pkcs7detached"))
    testImplementation(group = "org.zeroturnaround", name = "zt-zip", version = "1.17")
}

tasks.register("releases", Copy::class) {
    dependsOn(tasks.jar, tasks.sourcesJar)
    from(tasks.jar.map { it.outputs.files })
    into(rootDir.resolve("releases/$version"))
}

tasks.test {
    useJUnit()
}
kotlin {
    jvmToolchain(17)
}