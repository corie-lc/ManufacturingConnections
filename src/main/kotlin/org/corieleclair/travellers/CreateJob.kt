package org.corieleclair.travellers

import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateJob {


    private fun createCSVInFolder(stuff: MutableList<List<String>>){

        var stuffParsed = ""

        // tracking date



        for(item in stuff){
            val tempItem = item[0] + ":" + item[1] + ";\n"
            stuffParsed += tempItem
        }

        stuffParsed += "date_time: " +  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()) + ";\n"



        try {
            val configFilePath = "src/config.properties"
            val propsInput = FileInputStream(configFilePath)

            val prop: Properties = Properties()
            prop.load(propsInput)

            print(stuff[0][1])

            val myWriter = FileWriter(prop.getProperty("MainDirectory") + "/jobs/" + stuff[0][1] + ".txt")
            myWriter.write(stuffParsed)
            myWriter.close()
            println("Successfully wrote to the file.")
        } catch (e: IOException) {
            println("An error occurred.")
            e.printStackTrace()
        }


    }


    fun createNewJob() {

        val stage = Stage()

        val parentBox = VBox()
        val jobInformationVbox = VBox()

        parentBox.children.add(jobInformationVbox)

        stage.scene = Scene(parentBox, 400.0, 400.0)

        val jobAttributes = Systems().getNewJobAttributes()

        for (item in jobAttributes){
            if(item == "job location"){
                val freshHBox = HBox()
                val tempLabel = Label(item)
                tempLabel.minWidth = 75.0
                freshHBox.children.add(tempLabel)
                freshHBox.children.add(Systems().getLocationDropDown("Select Default Position"))
                jobInformationVbox.children.add(freshHBox)
            } else {
                val freshHBox = HBox()
                val tempLabel = Label(item)
                tempLabel.minWidth = 75.0
                freshHBox.children.add(tempLabel)
                freshHBox.children.add(TextField())
                jobInformationVbox.children.add(freshHBox)
            }

        }

        val buttonCreateJob = Button("Create Job")

        var allNewJobInfo = mutableListOf<List<String>>()

        buttonCreateJob.setOnAction {
            for (item in jobInformationVbox.children){
                val tempHBox = item as HBox
                val tempLabel = tempHBox.children[0] as Label
                var text = ""

                if(tempLabel.text == "job location"){
                    val tempBox = tempHBox.children[1] as ComboBox<String>
                    text = tempBox.value.toString()
                    tempBox.value = "Select Default Position"

                } else {
                    val tempTextField = tempHBox.children[1] as TextField
                    text = tempTextField.text
                    tempTextField.text = ""
                }

                allNewJobInfo += mutableListOf(tempLabel.text ,text)

            }


            try{
                createCSVInFolder(allNewJobInfo)
                val a = Alert(Alert.AlertType.INFORMATION)
                a.title = "Job Has Been Added To Que"
                a.contentText = "Job Has Been Added To Que"
                // show the dialog
                a.showAndWait();
            } catch (exception: Exception) {
                val a = Alert(Alert.AlertType.ERROR)
                a.title = "There was an error. Please add to the github issues."
                a.contentText = exception.toString()
                // show the dialog
                a.showAndWait();
            }


        }


        parentBox.children.add(buttonCreateJob)
        stage.show()
    }
}