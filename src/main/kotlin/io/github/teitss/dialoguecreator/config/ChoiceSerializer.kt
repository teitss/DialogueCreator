package io.github.teitss.dialoguecreator.config

import com.google.common.reflect.TypeToken
import com.pixelmonmod.pixelmon.api.dialogue.Choice
import ninja.leaping.configurate.ConfigurationNode
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player

object ChoiceSerializer {

    fun deserialize(value: ConfigurationNode): ArrayList<Choice> {
        return arrayListOf<Choice>().apply {
            for (choice in value.childrenList) {
                this.add(Choice.builder()
                        .setText(choice.getNode("name").string)
                        .setHandle { dialogue ->
                            val commandList = choice.getNode("command").getList(TypeToken.of(String::class.java))
                            val action = choice.getNode("action").string
                            if(!commandList.isEmpty())
                                commandList.forEach {
                                    if(it.contains("player||")) {
                                        Sponge.getCommandManager().process((dialogue.player as Player),
                                                it.split("player||")[1]
                                                        .replace("%player%", dialogue.player.name))
                                    }
                                    else {
                                        Sponge.getCommandManager().process(Sponge.getServer().console,
                                                it.replace("%player%", dialogue.player.name)
                                        )
                                    }
                                }
                            if(!action!!.isEmpty())
                                dialogue.reply(DialogueConfig.dialoguesMap.get(action))

                        }
                        .build())
            }
        }
    }

}