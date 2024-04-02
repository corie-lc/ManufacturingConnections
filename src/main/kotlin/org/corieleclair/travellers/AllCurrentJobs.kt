package org.corieleclair.travellers

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.nameWithoutExtension


class AllCurrentJobs {
    private val stage = Stage()
    private val allJobsLayout = VBox()
    private val scrollPane = ScrollPane(allJobsLayout)
    private val root = BorderPane(scrollPane)
    private val scene = Scene(root)

    enum class Filter {
        JOB_LOCATION,
        NONE
    }
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
    fun allCurrentJobsWindow(filter : Filter =  Filter.NONE, filterString: String = "NONE"){
        val jobSelector = JobSelector()
        val allFiles = getAllFilesInJobFolder()


        stage.isResizable = false

        scrollPane.minWidth = 600.0

        scrollPane.isFitToHeight = true
        scrollPane.isFitToWidth = true

        root.minWidth = 600.0
        root.minHeight = 550.0

        for(item in allFiles){
            val tempButton = Button(item)
            val tempJobInfo = Systems().getJobInfo(item)
            val tempHBox = HBox()

            println("----------------------------------------------------------------------")
            println(tempJobInfo)
            val tempLocationButton = Button(tempJobInfo[1].split(":")[1])

            if(filter == Filter.JOB_LOCATION){
                if (tempJobInfo[1].split(":")[0] == filterString){
                    tempButton.minWidth = 350.0
                    tempButton.setOnAction {
                        jobSelector.loadJobIntoWindow(item)
                    }

                    tempHBox.children.add(tempButton)
                    tempHBox.children.add(tempLocationButton)

                    allJobsLayout.children.add(tempButton)
                } else{
                    continue
                }
            } else {
                println("HERE")
                tempButton.minWidth = 350.0

                tempButton.setOnAction {
                    jobSelector.loadJobIntoWindow(item)
                }

                tempLocationButton.setOnAction {
                    stage.close()
                    AllCurrentJobs().allCurrentJobsWindow(Filter.JOB_LOCATION, tempJobInfo[1].split(":")[0])
                }

                tempHBox.children.add(tempButton)
                tempHBox.children.add(tempLocationButton)

                allJobsLayout.children.add(tempHBox)
            }
        }

        if(!stage.isShowing){
            stage.scene = scene
            stage.show()
        }
    }
}