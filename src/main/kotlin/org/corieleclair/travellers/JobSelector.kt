package org.corieleclair.travellers

import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.Stage

class JobSelector {
    private val stage = Stage()
    private val moveJobLayout = GridPane()
    private val jobInformation = GridPane()
    private val scrollPane = ScrollPane(jobInformation)
    private val root = BorderPane(scrollPane)
    private var showing = false



    fun loadJobIntoWindow(jobNumberInput: String){
        if(!showing){
            moveJobsWindow()
        }

        jobInformation.children.clear()

        val jobInfo = Systems().getJobInfo(jobNumberInput)
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
            val jobLocationDropdown = Systems().getLocationDropDown(jobLocation)




            jobInformation.add(Label("Comment"), 0, whereY, 2, 1)

            jobInformation.add(commentOne, 0, whereY + 1, 2, 1)
            jobInformation.add(jobLocationDropdown, 0, whereY + 2, 2, 1)
            jobInformation.add(buttonUpdateJob, 0, whereY + 3, 2, 1)

            buttonUpdateJob.setOnAction {
                val tempJobInfo = jobInfo.toMutableList()
                tempJobInfo[1] = "job location :" + jobLocationDropdown.value.toString()

                if(commentOne.text != ""){
                    tempJobInfo.add(tempJobInfo.count(), "comment1: " + commentOne.text)
                }

                CreateJob().createCSVInFolder(tempJobInfo)
                loadJobIntoWindow(jobNumberInput)
            }





        } else {
            jobInformation.children.add(Label("No Job Found or No Job Data Found"))
        }
    }






    fun moveJobsWindow(): GridPane {



        stage.isResizable = false


        moveJobLayout.padding = Insets(0.0, 0.0, 0.0, 20.0) //margins around the whole grid
        jobInformation.hgap = 40.0
        jobInformation.vgap = 5.0


        scrollPane.minWidth = 500.0

        scrollPane.isFitToHeight = true
        scrollPane.isFitToWidth = true

        root.minWidth = 600.0

        val jobNumberInput = TextField("Job Number")
        val buttonSearchForJob = Button("Search")


        moveJobLayout.add(jobNumberInput, 0, 0)
        moveJobLayout.add(buttonSearchForJob, 1, 0)
        moveJobLayout.add(root, 0, 1, 3, 1)



        buttonSearchForJob.setOnAction {
            loadJobIntoWindow(jobNumberInput.text)
        }



        stage.title = "Move Jobs"
        stage.scene = Scene(moveJobLayout, 650.0, 650.0)
        stage.show()
        showing = true

        return jobInformation
    }

}