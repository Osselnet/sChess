package com.schess.view.list;

import com.schess.models.Users;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class UserForm extends FormLayout {
    private Users users;

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

    public void setUser(Users users) {
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
        private Users users;

        protected UserFormEvent(UserForm source, Users users) {
            super(source, false);
            this.users = users;
        }

        public Users getUser() {
            return users;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, Users users) {
            super(source, users);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, Users users) {
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
