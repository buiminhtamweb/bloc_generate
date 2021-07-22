package com.github.buiminhtamweb.blocgenerate.model

class FileModel {
    var fileName: String = "";
    var fileContent: String = "";

    constructor(fileName: String, fileContent: String) {
        this.fileName = fileName
        this.fileContent = fileContent
    }

    override fun toString(): String {
        return "FileModel(fileName='$fileName', fileContent='$fileContent')"
    }


}