package com.schess.components;

import com.schess.models.Users;
import com.schess.service.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {

    private final UserService userService;

    private Users users;

    private TextField name = new TextField("Name");
    private TextField email = new TextField("email");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Users> binder = new Binder<>(Users.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public UserEditor(UserService userService) {
        this.userService = userService;

        add(name, email, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editUser(users));
        setVisible(false);
    }

    private void delete() {
        userService.delete(users);
        changeHandler.onChange();
    }

    private void save() {
        userService.save(users);
        changeHandler.onChange();
    }

    public void editUser(Users newUser) {
        if (newUser == null) {
            setVisible(false);
            return;
        }
        if (newUser.getId() != 0) {
            users = userService.findById(newUser.getId()).orElse(newUser);
        } else {
            users = newUser;
        }
        binder.setBean(users);

        setVisible(true);

        email.focus();
    }
}
