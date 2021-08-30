package com.schess.view.list;

import com.schess.models.Users;
import com.schess.service.UserService;
import com.schess.view.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Route(value = "", layout = MainLayout.class)
@PageTitle("Users | SChess")
public class UserView extends VerticalLayout {
    private final UserService userService;

    private Grid<Users> grid = new Grid<>(Users.class);

    private final TextField filter = new TextField("", "Type to filter");
    //private final Button addNewButton = new Button("Add new");
    private UserForm form;

    @Autowired
    public UserView(UserService userService) {
        this.userService = userService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new UserForm();
        form.addListener(UserForm.SaveEvent.class, this::saveContact);
        form.addListener(UserForm.DeleteEvent.class, this::deleteContact);
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(filter, content);
        updateList();
        closeEditor();
    }

    private void deleteContact(UserForm.DeleteEvent evt) {
        userService.delete(evt.getUser());
        updateList();
        closeEditor();
    }

    private void saveContact(UserForm.SaveEvent evt) {
        userService.save(evt.getUser());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolBar() {
        filter.setPlaceholder("Filter by name...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact", click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filter, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editUser(new Users());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "email");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));
    }

    public void editUser(Users newUser) {
        if (newUser == null) {
            closeEditor();
        } else {
            form.setUser(newUser);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(userService.findAll(filter.getValue()));
    }
}
