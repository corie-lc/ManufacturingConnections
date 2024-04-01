package org.corieleclair.travellers

import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path
import kotlin.system.exitProcess


class Systems {

    private fun isConfigFileSetupCorrect() : Boolean{
        val attributes = getNewJobAttributes()


        if (attributes.isNotEmpty()){
            println(attributes[0])
            return attributes[0] == "job number"
        }

        return false
    }

    fun getProperty(property: String): String {
        val configFilePath = "src/config.properties"
        val propsInput = FileInputStream(configFilePath)

        val prop: Properties = Properties()
        prop.load(propsInput)

        return prop.getProperty(property)
    }



    fun getNewJobAttributes(): List<String> {
        val configFilePath = "src/config.properties"
        val propsInput = FileInputStream(configFilePath)

        val prop: Properties = Properties()
        prop.load(propsInput)


        val list = prop.getProperty("job_properties").split(",")
        list.removeLast()
        return list
    }

    private fun getJobLocations() : List<String> {
        val configFilePath = "src/config.properties"
        val propsInput = FileInputStream(configFilePath)

        val prop: Properties = Properties()
        prop.load(propsInput)


        val list = prop.getProperty("job_locations").split(",")
        list.removeLast()
        return list
    }

    fun getLocationDropDown(jobLocation : String) : ComboBox<String> {
        val locationBox = ComboBox<String>()
        val data = FXCollections.observableArrayList(getJobLocations())
        println(jobLocation)

        locationBox.items = data
        locationBox.value = jobLocation

        return locationBox
    }


    fun getCompanyName(fileString: String): String {
        val bufferedReader: BufferedReader = File(fileString).bufferedReader()
        val inputString = bufferedReader.use { it.readText() }


        // convert the file into an array

        return inputString.split(";")[1]
    }

    fun selectMainDirectory(stage: Stage, prop: Properties, error: String) {
        val alert =
            Alert(
                Alert.AlertType.WARNING,
                (error),
                ButtonType.OK,
                ButtonType.CANCEL
            )
        alert.title = error
        val result = alert.showAndWait()

        if (result.get() == ButtonType.OK) {
            val directoryChooser = DirectoryChooser()


            val selectedDirectory = directoryChooser.showDialog(stage)
            prop["MainDirectory"] = selectedDirectory.toString()
            prop.store(FileWriter("src/config.properties"), "store to properties file");
            stage.close()
        } else{
            exitProcess(0)
        }
    }

    fun runSystemChecks(primaryLayout: VBox, stage: Stage): Boolean{
        val configFilePath = "src/config.properties"
        val propsInput = FileInputStream(configFilePath)



        val prop: Properties = Properties()
        prop.load(propsInput)


        if (prop.getProperty("MainDirectory") == null){
            val alert =
                Alert(
                    Alert.AlertType.ERROR,
                    ("Failed config systems check, Please reinstall the jar and set up your system again. This should not have happened."),
                    ButtonType.OK,
                    ButtonType.CANCEL
                )
            alert.title = "Failed config systems check"
            alert.showAndWait()
            exitProcess(0)
        }

        if(!Files.isWritable(Path(prop.getProperty("MainDirectory")))){
            selectMainDirectory(stage, prop, "Main directory is NOT writable. Change directory or fix permissions!")
        }

        if(!Systems().isConfigFileSetupCorrect()){
            val alert =
                Alert(
                    Alert.AlertType.ERROR,
                    ("Failed config systems check, Please reinstall the jar and set up your system again. This should not have happened."),
                    ButtonType.OK,
                    ButtonType.CANCEL
                )
            alert.title = "Failed config systems check"
            alert.showAndWait()
            exitProcess(0)
        }

        return true
    }
}