package com.github.buiminhtamweb.blocgenerate.codegen

import com.github.buiminhtamweb.blocgenerate.model.FileModel
import com.github.buiminhtamweb.blocgenerate.model.GeneratorConfig
import com.github.buiminhtamweb.blocgenerate.util.snakeToLowerCamelCase
import com.github.buiminhtamweb.blocgenerate.util.toSnakeCase
import java.util.*

object RouteGenerator : FileGenerator {

    override fun generate(generatorConfig: GeneratorConfig): FileModel {
        val className = generatorConfig.className + "Route"
        val fileName = generatorConfig.className.toSnakeCase() + "_route"
        val variName = className.snakeToLowerCamelCase();

        val viewClassName = generatorConfig.className + "View"
        val viewFileName = generatorConfig.className.toSnakeCase() + "_view"

        val blocClassName = generatorConfig.className + "Bloc"
        val blocFileName = generatorConfig.className.toSnakeCase() + "_bloc"
        val blocVariName = blocClassName.snakeToLowerCamelCase();

        val routeContent = "" +
                "import 'package:flutterbaseproject/repo/user_repo.dart';\n" +
                "import 'package:provider/provider.dart';\n" +
                "\n" +
                "import '" + blocFileName + ".dart';\n" +
                "import '" + viewFileName + ".dart';\n" +
                "\n" +
                "var " + variName + " = ProxyProvider<UserRepo, " + blocClassName + ">(\n" +
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
