package org.corieleclair.travellers

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.FileInputStream
import java.io.FileWriter
import java.util.*


class UpdateConfig {

    private fun getJobLocations(): List<String> {
        val listOfLocations = Systems().getProperty("job_locations").split(",")
        print(listOfLocations)

        return listOfLocations


    }

    private fun updateJobProperties(propLayout: VBox){

        var newListOfProperites = ""

        for (item in propLayout.children){
            val tempTextField = item as TextField

            if(tempTextField.text != ""){
                newListOfProperites = newListOfProperites + tempTextField.text + ","
            }
        }

        val configFilePath = "src/config.properties"
        val propsInput = FileInputStream(configFilePath)


        val prop: Properties = Properties()
        prop.load(propsInput)
        prop.setProperty("job_properties", newListOfProperites)
        prop.store(FileWriter("src/config.properties"), "store to properties file");
    }

    private fun updateLocationsProperties(propLayout: VBox){

        var newListOfLocations = ""

        for (item in propLayout.children){
            val tempTextField = item as TextField

            if(tempTextField.text != ""){
                newListOfLocations = newListOfLocations + tempTextField.text + ","
                print(newListOfLocations)
            }
        }

        val configFilePath = "src/config.properties"
        val propsInput = FileInputStream(configFilePath)

        val prop = Properties()
        prop.load(propsInput)
        prop.setProperty("job_locations", newListOfLocations)
        prop.store(FileWriter("src/config.properties"), "store to properties file");
    }

    fun openConfigWindow (){
        // update to add username and password protection

        val stage = Stage()
        val mainLayout = GridPane()
        val jobPropertyLayout = VBox()
        val jobLocationLayout = VBox()
        val buttonAddJobProperty = Button("Add Job Property")
        val buttonAddJobLocation = Button("Add Job Location")
        val buttonPushUpdate = Button("Apply")
        val scrollPane = ScrollPane(mainLayout)
        val root = BorderPane(scrollPane)

        mainLayout.add(jobPropertyLayout, 0, 1)
        mainLayout.add(jobLocationLayout, 1, 1)
        mainLayout.add(buttonPushUpdate, 0, 3)
        mainLayout.add(buttonAddJobProperty, 1, 0)
        mainLayout.add(buttonAddJobLocation, 2, 0)



        // dynamic stuff, messy

        buttonAddJobProperty.setOnAction {
            jobPropertyLayout.children.add(TextField("New Property"))
        }

        buttonAddJobLocation.setOnAction {
            jobLocationLayout.children.add(TextField("New Location"))
        }

        buttonPushUpdate.setOnAction {
            updateJobProperties(jobPropertyLayout)
            updateLocationsProperties(jobLocationLayout)
        }

        for (property in getJobLocations()){
            jobLocationLayout.children.add(TextField(property))
        }

        for (property in Systems().getProperty("job_properties").split(",")){
            if(property == "job number" || property == "job location"){
                val tempTextField = TextField(property)
                tempTextField.isDisable = true
                jobPropertyLayout.children.add(tempTextField)
            } else{
                jobPropertyLayout.children.add(TextField(property))
            }
        }


        scrollPane.isFitToHeight = true
        stage.height = 600.0
        stage.width = 600.0
        stage.isResizable = false
        stage.scene = Scene(root)
        stage.show()
    }
}