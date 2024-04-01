package org.corieleclair.travellers

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.nameWithoutExtension


class AllCurrentJobs {
    private fun getAllFilesInJobFolder(): MutableList<String> {
        val directoryPath = Systems().getProperty("MainDirectory") + "/jobs/"
        val listOfFiles = mutableListOf<String>()

        // create a Path object for the specified directory

        // create a Path object for the specified directory
        val directory: Path = Paths.get(directoryPath)

        // use DirectoryStream to list files which are present in specific
        Files.newDirectoryStream(directory).use { stream ->

            //with forEach loop get all the path of files present in directory
            for (file in stream) {
                listOfFiles.add(file.nameWithoutExtension)
            }
        }

        return listOfFiles
    }
    fun allCurrentJobsWindow(){
        val jobSelector = JobSelector()
        val allFiles = getAllFilesInJobFolder()

        val stage = Stage()

        stage.isResizable = false

        val allJobsLayout = VBox()


        val scrollPane = ScrollPane(allJobsLayout)
        scrollPane.minWidth = 500.0

        scrollPane.isFitToHeight = true
        scrollPane.isFitToWidth = true

        val root = BorderPane(scrollPane)
        root.minWidth = 600.0

        for(item in allFiles){
            val tempButton = Button(item)
            tempButton.setOnAction {
                jobSelector.loadJobIntoWindow(item)
            }

            allJobsLayout.children.add(tempButton)
        }

        stage.scene = Scene(root)
        stage.show()
    }
}