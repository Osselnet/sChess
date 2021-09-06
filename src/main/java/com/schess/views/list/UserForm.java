package com.schess.views.list;

import com.schess.models.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class UserForm extends FormLayout {
    private User users;

    private TextField name = new TextField("Name");
    private EmailField email = new EmailField("email");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    //Binder<Users> binder = new Binder<>(Users.class);

    public UserForm() {
        addClassName("user-form");
        //binder.bindInstanceFields(this);
        add(name, email, actions());
    }

    public void setUser(User users) {
        this.users = users;
        //binder.readBean(users);
    }

    private Component actions() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, users)));
        cancel.addClickListener(click -> fireEvent(new CloseEvent(this)));

        //binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, cancel, delete);
    }

    private void validateAndSave() {
//        try {
            //binder.writeBean(users);
            fireEvent(new SaveEvent(this, users));
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
    }

    // Events
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User users;

        protected UserFormEvent(UserForm source, User users) {
            super(source, false);
            this.users = users;
        }

        public User getUser() {
            return users;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User users) {
            super(source, users);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User users) {
            super(source, users);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
