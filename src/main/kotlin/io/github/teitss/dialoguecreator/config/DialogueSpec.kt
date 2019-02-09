package io.github.teitss.dialoguecreator.config

import com.pixelmonmod.pixelmon.api.dialogue.Choice
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue
import me.rojo8399.placeholderapi.PlaceholderService
import org.spongepowered.api.entity.living.player.Player

class DialogueSpec(val name: String, val description: String, val choices: ArrayList<Choice>) {

    fun buildDialogue(service: PlaceholderService, player: Player): Dialogue {
        return Dialogue.builder()
                .setName(service.replacePlaceholders(name, player, player).toPlain())
                .setText(service.replacePlaceholders(description, player, player).toPlain())
                .setChoices(choices)
                .build()
    }

}