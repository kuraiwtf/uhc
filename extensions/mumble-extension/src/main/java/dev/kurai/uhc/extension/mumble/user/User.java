package dev.kurai.uhc.extension.mumble.user;

import dev.kurai.uhc.extension.mumble.channel.Channel;

public sealed interface User permits UserImpl {

  String name();

  Channel channel();
}
