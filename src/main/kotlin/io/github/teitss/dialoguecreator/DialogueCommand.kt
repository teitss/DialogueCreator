package io.github.teitss.dialoguecreator

import com.pixelmonmod.pixelmon.api.dialogue.Dialogue
import io.github.teitss.dialoguecreator.config.DialogueConfig
import net.minecraft.entity.player.EntityPlayerMP
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

class DialogueCommand {

    val commandSpec = CommandSpec.builder()
            .description(Text.of("Command to open dialogues."))
            .permission("dialoguecreator.command.dialogue")
            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("dialogue-id"))))
            .executor { src, args ->
                    if(src is Player) {
                        val dialogueId = args.getOne<String>("dialogue-id").get()
                        if (!src.hasPermission("dialoguecreator.dialogue.$dialogueId")) {
                            src.localizedMessage("command.dialogue.nopermission")
                            return@executor CommandResult.success()
                        }
                        val dialogue = DialogueConfig.dialoguesMap.get(dialogueId)
                        if(dialogue == null) {
                            src.localizedMessage("command.dialogue.inexistentdialogue")
                            return@executor CommandResult.success()
                        }
                        Dialogue.setPlayerDialogueData(
                                src as EntityPlayerMP,
                                mutableListOf(dialogue.buildDialogue(DialogueCreator.INSTANCE.placeholderService, src)),
                                true)
                    }

                return@executor CommandResult.success()
            }
            .build()

}