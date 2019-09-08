package models

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File

class Layer(name: String?, level: Int=-1, visible: Boolean=true, file: File? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val levelProperty = SimpleIntegerProperty(this, "level", level)
    var level by levelProperty

    val visibleProperty = SimpleBooleanProperty(this, "visible", visible)
    var visible by visibleProperty

    // Note that for SimpleObjectProperty you declare the T as non-nullable, but since Java it in fact is.
    // This...DOES open you up to full-on NPEs. But tight Java integration already seems to do this...
    val fileProperty = SimpleObjectProperty<File>(this, "file", file)
    var file by fileProperty
}

class LayerModel: ItemViewModel<Layer>() {
    val name = bind(Layer::nameProperty)
    val level = bind(Layer::levelProperty)
    val visible = bind(Layer::visibleProperty)
    val file = bind(Layer::fileProperty)

    fun fileName(): String {
        return file.value?.name ?: "NO FILE CHOSEN"
    }
}
