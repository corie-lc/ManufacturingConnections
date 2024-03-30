package org.corieleclair.travellers

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.Stage
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path


class JobSelector {

    private fun getJobInfo(jobNumber: String) : List<String> {
        val configFilePath = "src/config.properties"
        val propsInput = FileInputStream(configFilePath)

        val prop: Properties = Properties()
        prop.load(propsInput)

        if(Files.exists(Path(prop.getProperty("MainDirectory") + "/jobs/" + jobNumber + ".txt"))){
            val bufferedReader: BufferedReader = File(prop.getProperty("MainDirectory") + "/jobs/" + jobNumber + ".txt").bufferedReader()
            val inputString = bufferedReader.use { it.readText() }

            // convert the file into an array

            val allInfo = inputString.split(";")
            print(allInfo)

            return allInfo
        } else{
            return listOf("null")
        }
    }






    fun moveJobsWindow() {
        val stage = Stage()

        stage.isResizable = false

        val moveJobLayout = GridPane()

        moveJobLayout.padding = Insets(0.0, 0.0, 0.0, 20.0) //margins around the whole grid


        val jobInformation = GridPane()
        jobInformation.hgap = 40.0
        jobInformation.vgap = 5.0


        val scrollPane = ScrollPane(jobInformation)
        scrollPane.minWidth = 500.0

        scrollPane.isFitToHeight = true
        scrollPane.isFitToWidth = true

        val root = BorderPane(scrollPane)
        root.minWidth = 600.0

        val jobNumberInput = TextField("Job Number")
        val buttonSearchForJob = Button("Search")


        moveJobLayout.add(jobNumberInput, 0, 0)
        moveJobLayout.add(buttonSearchForJob, 1, 0)
        moveJobLayout.add(root, 0, 1, 3, 1)



        buttonSearchForJob.setOnAction {
            jobInformation.children.clear()

            val jobInfo = getJobInfo(jobNumberInput.text)
            var jobLocation = ""


            var whereX = 0;
            var whereY = 0

            if (jobInfo[0] != "null"){
                jobInfo.removeLast()


                for (item in jobInfo) {
                    if (item.split(":")[0].replace("\n", "") == "job location"){
                        jobLocation = item.split(":")[1].toString()
                    }
                    val tempLabel = Label(item.split(":")[0].replace("\n", "") + "  :  " + item.split(":")[1].toString())
                    tempLabel.isUnderline = true

                    jobInformation.add(tempLabel, whereX, whereY)

                    whereY += 1

                    if (whereX == 0){
                        whereX = 1;
                    } else{
                        whereX = 0
                    }
                }

                // add comments

                val commentOne = TextField("")
                val buttonUpdateJob = Button("UpdateJob ->")
                jobInformation.add(Label("Comment"), 0, whereY, 2, 1)

                jobInformation.add(commentOne, 0, whereY + 1, 2, 1)
                jobInformation.add(Systems().getLocationDropDown(jobLocation), 0, whereY + 2, 2, 1)
                jobInformation.add(buttonUpdateJob, 0, whereY + 3, 2, 1)





            } else {
                jobInformation.children.add(Label("No Job Found or No Job Data Found"))
            }
        }



        stage.title = "Move Jobs"
        stage.scene = Scene(moveJobLayout, 650.0, 650.0)
        stage.show()
    }

}