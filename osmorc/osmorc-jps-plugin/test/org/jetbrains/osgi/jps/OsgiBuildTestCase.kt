/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.osgi.jps

import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.jps.builders.BuildResult
import org.jetbrains.jps.builders.JpsBuildTestCase
import org.jetbrains.jps.maven.model.impl.MavenProjectConfiguration
import org.jetbrains.jps.model.java.JavaResourceRootType
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.jps.model.java.JpsJavaExtensionService
import org.jetbrains.jps.model.module.JpsModule
import org.jetbrains.jps.util.JpsPathUtil
import org.jetbrains.osgi.jps.build.OsmorcBuilder
import org.jetbrains.osgi.jps.model.JpsOsmorcExtensionService
import org.jetbrains.osgi.jps.model.JpsOsmorcModuleExtension
import org.jetbrains.osgi.jps.model.ManifestGenerationMode
import org.jetbrains.osgi.jps.model.impl.JpsOsmorcModuleExtensionImpl
import org.jetbrains.osgi.jps.model.impl.OsmorcModuleExtensionProperties
import java.io.File
import java.util.Enumeration
import java.util.jar.JarFile
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class OsgiBuildTestCase : JpsBuildTestCase() {
  fun module(name: String, osgi: Boolean = true): JpsModule {
    val module = addModule(name)

    val contentRoot = JpsPathUtil.pathToUrl(getAbsolutePath(name))
    module.getContentRootsList().addUrl(contentRoot)
    module.addSourceRoot("${contentRoot}/src", JavaSourceRootType.SOURCE)
    module.addSourceRoot("${contentRoot}/res", JavaResourceRootType.RESOURCE)
    JpsJavaExtensionService.getInstance().getOrCreateModuleExtension(module).setOutputUrl("${contentRoot}/out")

    if (osgi) {
      val extension = JpsOsmorcModuleExtensionImpl(OsmorcModuleExtensionProperties())
      extension.getProperties().myJarFileLocation = "${name}.jar"
      module.getContainer().setChild(JpsOsmorcModuleExtension.ROLE, extension)
    }

    return module
  }

  fun extension(module: JpsModule) = JpsOsmorcExtensionService.getExtension(module)!! as JpsOsmorcModuleExtensionImpl

  fun bndBuild(module: JpsModule) {
    val properties = extension(module).getProperties()
    properties.myManifestGenerationMode = ManifestGenerationMode.Bnd
    properties.myBndFileLocation = "bnd.bnd"
  }

  fun ideaBuild(module: JpsModule) {
    val properties = extension(module).getProperties()
    properties.myManifestGenerationMode = ManifestGenerationMode.OsmorcControlled
    properties.myBundleSymbolicName = "main"
    properties.myBundleVersion = "1.0.0"
    properties.myAdditionalProperties = hashMapOf("Export-Package" to "main")
  }

  fun createMavenConfig(module: JpsModule) {
    val file = File(myDataStorageRoot, MavenProjectConfiguration.CONFIGURATION_FILE_RELATIVE_PATH)
    FileUtil.writeToFile(file, """
        <?xml version="1.0" encoding="UTF-8"?>
        <maven-project-configuration>
          <resource-processing>
            <maven-module name="${module.getName()}">
              <MavenModuleResourceConfiguration>
                <directory>${JpsPathUtil.urlToOsPath(module.getContentRootsList().getUrls()[0])}</directory>
              </MavenModuleResourceConfiguration>
            </maven-module>
          </resource-processing>
        </maven-project-configuration>""".trim())
  }

  fun BuildResult.assertBundleCompiled(module: JpsModule) {
    assertSuccessful()
    assertCompiled(OsmorcBuilder.ID, "${module.getName()}/${extension(module).getProperties().myJarFileLocation}")
  }

  fun assertJar(module: JpsModule, expected: Set<String>) {
    JarFile(extension(module).getJarFileLocation()).use {
      val actual = it.entries().asSequence().filter { !it.isDirectory() }.map { it.getName() }.toSet()
      assertEquals(expected, actual)
    }
  }

  fun assertManifest(module: JpsModule, expected: Set<String>) {
    val instrumental = setOf("Bnd-LastModified", "Tool", "Created-By")
    val required = setOf("Manifest-Version", "Bundle-ManifestVersion", "Require-Capability")
    val sorting = setOf("Export-Package", "Import-Package")

    JarFile(extension(module).getJarFileLocation()).use {
      val actual = it.getManifest()!!.getMainAttributes()!!
          .map { Pair(it.key.toString(), it.value.toString()) }
          .filter {
            if (it.first in instrumental) false
            else if (it.first in required) { assertNotNull(it.second); false }
            else true
          }
          .map { "${it.first}=${if (it.first in sorting) it.second.split(',').sort().join(",") else it.second}" }
          .toSet()
      assertEquals(expected, actual)
    }
  }

  private fun JarFile.use(block: (JarFile) -> Unit) {
    try { block(this) } finally { close() }
  }

  private fun<T> Enumeration<T>.asSequence(): Sequence<T> = object : Sequence<T> {
    override fun iterator(): Iterator<T> = this@asSequence.iterator()
  }
}