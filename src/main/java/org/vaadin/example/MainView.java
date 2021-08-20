package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.model.Person;
import org.vaadin.example.model.PersonRepository;
import org.vaadin.example.views.PersonForm;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    @Autowired
    private final PersonRepository personRepository;

    private TextField textFieldFilter = new TextField();
    private Button buttonNew = new Button("Novo Registro");
    private Grid<Person> grid = new Grid<>(Person.class);

    private PersonForm form;

    public MainView(@Autowired PersonRepository personRepository) {
        this.personRepository = personRepository;

        form = new PersonForm(this, personRepository);
        form.setVisible(false);

        textFieldFilter.setClearButtonVisible(true);
        textFieldFilter.setValueChangeMode(ValueChangeMode.EAGER);
        textFieldFilter.addValueChangeListener(event -> updateGrid());

        buttonNew.addClickListener(e -> addNew());

        grid.setColumns(
            "fullName",
            "birthDate",
            "cpf",
            "email",
            "profession"
        );
        changeGridColumnsName();        
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(e -> select());

        updateGrid();

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        add(new HorizontalLayout(textFieldFilter, buttonNew), mainContent);
        setSizeFull();
    }

    public void addNew() {
        grid.asSingleSelect().clear();
        form.setPerson(new Person());
    }

    public void updateGrid() {
        if (textFieldFilter.getValue().isEmpty()) {
            grid.setItems(personRepository.findAll());
        } else {
            grid.setItems(
                personRepository.findByFullName(textFieldFilter.getValue())
            );
        }
    }

    private void select() {
        form.setPerson(grid.asSingleSelect().getValue());
    }

    private void changeGridColumnsName() {
        grid.getColumnByKey("fullName").setHeader("Nome Completo");
        grid.getColumnByKey("birthDate").setHeader("Data de Nascimento");
        grid.getColumnByKey("cpf").setHeader("CPF");
        grid.getColumnByKey("email").setHeader("E-mail");
        grid.getColumnByKey("profession").setHeader("Profissão");
    }

}
