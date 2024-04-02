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


    private val parentBox = VBox()
    private val jobInformationVbox = VBox()
    private val jobAttributes = Systems().getNewJobAttributes()
    private val scene = Scene(parentBox, 400.0, 400.0)
    private val stage = Stage()


    fun createCSVInFolder(stuff: MutableList<String>){

        var stuffParsed = ""

        // tracking date



        for(item in stuff){
            val tempItem = item + ";\n"
            stuffParsed += tempItem
        }

        //stuffParsed += "date_time: " +  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()) + ";\n"



        try {
            val configFilePath = "src/config.properties"
            val propsInput = FileInputStream(configFilePath)

            val prop: Properties = Properties()
            prop.load(propsInput)

            print(stuff[0][1])

            val myWriter = FileWriter(prop.getProperty("MainDirectory") + "/jobs/" + stuff[0].split(":")[1] + ".txt")
            myWriter.write(stuffParsed)
            myWriter.close()
            println("Successfully wrote to the file.")
            val a = Alert(Alert.AlertType.INFORMATION)
            a.title = "Job Has Been Added To Que"
            a.contentText = "Job Has Been Added To Que"
            // show the dialog
            a.showAndWait();
        } catch (e: IOException) {
            println("An error occurred.")
            val a = Alert(Alert.AlertType.ERROR)
            a.title = "There was an error. Please add to the github issues."
            a.contentText = e.toString()
            // show the dialog
            a.showAndWait();
            e.printStackTrace()
        }


    }


    fun createNewJob() {


        parentBox.children.clear()
        parentBox.children.add(jobInformationVbox)

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

        var allNewJobInfo = mutableListOf<String>()

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

                allNewJobInfo += tempLabel.text + ":" + text

            }


            createCSVInFolder(allNewJobInfo)


        }


        parentBox.children.add(buttonCreateJob)
        if(!stage.isShowing){
            stage.scene = scene
            stage.show()
        }
    }
}