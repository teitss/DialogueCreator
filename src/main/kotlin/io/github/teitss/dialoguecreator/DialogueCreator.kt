package io.github.teitss.dialoguecreator

import com.google.inject.Inject
import io.github.teitss.dialoguecreator.config.DialogueConfig
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.GameReloadEvent
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GameStartingServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import java.nio.file.Path

@Plugin(
        id="dialoguecreator",
        name="DialogueCreator",
        version="1.0.0",
        authors=arrayOf("Teits / Discord Teits#7663"),
        description = "A plugin to create Pixelmon Reforged dialogues."
)
class DialogueCreator @Inject constructor(
        val logger: Logger,
        val pluginContainer: PluginContainer,
        @ConfigDir(sharedRoot = false) val configDir: Path) {

    companion object {
        lateinit var instance: DialogueCreator
    }

    lateinit var configManager: ConfigurationLoader<CommentedConfigurationNode>

    @Listener
    fun onInit(event: GameInitializationEvent) {
        instance = this
        logger.info("Checking configuration file...")
        configManager = HoconConfigurationLoader.builder().setPath(configDir.resolve("${this.pluginContainer.id}.conf")).build()
        DialogueConfig.setup(configDir, configManager)
    }



    @Listener
    fun onServerStart(event: GameStartingServerEvent) {
        logger.info("Registering commands...")
        Sponge.getCommandManager().register(this, DialogueCommand().commandSpec, "dialogue", "dialogues")
    }

    @Listener
    fun onGameReload(e: GameReloadEvent) {
        logger.info("Reloading plugin configuration...")
        DialogueConfig.load(configManager)
    }

}