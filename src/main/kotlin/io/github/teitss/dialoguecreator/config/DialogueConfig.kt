package io.github.teitss.dialoguecreator.config

import io.github.teitss.dialoguecreator.DialogueCreator
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.spongepowered.api.Sponge
import java.nio.file.Files
import java.nio.file.Path

object DialogueConfig {

    val dialoguesMap = hashMapOf<String, DialogueSpec>()
    val messagesMap = hashMapOf<String, String>()

    fun setup(path: Path, configManager: ConfigurationLoader<CommentedConfigurationNode>) {
        if (Files.notExists(path.resolve("dialoguecreator.conf")))
            install(path)
        load(configManager)
    }

    fun load(configManager: ConfigurationLoader<CommentedConfigurationNode>) {
        val configNode = configManager.load()
        dialoguesMap.clear()
        for (node in configNode.getNode("dialogues").childrenList) {
            val dialogue = DialogueSpecDeserializer.deserialize(node)
            dialoguesMap.put(dialogue.first, dialogue.second)
        }
        messagesMap.clear()
        for (node in configNode.getNode("messages").childrenMap) {
            messagesMap.put(node.key.toString(), node.value.string!!)
        }
        DialogueCreator.INSTANCE.logger.info("Configuration successfully loaded.")
    }

    fun install(path: Path) {
        val configFile = Sponge.getAssetManager().getAsset(DialogueCreator.INSTANCE, "dialoguecreator.conf").get()
        configFile.copyToDirectory(path)
        DialogueCreator.INSTANCE.logger.info("Configuration successfully installed.")
    }

}