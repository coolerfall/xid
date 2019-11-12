plugins {
  `java-library`
  `maven-publish`
  signing
}

group = "com.coolerfall"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("mavenPub") {
      artifactId = "xid"
      from(components["java"])
      pom {
        name.set("xid")
        description.set("A port of xid in Java")
        url.set("https://github.com/coolerfall/xid")
        licenses {
          name.set("The Apache License, Version 2.0")
          url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
        developers {
          developer {
            id.set("coolerfall")
            name.set("Vincent Cheung")
            email.set("coolingfall@gmail.com")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/xid.git")
          developerConnection.set("scm:git:ssh://github.com/xid.git")
          url.set("https://github.com/coolerfall/xid")
        }
      }
    }
  }

  val nexusUsername: String by project
  val nexusPassword: String by project

  repositories {
    maven {
      val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
      val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
      url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
      credentials {
        username = nexusUsername
        password = nexusPassword
      }
    }
  }
}

signing {
  if (!version.toString().endsWith("SNAPSHOT")) {
    sign(publishing.publications["mavenPub"])
  }
}

dependencies {
  testImplementation("junit:junit:4.12")
}

