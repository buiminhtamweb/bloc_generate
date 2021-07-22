package com.github.buiminhtamweb.blocgenerate.codegen

import com.github.buiminhtamweb.blocgenerate.model.FileModel
import com.github.buiminhtamweb.blocgenerate.model.GeneratorConfig
import com.github.buiminhtamweb.blocgenerate.util.toCamelCase
import com.github.buiminhtamweb.blocgenerate.util.toSnakeCase

object RouteGenerator : FileGenerator {

    override fun generate(generatorConfig: GeneratorConfig): FileModel {
        val className = generatorConfig.className + "Route"
        val fileName = generatorConfig.className.toSnakeCase() + "_route"

        val viewClassName = generatorConfig.className + "View"
        val viewFileName = generatorConfig.className.toSnakeCase() + "_view"

        val blocClassName = generatorConfig.className + "Bloc"
        val blocFileName = generatorConfig.className.toSnakeCase() + "_bloc"
        val blocVariName = blocFileName.toCamelCase()

        val routeContent = "" +
                "import 'package:flutterbaseproject/repo/user_repo.dart';\n" +
                "import 'package:provider/provider.dart';\n" +
                "\n" +
                "import '" + blocFileName + ".dart';\n" +
                "import '" + viewFileName + ".dart';\n" +
                "\n" +
                "var homeRoute = ProxyProvider<UserRepo, " + blocClassName + ">(\n" +
                "  create: (context) {\n" +
                "    " + blocClassName + " " + blocVariName + " = " + blocClassName + "(\n" +
                "        userRepo: Provider.of<UserRepo>(context, listen: false));\n" +
                "\n" +
                "    return " + blocVariName + ";\n" +
                "  },\n" +
                "  update: (context, userRepo, " + blocVariName + ") {\n" +
                "    if (" + blocVariName + " != null) return " + blocVariName + ";\n" +
                "    return " + blocClassName + "(\n" +
                "      userRepo: userRepo,\n" +
                "    );\n" +
                "  },\n" +
                "  dispose: (context, " + blocVariName + ") => " + blocVariName + ".dispose(),\n" +
                "  child: " + viewClassName + "(),\n" +
                ");\n";

        return FileModel(fileName, routeContent);
    }


}
