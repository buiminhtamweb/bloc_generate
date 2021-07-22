package com.github.buiminhtamweb.blocgenerate.codegen

import com.github.buiminhtamweb.blocgenerate.model.FileModel
import com.github.buiminhtamweb.blocgenerate.model.GeneratorConfig


interface FileGenerator {
    fun generate(generatorConfig: GeneratorConfig): FileModel
}
