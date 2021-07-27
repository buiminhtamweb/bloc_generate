package com.github.buiminhtamweb.blocgenerate.codegen

import com.github.buiminhtamweb.blocgenerate.model.FileModel
import com.github.buiminhtamweb.blocgenerate.model.GeneratorConfig
import com.github.buiminhtamweb.blocgenerate.util.toSnakeCase


object BlocGenerator : FileGenerator {

    override fun generate(generatorConfig: GeneratorConfig): FileModel {
        val pakageName = generatorConfig.packageName
        
        val className = generatorConfig.className + "Bloc"
        val fileName = generatorConfig.className.toSnakeCase() + "_bloc"

        val blocContent = "" +
                "import 'package:flutter/cupertino.dart';\n" +
                "import 'package: " + pakageName + "/base/base_bloc.dart';\n" +
                "import 'package: " + pakageName + "/repo/user_repo.dart';\n" +
                "\n" +
                "class " + className + " extends BaseBloc {\n" +
                "  final UserRepo userRepo;\n" +
                "\n" +
                "  " + className + "({\n" +
                "    required this.userRepo,\n" +
                "  });\n" +
                "\n" +
                "  @override\n" +
                "  void dispose() {\n" +
                "    super.dispose();\n" +
                "  }\n" +
                "}";

        return FileModel(fileName, blocContent);
    }


}
