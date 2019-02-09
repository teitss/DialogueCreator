package io.github.teitss.dialoguecreator.config

import com.google.common.reflect.TypeToken
import com.pixelmonmod.pixelmon.api.dialogue.Choice
import io.github.teitss.dialoguecreator.DialogueCreator
import ninja.leaping.configurate.ConfigurationNode
import org.apache.commons.lang3.StringUtils
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player

object ChoiceDeserializer {

    fun deserialize(value: ConfigurationNode): ArrayList<Choice> {
        return arrayListOf<Choice>().apply {
            for (choice in value.childrenList) {
                this.add(Choice.builder()
                        .setText(choice.getNode("name").string)
                        .setHandle {
                            val commandList = choice.getNode("command").getList(TypeToken.of(String::class.java))
                            commandList.forEach { command ->
                                if (command.contains("player||")) {
                                    Sponge.getCommandManager().process((it.player as Player),
                                            StringUtils.replace(command.split("player||")[1], "%player%",
                                                    it.player.name))
                                } else {
                                    Sponge.getCommandManager().process(Sponge.getServer().console,
                                            StringUtils.replace(command, "%player%", it.player.name)
                                    )
                                }
                            }
                            if (!choice.getNode("action").string!!.isEmpty())
                                it.reply(DialogueConfig.dialoguesMap.get(choice.getNode("action").string!!)!!
                                        .buildDialogue(DialogueCreator.INSTANCE.placeholderService, it.player as Player))

                        }
                        .build())
            }
        }
    }

}