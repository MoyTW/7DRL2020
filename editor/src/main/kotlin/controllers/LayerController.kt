package controllers

import javafx.collections.ObservableList
import models.Layer
import tornadofx.*

class LayerController: Controller() {
    private val layers = mutableListOf<Layer>().observable()

    fun getObservableLayers(): ObservableList<Layer> {
        return layers
    }

    fun addLayer(layer: Layer) {
        this.layers.add(layer)
    }

    fun removeLayer(layer: Layer) {
        if (layer in layers) {
            layers.remove(layer)
        }
    }
}
