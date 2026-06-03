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
    val pdfboxVersion = project.ext["pdfboxVersion"] as String
    api("org.apache.pdfbox", "pdfbox", pdfboxVersion)
    implementation("org.apache.pdfbox", "pdfbox-tools", pdfboxVersion)
    implementation("org.apache.pdfbox", "xmpbox", pdfboxVersion)
    implementation("org.apache.pdfbox", "preflight", pdfboxVersion)
    implementation("org.apache.pdfbox", "jbig2-imageio", "3.0.5")

    testImplementation("ch.qos.logback", "logback-classic", project.ext["logbackVersion"] as String)
    testImplementation(project(":signature-standards:sigs-pades"))
    testImplementation(project(":signature-standards:sigs-pkcs7detached"))
    testImplementation(group = "org.zeroturnaround", name = "zt-zip", version = project.ext["ztZipVersion"] as String)
}

tasks.register("releases", Copy::class) {
    dependsOn(tasks.jar, tasks.sourcesJar)
    from(tasks.jar.map { it.outputs.files })
    into(rootDir.resolve("releases/$version"))
}

kotlin {
    jvmToolchain(17)
}