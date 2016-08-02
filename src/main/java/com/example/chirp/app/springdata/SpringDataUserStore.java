package com.example.chirp.app.springdata;

import com.example.chirp.app.kernel.Chirp;
import com.example.chirp.app.kernel.User;
import com.example.chirp.app.kernel.exceptions.NoSuchEntityException;
import com.example.chirp.app.stores.UserStore;
import com.example.chirp.app.stores.UserStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class SpringDataUserStore implements UserStore {

    private UserRepository userRepository;

    @Autowired
    public SpringDataUserStore(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setSeedDatabase(boolean seedDatabase) {
        if (seedDatabase) {
            UserStoreUtils.resetAndSeedRepository(this);
        }
    }

    @Override
    public void clear() {
        userRepository.deleteAll();
    }

    @Override
    public User createUser(String username, String fullName) {
        UserEntity userEntity = new UserEntity(username, fullName);
        userRepository.save(userEntity);
        return userEntity.toUser();
    }

    @Override
    public void updateUser(User user) {
        UserEntity userEntity = new UserEntity(user);
        userRepository.save(userEntity);
        String username = user.getUsername();
        List<UserEntity> userEntities = userRepository.findByUsername(username);
    }

    @Override
    public User getUser(String username) {
        List<UserEntity> userEntities = userRepository.findByUsername(username);
        if (userEntities.isEmpty()) {
            throw new NoSuchEntityException();
        }
        return userEntities.get(0).toUser();
    }

    @Override
    public Deque<User> getUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        Deque<User> users = new LinkedList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(userEntity.toUser());
        }
        return users;
    }

    @Override
    public Chirp getChirp(String chirpId) {
        for (User user : getUsers()) {
            for (Chirp chirp : user.getChirps()) {
                if (chirpId.equals(chirp.getId().getId())) {
                    return chirp;
                }
            }
        }
        throw new NoSuchEntityException();
    }
}