package com.schess.views.list;

import com.schess.models.User;
import com.schess.service.UserService;
import com.schess.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Route(value = "", layout = MainLayout.class)
@PageTitle("Users | SChess")
public class UserView extends VerticalLayout {
    private final UserService userService;

    private Grid<User> grid = new Grid<>(User.class);

    private final TextField filter = new TextField("", "Type to filter");
    private UserForm form;

    public UserView(UserService userService) {
        this.userService = userService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new UserForm();
        form.addListener(UserForm.SaveEvent.class, this::saveUser);
        form.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent evt) {
        userService.delete(evt.getUser());
        updateList();
        closeEditor();
    }

    private void saveUser(UserForm.SaveEvent evt) {
        if (userService.save(evt.getUser())) {
            updateList();
            closeEditor();
        }
    }

    private HorizontalLayout getToolBar() {
        filter.setPlaceholder("Filter by name...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());

        Button addUserButton = new Button("Add new", click -> addUser());

        HorizontalLayout toolbar = new HorizontalLayout(filter, addUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("name", "email");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));
    }

    public void editUser(User newUser) {
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
