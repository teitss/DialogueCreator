package io.github.teitss.dialoguecreator

import io.github.teitss.dialoguecreator.config.DialogueConfig
import net.minecraft.nbt.NBTTagCompound
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.channel.MessageReceiver
import org.spongepowered.api.text.chat.ChatType
import org.spongepowered.api.text.chat.ChatTypes
import org.spongepowered.api.text.serializer.TextSerializers

fun MessageReceiver.localizedMessage(msg: String, chatType: ChatType = ChatTypes.CHAT) {
    if (this is Player)
        this.sendMessage(chatType, TextSerializers.formattingCode('&')
                .deserialize(DialogueConfig.messagesMap.get(msg)!!))
    else
        this.sendMessage(TextSerializers.formattingCode('&')
                .deserialize(DialogueConfig.messagesMap.get(msg)!!))
}