package org.vaadin.example.views;

import java.util.Locale;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import org.vaadin.example.MainView;
import org.vaadin.example.enums.CrudMode;
import org.vaadin.example.model.Person;
import org.vaadin.example.model.PersonRepository;

public class PersonForm extends FormLayout{
    
    private TextField fullName = new TextField("Nome Completo");
    private DatePicker birthDate = new DatePicker("Data de nascimento");
    private TextField cpf = new TextField("CPF");
    private TextField email = new TextField("E-mail");
    private TextField profession = new TextField("Profissão");

    private Button buttonSave = new Button("Salvar");
    private Button buttonEdit = new Button("Editar");
    private Button buttonFechar = new Button("Fechar");
    private Button buttonDelete = new Button("Excluir");

    private ConfirmationDialog confirmationDialog = new ConfirmationDialog();

    private MainView mainView;
    
    private BeanValidationBinder<Person> binder = new BeanValidationBinder<>(Person.class);
    private PersonRepository personRepository;

    private CrudMode crudMode;
    
    public PersonForm(MainView mainView, PersonRepository personRepository) {
        this.mainView = mainView;
        this.personRepository = personRepository;

        binder.setBean(new Person());

        binder.bindInstanceFields(this);
        
        birthDate.setLocale(new Locale("pt", "BR"));
        birthDate.setRequiredIndicatorVisible(true);

        cpf.setRequiredIndicatorVisible(true);
        cpf.setPlaceholder("xxx.xxx.xxx-xx");

        email.setRequiredIndicatorVisible(true);
        
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSave.addClickListener(event -> save());

        buttonFechar.getStyle().set("position", "absolute");
        buttonFechar.getStyle().set("right", "16px");
        buttonFechar.addClickListener(even -> setPerson(null));

        buttonEdit.addClickListener(event -> edit());

        buttonDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonDelete.addClickListener(event -> delete());

        getStyle().set("max-width", "400px");

        HorizontalLayout buttons = new HorizontalLayout(buttonSave, buttonEdit, buttonDelete, buttonFechar);
        add(fullName, birthDate, cpf, email, profession, buttons);
    }

    private void save() {
        Person person = binder.getBean();

        binder.validate();

        switch(crudMode) {

            case CREATE:
                if (!isDuplicated(person)) {            
                    personRepository.save(person);
        
                    mainView.updateGrid();
                    binder.setBean(new Person());
        
                    restartBinder();            
                }
            break;

            case UPDATE:
                Person personToUpdate = personRepository.findById(person.getId()).get();
                
                personToUpdate.setFullName(fullName.getValue());
                personToUpdate.setBirthDate(birthDate.getValue());        
                personToUpdate.setCpf(cpf.getValue());
                personToUpdate.setEmail(email.getValue());
                personToUpdate.setProfession(profession.getValue());
                
                personRepository.saveAndFlush(personToUpdate);
                
                mainView.updateGrid();
                
                binder.setBean(new Person());        
                
                restartBinder();
            break;

            default: return;
        }     

                
    }

    private void edit() {
        crudMode = CrudMode.UPDATE;

        buttonSave.setEnabled(true);
        buttonDelete.setEnabled(true);
        buttonEdit.setEnabled(false);

        fullName.setEnabled(true);
        birthDate.setEnabled(true);
        cpf.setEnabled(true);
        email.setEnabled(true);
        profession.setEnabled(true);
    }

    private void delete() {
        Person person = binder.getBean();

        confirmationDialog.setTitle("Excluir Registro");
        
        confirmationDialog.setQuestion(
            "Excluir " +
            person.getFullName() +
            ", CPF: " +
            person.getCpf() + "?"
        );
        
        confirmationDialog.open();
        confirmationDialog.addConfirmationListener(event -> {
            personRepository.delete(person);
            mainView.updateGrid();
            setPerson(null);
        });
    }

    private boolean isDuplicated(Person person) {
        boolean isDuplicated = false;        
        
        if (!personRepository.findByCpf(cpf.getValue()).isEmpty()) {
            isDuplicated = true;

            binder.forField(cpf)
                .withValidator(
                    cpf -> personRepository.findByCpf(cpf)
                        .isEmpty(),
                        "Já existe um registro com este CPF!")
                .bind("cpf");
            
            binder.validate();
        }

        if (!personRepository.findByEmail(email.getValue()).isEmpty()) {
            isDuplicated = true;

            binder.forField(email)
                .withValidator(
                    email -> personRepository.findByEmail(email)
                        .isEmpty(),
                        "Este e-mail já está cadastrado!")
                .bind("email");
            
            binder.validate();
        }

        return isDuplicated;
    } 

    private void restartBinder() {
        binder.removeBinding(cpf);
        binder.removeBinding(email);
        binder.bindInstanceFields(this);
    }

    public void setPerson(Person person) {
        binder.setBean(person);

        if (person == null) {
            
            setVisible(false);
        
        } else {
            
            setVisible(true);
            
            if (binder.getBean().isPersisted()) {
                buttonSave.setEnabled(false);
                buttonDelete.setEnabled(true);
                buttonEdit.setEnabled(true);

                fullName.setEnabled(false);
                birthDate.setEnabled(false);
                cpf.setEnabled(false);
                email.setEnabled(false);
                profession.setEnabled(false);
            } else {
                crudMode = CrudMode.CREATE;
                
                buttonSave.setEnabled(true);
                buttonDelete.setEnabled(false);
                buttonEdit.setEnabled(false);

                fullName.setEnabled(true);
                birthDate.setEnabled(true);
                cpf.setEnabled(true);
                email.setEnabled(true);
                profession.setEnabled(true);
            }

            fullName.focus();
            
        }
    }
}