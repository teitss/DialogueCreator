package io.github.teitss.dialoguecreator

import com.google.inject.Inject
import io.github.teitss.dialoguecreator.config.DialogueConfig
import me.rojo8399.placeholderapi.PlaceholderService
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.GameReloadEvent
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GameStartingServerEvent
import org.spongepowered.api.event.service.ChangeServiceProviderEvent
import org.spongepowered.api.plugin.Dependency
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import java.nio.file.Path

@Plugin(
        id="dialoguecreator",
        name="DialogueCreator",
        version = "1.1.0",
        authors=arrayOf("Teits / Discord Teits#7663"),
        description = "A plugin to create Pixelmon Reforged dialogues.",
        dependencies = arrayOf(Dependency(id = "pixelmon"), Dependency(id = "spotlin"), Dependency(id = "placeholderapi"))
)
class DialogueCreator @Inject constructor(
        val logger: Logger,
        val pluginContainer: PluginContainer,
        @ConfigDir(sharedRoot = false) val configDir: Path) {

    companion object {
        lateinit var INSTANCE: DialogueCreator
    }

    lateinit var configManager: ConfigurationLoader<CommentedConfigurationNode>
    lateinit var placeholderService: PlaceholderService

    @Listener
    fun onInit(event: GameInitializationEvent) {
        INSTANCE = this
        logger.info("Checking configuration file...")
        configManager = HoconConfigurationLoader.builder().setPath(configDir.resolve("${this.pluginContainer.id}.conf")).build()
        DialogueConfig.setup(configDir, configManager)
    }

    @Listener
    fun onChangeServiceProvider(event: ChangeServiceProviderEvent) {
        //Searching for Placeholder service
        if (event.service == PlaceholderService::class.java) {
            val placeholderServiceProvider = event.newProviderRegistration
            logger.info("Plugin de Placeholders encontrado! ${placeholderServiceProvider.plugin.name} " +
                    "v${placeholderServiceProvider.plugin.version.orElse("?")} será usado nos sistemas do núcleo.")
            placeholderService = placeholderServiceProvider.provider as PlaceholderService
        }

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