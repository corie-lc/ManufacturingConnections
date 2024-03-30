package org.corieleclair.travellers

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.FileInputStream
import java.util.*

class HelloApplication : Application() {


    override fun start(stage: Stage) {
        stage.title = "OpenManufacturing"

        val mainVBox = VBox()
        val scene = Scene(mainVBox, 600.0, 600.0)


        val buttonMoveJobs = Button("Move Jobs")
        val buttonCreateJob = Button("Create Jobs")
        val buttonCurrentJobs = Button("Current Jobs")
        val buttonUpdateDirectory = Button("Change Directory (ONLY IF YOU ARE SURE")
        val buttonUpdateConfig = Button("Update Configuration(Admin Use Only)")



        buttonMoveJobs.setOnAction {
            JobSelector().moveJobsWindow()
        }

        buttonCreateJob.setOnAction {
            CreateJob().createNewJob()
        }

        buttonUpdateConfig.setOnAction {
            UpdateConfig().openConfigWindow()
        }

        buttonUpdateDirectory.setOnAction {
            val configFilePath = "src/config.properties"
            val propsInput = FileInputStream(configFilePath)

            val prop = Properties()
            prop.load(propsInput)
            Systems().selectMainDirectory(stage, prop, "You are about to change the main directory.")
        }



        mainVBox.children.add(buttonMoveJobs)
        mainVBox.children.add(buttonCurrentJobs)
        mainVBox.children.add(buttonCreateJob)
        mainVBox.children.add(buttonUpdateConfig)

        mainVBox.children.add(buttonUpdateDirectory)




        Systems().runSystemChecks(mainVBox, stage)


        stage.scene = scene
        stage.show()
    }
}



fun main() {

    Application.launch(HelloApplication::class.java)
}