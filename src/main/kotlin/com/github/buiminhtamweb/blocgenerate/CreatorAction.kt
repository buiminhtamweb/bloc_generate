package com.github.buiminhtamweb.blocgenerate


import com.github.buiminhtamweb.blocgenerate.codegen.BlocGenerator
import com.github.buiminhtamweb.blocgenerate.codegen.RouteGenerator
import com.github.buiminhtamweb.blocgenerate.codegen.ViewGenerator
import com.github.buiminhtamweb.blocgenerate.model.MobiusComponent
import com.intellij.ide.util.DirectoryChooserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.PlatformIcons
import com.jetbrains.lang.dart.util.PubspecYamlUtil.PUBSPEC_YAML
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream


class CreatorAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val view = event.getData(LangDataKeys.IDE_VIEW) ?: return
        val project = event.getData(CommonDataKeys.PROJECT) ?: return
        val directory = DirectoryChooserUtil.getOrChooseDirectory(view) ?: return

        val packageRoot = getPackageRoot(project, directory)
        val basePackageName = getProjectDartName(project);
        val rootPackagePath = packageRoot.virtualFile.path

        val dialog = CreatorDialog(basePackageName)
        if (dialog.showAndGet()) {
            ProgressManager.getInstance()
                .runProcessWithProgressSynchronously(
                    generateFiles(directory, dialog, rootPackagePath, packageRoot),
                    "Creating files",
                    false,
                    project
                )
        }
    }

    private fun getProjectDartName(project: Project): String {

        val pubspecYamlFiles: Collection<VirtualFile> = FilenameIndex.getVirtualFilesByName(
            project, PUBSPEC_YAML, GlobalSearchScope.projectScope(project)
        )

        for (pubspecFile in pubspecYamlFiles) {

            val yaml = Yaml()
            val obj: Map<String, Any> = yaml.load(pubspecFile.inputStream)

            print(obj);
            return obj.get("name").toString();
        }
        return "";
    }

    override fun update(event: AnActionEvent) {
        val presentation = event.presentation.apply {
            icon = PlatformIcons.PACKAGE_ICON
        }

        val project = event.getData(CommonDataKeys.PROJECT)
        val view = event.getData(LangDataKeys.IDE_VIEW)
        if (project == null || view == null) {
            presentation.isEnabledAndVisible = false
            return
        }

        val directories = view.directories
        if (directories.isEmpty()) {
            presentation.isEnabledAndVisible = false
            return
        }

        var isPackage = true
        val factory = PsiDirectoryFactory.getInstance(project)
//    for (directory in directories) {
//      if (factory.isPackage(directory!!)) {
//        isPackage = true
//        break
//      }
//    }

        presentation.isEnabledAndVisible = isPackage
    }

    private fun generateFiles(
        directory: PsiDirectory,
        dialog: CreatorDialog,
        rootPackagePath: String,
        packageRoot: PsiDirectory
    ): Runnable {
        return Runnable {
            val generatorConfig = dialog.generatorConfig

//      if (generatorConfig.addDependencyEnabled) {
//        // Adding Mobius gradle dependencies
//        addMobiusDependencies(directory)
//      }

            generatorConfig.mobiusComponents.forEach { component ->
                val fileModel = when (component) {
                    is MobiusComponent.Bloc -> BlocGenerator.generate(generatorConfig)

                    is MobiusComponent.Route -> RouteGenerator.generate(generatorConfig)

                    is MobiusComponent.View -> ViewGenerator.generate(generatorConfig)


                }
                var selectDir = "";
                if (directory.isDirectory) {
                    //Messages.showErrorDialog("Vui l??ng ch???n th?? m???c c???n t???o", "Error")
                    selectDir = directory.virtualFile.url.replace("file://", "")
                } else {
                    Thread(Runnable {
                        Messages.showErrorDialog("File ???? t???n t???i. Vui l??ng x??a file", "Error")
                    })
                    return@forEach
                }

                val filePath = selectDir + "/" + fileModel.fileName + ".dart";
                if (File(filePath).exists()) {
                    Thread(Runnable {
                        Messages.showErrorDialog("File ???? t???n t???i. Vui l??ng x??a file", "Error")
                    })

                } else {
//          File(filePath).createNewFile();
                    File(filePath).writeText(fileModel.fileContent);
                }
                // Refreshing directory once files are created
                packageRoot.virtualFile.refresh(false, true)
            }
        }
    }

//  private fun addMobiusDependencies(directory: PsiDirectory) {
//    // Getting the module based on the directory from which the creator is opened.
//    val dependencyHandler = DependencyHandler(directory)
//    Constants.mobiusArtifacts.forEach { artifact ->
//      dependencyHandler.addDependency(artifact)
//    }
//  }


    private fun getPackageRoot(project: Project, selectedDirectory: PsiDirectory): PsiDirectory {
        val directoryFactory = PsiDirectoryFactory.getInstance(project)
        var directory = selectedDirectory
        var parent = directory.parent

        while (parent != null && directoryFactory.isPackage(parent)) {
            directory = parent
            parent = parent.parent
        }

        return directory
    }

    private fun buildBasePackageName(packageRoot: PsiDirectory, directory: PsiDirectory): String {
        if (packageRoot.isEquivalentTo(directory)) {
            return ""
        }
        val root = packageRoot.virtualFile.path
        val current = directory.virtualFile.path
        return current.substring(root.length + 1).replace("/", DELIMITER)
    }

    companion object {
        private const val DELIMITER = "."
    }

//    fun getDartProjectName(pubspecYamlFile: VirtualFile): String? {
//        if (pubspecYamlFile == null) {
//           return ""
//        }
//        val yamlInfo = PubspecYamlUtil.getPubspecYamlInfo(pubspecYamlFile)
//        val name = yamlInfo?.get("name")
//        return if (name is String) name else null
//    }
}
