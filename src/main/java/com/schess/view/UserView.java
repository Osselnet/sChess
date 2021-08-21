package com.schess.view;

import com.schess.components.UserEditor;
import com.schess.models.User;
import com.schess.repositories.UserRepository;
import com.schess.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class UserView extends VerticalLayout {
    private final UserService userService;

    private Grid<User> grid = new Grid<>(User.class);

    private final TextField filter = new TextField("", "Type to filter");
    private final Button addNewButton = new Button("Add new");
    private UserEditor editor;

    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewButton);

    @Autowired
    public UserView(UserService userService, UserEditor editor) {
        this.userService = userService;
        this.editor = editor;
        add(toolbar, grid, this.editor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showUsers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editUser((User) e.getValue()));

        addNewButton.addClickListener(e -> editor.editUser(new User()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showUsers(filter.getValue());
        });

        showUsers("Oleg");
    }

    private void showUsers(String name) {
        if (name.isEmpty()) {
            grid.setItems(userService.findAll());
        } else {
            grid.setItems(userService.findByName(name));
        }
    }
}
