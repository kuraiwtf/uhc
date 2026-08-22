package dev.kurai.uhc.extension.mumble.channel;

public sealed interface Channel permits ChannelImpl {

  String name();
}
