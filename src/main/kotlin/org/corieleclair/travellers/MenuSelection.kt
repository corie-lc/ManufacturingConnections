package org.corieleclair.travellers

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.io.FileInputStream
import java.util.*

class HelloApplication : Application() {


    override fun start(stage: Stage) {
        stage.title = "OpenManufacturing"

        val mainLayout = GridPane()
        val scene = Scene(mainLayout, 600.0, 600.0)


        val buttonMoveJobs = Button("Move Jobs")
        val buttonCreateJob = Button("Create Jobs")
        val buttonCurrentJobs = Button("Current Jobs")
        val buttonUpdateDirectory = Button("Change Directory (ONLY IF YOU ARE SURE")
        val buttonUpdateConfig = Button("Update Configuration(Admin Use Only)")

        buttonMoveJobs.setPrefSize(300.0,100.0)
        buttonCreateJob.setPrefSize(300.0,100.0)
        buttonCurrentJobs.setPrefSize(300.0,100.0)
        buttonUpdateDirectory.setPrefSize(300.0,100.0)
        buttonUpdateConfig.setPrefSize(300.0,100.0)




        buttonMoveJobs.setOnAction {
            JobSelector().moveJobsWindow()
        }

        buttonCreateJob.setOnAction {
            CreateJob().createNewJob()
        }

        buttonUpdateConfig.setOnAction {
            UpdateConfig().openConfigWindow()
        }

        buttonCurrentJobs.setOnAction {
            AllCurrentJobs().allCurrentJobsWindow()
        }

        buttonUpdateDirectory.setOnAction {
            val configFilePath = "src/config.properties"
            val propsInput = FileInputStream(configFilePath)

            val prop = Properties()
            prop.load(propsInput)
            Systems().selectMainDirectory(stage, prop, "You are about to change the main directory.")
        }



        mainLayout.add(buttonMoveJobs, 0, 0)
        mainLayout.add(buttonCurrentJobs, 0, 1)
        mainLayout.add(buttonCreateJob, 1, 0)
        mainLayout.add(buttonUpdateConfig, 1, 1)

        mainLayout.add(buttonUpdateDirectory, 0, 2)




        Systems().runSystemChecks(mainLayout, stage)


        stage.isResizable = false
        stage.scene = scene
        stage.show()
    }
}



fun main() {

    Application.launch(HelloApplication::class.java)
}