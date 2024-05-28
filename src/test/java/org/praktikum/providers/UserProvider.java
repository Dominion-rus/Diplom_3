package org.praktikum.providers;

import org.praktikum.models.User;
import org.praktikum.utils.TestDataGenerator;

public class UserProvider {
    public static User getRandom() {
        return TestDataGenerator.getRandomUser();
    }
}
