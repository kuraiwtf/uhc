package dev.kurai.uhc.profile;

import dev.kurai.uhc.UltraHardcoreAPI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class ProfileServiceImpl implements ProfileService {

    private final Map<UUID, Profile> profiles;

    private final Collection<Profile> profilesView;

    private final UltraHardcoreAPI ultraHardcore;

    public ProfileServiceImpl(final UltraHardcoreAPI ultraHardcore) {
        this.profiles = new ConcurrentHashMap<>();
        this.profilesView = Collections.unmodifiableCollection(this.profiles.values());

        this.ultraHardcore = ultraHardcore;
    }

    @Override
    public Profile getOrCreateProfile(final UUID uniqueId) {
        return this.profiles.computeIfAbsent(uniqueId, uuid -> new ProfileImpl(uuid, this.ultraHardcore));
    }

    @Override
    public Optional<Profile> findProfile(final UUID uniqueId) {
        return Optional.ofNullable(this.profiles.get(uniqueId));
    }

    @Override
    public Optional<Profile> getProfile(final String name) {
        return this.getProfile(profile -> profile.getName().equals(name));
    }

    @Override
    public Optional<Profile> getProfile(final Predicate<Profile> filter) {
        return this.profilesView.stream().filter(filter).findFirst();
    }

    @Contract(pure = true)
    @Override
    public Collection<Profile> getProfiles() {
        return this.profilesView;
    }

    @Override
    public @Unmodifiable Collection<Profile> getProfiles(final Predicate<Profile> filter) {
        return this.profilesView.stream().filter(filter).toList();
    }

    @Override
    public @Unmodifiable Collection<Profile> getPlayingProfiles() {
        return this.getProfiles(Profile::alive);
    }
}
