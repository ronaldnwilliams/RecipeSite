package com.teamtreehouse.recipesite.service;

import com.teamtreehouse.recipesite.dao.UserDao;
import com.teamtreehouse.recipesite.model.Recipe;
import com.teamtreehouse.recipesite.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    public void findByUsernameTest() throws Exception {
        User user = new User();
        user.setUsername("username");

        when(userDao.findByUsername("username")).thenReturn(user);

        assertEquals("findByUsername returns matching username",
                user.getUsername(),
                userService.findByUsername("username").getUsername());
        verify(userDao).findByUsername("username");
    }

    @Test
    public void toggleFavorite_ShouldAddFavorite() throws Exception {
        User user = new User();
        Recipe recipe = new Recipe();

        userService.toggleFavorite(user, recipe);

        assertEquals(user.getFavorites().size(), 1);
    }
}