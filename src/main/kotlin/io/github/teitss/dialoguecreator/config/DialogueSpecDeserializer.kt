package io.github.teitss.dialoguecreator.config

import ninja.leaping.configurate.ConfigurationNode

object DialogueSpecDeserializer {

    fun deserialize(value: ConfigurationNode): Pair<String, DialogueSpec> {
        return value.getNode("id").string!! to
                DialogueSpec(value.getNode("name").string!!,
                        value.getNode("description").string!!,
                        ChoiceDeserializer.deserialize(value.getNode("choices")))
    }

}