//package com.github.buiminhtamweb.blocgenerate.util
//
//import com.intellij.openapi.fileEditor.FileDocumentManager
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.roots.ProjectRootManager
//import com.intellij.openapi.util.Key
//import com.intellij.openapi.util.Pair
//import com.intellij.openapi.vfs.VfsUtilCore
//import com.intellij.openapi.vfs.VirtualFile
//import com.jetbrains.lang.dart.ide.errorTreeView.DartProblemsView
//import com.jetbrains.lang.dart.ide.errorTreeView.DartProblemsViewSettings.ScopedAnalysisMode
//import com.jetbrains.lang.dart.util.PubspecYamlUtil
//import org.yaml.snakeyaml.DumperOptions
//import org.yaml.snakeyaml.Yaml
//import org.yaml.snakeyaml.constructor.SafeConstructor
//import org.yaml.snakeyaml.nodes.Tag
//import org.yaml.snakeyaml.representer.Representer
//import org.yaml.snakeyaml.resolver.Resolver
//import java.io.IOException
//
//class PubspecYamlUtil {
//
//    val PUBSPEC_YAML = "pubspec.yaml"
//    val NAME = "name"
//    val DEPENDENCIES = "dependencies"
//    val DEV_DEPENDENCIES = "dev_dependencies"
//    val DEPENDENCY_OVERRIDES = "dependency_overrides"
//    val PATH = "path"
//    val LIB_DIR_NAME = "lib"
//    private val MOD_STAMP_TO_PUBSPEC_NAME = Key.create<Pair<Long, Map<String, Any>>>("MOD_STAMP_TO_PUBSPEC_NAME")
//
//    fun PubspecYamlUtil() {}
//
//    fun isPubspecFile(file: VirtualFile): Boolean {
//        if (file == null) {
//            return false
//        }
//        return !file.isDirectory && file.name == "pubspec.yaml"
//    }
//
//    fun findPubspecYamlFile(project: Project, contextFile: VirtualFile): VirtualFile? {
//        if (project == null) {
//            return null
//        }
//        if (contextFile == null) {
//            return null
//        }
//        val fileIndex = ProjectRootManager.getInstance(project).fileIndex
//        var current = contextFile
//        var parent = if (contextFile.isDirectory) contextFile else contextFile.parent
//        val isPackageScopedAnalysis = DartProblemsView.getScopeAnalysisMode(project) == ScopedAnalysisMode.DartPackage
//        while (parent != null && ("lib" == current.name || isPackageScopedAnalysis || fileIndex.isInContent(parent))) {
//            current = parent
//            val file = parent.findChild("pubspec.yaml")
//            if (file != null && !file.isDirectory) {
//                return file
//            }
//            parent = parent.parent
//        }
//        return null
//    }
//
//    fun getDartProjectName(pubspecYamlFile: VirtualFile): String? {
//        if (pubspecYamlFile == null) {
//            return ""
//        }
//        val yamlInfo = getPubspecYamlInfo(pubspecYamlFile)
//        val name = yamlInfo?.get("name")
//        return if (name is String) name else null
//    }
//
//
//    private fun getPubspecYamlInfo(pubspecYamlFile: VirtualFile): Map<*, *> {
//        if (pubspecYamlFile == null) {
//            return null
//        }
//        var data: Pair<Long, Map<String, Any>>? = pubspecYamlFile.getUserData(MOD_STAMP_TO_PUBSPEC_NAME)
//        val documentManager = FileDocumentManager.getInstance()
//        val cachedDocument = documentManager.getCachedDocument(pubspecYamlFile)
//        val currentTimestamp = cachedDocument?.modificationStamp ?: pubspecYamlFile.modificationCount
//        val cachedTimestamp = Pair.getFirst(data) as Long
//        if (cachedTimestamp == null || cachedTimestamp != currentTimestamp) {
//            data = null
//            pubspecYamlFile.putUserData(MOD_STAMP_TO_PUBSPEC_NAME, null)
//            try {
//                val pubspecYamlInfo: Map<*, *>?
//                pubspecYamlInfo = if (cachedDocument != null) {
//                    loadPubspecYamlInfo(cachedDocument.text)
//                } else {
//                    loadPubspecYamlInfo(VfsUtilCore.loadText(pubspecYamlFile))
//                }
//                if (pubspecYamlInfo != null) {
//                    data = Pair.create(currentTimestamp, pubspecYamlInfo)
//                    pubspecYamlFile.putUserData(MOD_STAMP_TO_PUBSPEC_NAME, data)
//                }
//            } catch (var7: IOException) {
//            }
//        }
//        return Pair.getSecond(data) as Map<*, *>
//    }
//
//    private fun loadPubspecYamlInfo(pubspecYamlFileContents: String): Map<String?, Any?>? {
//        if (pubspecYamlFileContents == null) {
//            return null
//        }
//        val yaml = Yaml(SafeConstructor(), Representer(), DumperOptions(), object : Resolver() {
//            override fun addImplicitResolvers() {
//                addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO")
//                addImplicitResolver(Tag.NULL, NULL, "~nN\u0000")
//                addImplicitResolver(Tag.NULL, EMPTY, null as String?)
//                addImplicitResolver(Tag("tag:yaml.org,2002:value"), VALUE, "=")
//                addImplicitResolver(Tag.MERGE, MERGE, "<")
//            }
//        })
//        return try {
//            return yaml.load<Any>(pubspecYamlFileContents) as Map<String?, Any?>
//        } catch (var3: Exception) {
//            return null
//        }
//    }
//}