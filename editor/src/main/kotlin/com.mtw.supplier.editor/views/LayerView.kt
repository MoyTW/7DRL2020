package com.mtw.supplier.editor.views

import controllers.LayerController
import javafx.scene.control.TableCell
import javafx.scene.control.TableView
import javafx.stage.FileChooser
import models.Layer
import models.LayerModel
import tornadofx.*
import java.io.File
import java.nio.file.Paths

class LayerFileCell: TableCell<Layer, File>() {
    private val noFileText = "NO_FILE_SELECTED"
    private val appPath = Paths.get("").toAbsolutePath().toString()
    private val pngFilters = arrayOf(FileChooser.ExtensionFilter("PNG", "*png"))

    init {
        isEditable = true
    }

    // I'm not super confident that this is how startEdit is supposed to be used; I'm preeety sure literally calling
    // the commit/cancel inside this function is not intended. Unfortunately I can't understand ChoiceBoxTableCell
    // so I'm just gonna go off this because it looks like it works at the moment.
    override fun startEdit() {
        super.startEdit()

        val chosenFile = chooseFile("Choose the image file",
            pngFilters,
            FileChooserMode.Single,
            op = { initialDirectory = File(appPath) }
        ).firstOrNull()

        if (chosenFile != null) {
            this.commitEdit(chosenFile)
        } else {
            this.cancelEdit()
        }
    }

    override fun updateItem(item: File?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            this.text = null
        } else if (item != null) {
            this.text = item.name
        } else {
            this.text = noFileText
        }
    }
}

class LayerTableView: View("Layer Table View") {
    private val layerController: LayerController by inject()
    private val layerModel: LayerModel by inject()

    private var tableView: TableView<Layer> by singleAssign()

    override val root = borderpane { }

    init {
        with(root) {
            top {
                hbox {
                    button("New Row").action {
                        layerController.addLayer(Layer("New Layer"))
                    }
                    button("Remove Row").action {
                        // I can't figure out how to get it to only be shown if there's a current selection
                        if (tableView.selectedItem != null) {
                            layerController.removeLayer(tableView.selectedItem!!)
                        }
                    }
                }
            }
            center {
                tableView = tableview(layerController.getObservableLayers()) {
                    column("Name", Layer::nameProperty).makeEditable()
                    column("Level", Layer::levelProperty).makeEditable()
                    column("Visible", Layer::visibleProperty).makeEditable()
                    column("File", Layer::fileProperty) {
                        setCellFactory {
                            LayerFileCell()
                        }
                    }

                    bindSelected(layerModel)
                }
            }
        }
    }
}

class LayerView: View("Layer View") {
    override val root = borderpane()

    init {
        with(root) {
            center {
                this += LayerTableView()
            }
        }
    }
}