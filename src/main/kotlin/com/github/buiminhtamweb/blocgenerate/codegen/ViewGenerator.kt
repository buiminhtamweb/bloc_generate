package com.github.buiminhtamweb.blocgenerate.codegen

import com.github.buiminhtamweb.blocgenerate.model.FileModel
import com.github.buiminhtamweb.blocgenerate.model.GeneratorConfig
import com.github.buiminhtamweb.blocgenerate.util.toSnakeCase

object ViewGenerator : FileGenerator {

    override fun generate(generatorConfig: GeneratorConfig): FileModel {
        val pakageName = generatorConfig.packageName

        val className = generatorConfig.className + "View"
        val fileName = generatorConfig.className.toSnakeCase() + "_view"

        val blocClassName = generatorConfig.className + "Bloc"
        val blocFileName = generatorConfig.className.toSnakeCase() + "_bloc"

        val viewContent = "" +
                "import 'package:flutter/material.dart';\n" +
                "import 'package:" + pakageName + "/base/base_state_bloc.dart';\n" +
                "\n" +
                "import '" + blocFileName + ".dart';\n" +
                "\n" +
                "class " + className + " extends StatefulWidget {\n" +
                "  @override\n" +
                "  _" + className + "State createState() => _" + className + "State();\n" +
                "}\n" +
                "\n" +
                "class _" + className + "State extends BaseStateBloc<" + className + ", " + blocClassName + "> {\n" +
                "  @override\n" +
                "  Widget build(BuildContext context) {\n" +
                "    return Scaffold(\n" +
                "      body: Container(\n" +
                "        child: Center(\n" +
                "          child: Text(\"Home Screen\"),\n" +
                "        ),\n" +
                "      ),\n" +
                "    );\n" +
                "  }\n" +
                "}";

        return FileModel(fileName, viewContent);
    }


}
