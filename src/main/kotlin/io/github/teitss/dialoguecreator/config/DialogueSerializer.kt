package br.com.pixelmonbrasil.anothercore.modules.dialoguecreator.config

import com.pixelmonmod.pixelmon.api.dialogue.Dialogue
import io.github.teitss.dialoguecreator.config.ChoiceSerializer
import ninja.leaping.configurate.ConfigurationNode

object DialogueSerializer  {

     fun deserialize(value: ConfigurationNode): Pair<String, Dialogue> {
        return value.getNode("id").string!! to Dialogue.builder()
                .setName(value.getNode("name").string)
                .setText(value.getNode("description").string)
                .setChoices(ChoiceSerializer.deserialize(value.getNode("choices")))
                .build()
    }

}