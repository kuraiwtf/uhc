package dev.kurai.uhc.nickname;

import java.util.UUID;

public interface NicknameService {

  Nickname nickname(final UUID uniqueId);

  NicknameHolder holder(final UUID uniqueId);
}
