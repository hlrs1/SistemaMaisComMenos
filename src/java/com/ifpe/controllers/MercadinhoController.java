package com.ifpe.controllers;

import com.ifpe.model.dao.ManagerDao;
import com.ifpe.model.negocio.LoteProduto;
import com.ifpe.model.negocio.Mercadinho;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class MercadinhoController {

    private Mercadinho mercadinhoCadastro;
    private Mercadinho selection = new Mercadinho();
    private Mercadinho mercadinhoLogado;
    private String modalType;

    @PostConstruct
    public void init() {
        this.mercadinhoCadastro = new Mercadinho();
        this.modalType = "create";
    }

    public Mercadinho getMercadinhoLogado() {
        mercadinhoLogado = (Mercadinho) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("mercadinhoLogado");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mercadinhoLogado", mercadinhoLogado);
        return mercadinhoLogado;
    }

    public void inserir(String confirma) {
        FacesContext context = FacesContext.getCurrentInstance();
        String cnpjRegex = "\\d{14}";
        String contatoRegex = "\\d{10,11}";

        boolean valid = true;

        if (!mercadinhoCadastro.getSenha().equals(confirma)) {
            context.addMessage("formCadMercadinho:senha", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Senhas não conferem", "As senhas digitadas são diferentes."));
            valid = false;
        }

        if (!Pattern.matches(cnpjRegex, mercadinhoCadastro.getCnpj())) {
            context.addMessage("formCadMercadinho:cnpj", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "CNPJ inválido", "O CNPJ deve conter 14 dígitos."));
            valid = false;
        }

        if (!Pattern.matches(contatoRegex, mercadinhoCadastro.getContato())) {
            context.addMessage("formCadMercadinho:contato", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Contato inválido", "O contato deve conter 10 ou 11 dígitos."));
            valid = false;
        }

        if (!valid) {
            return;
        }

        // Verificar duplicidade de Login
        if (!ManagerDao.getCurrentInstance()
                .readAll("select m from Mercadinho m where m.login='" + mercadinhoCadastro.getLogin() + "'", Mercadinho.class)
                .isEmpty()) {
            context.addMessage("formCadMercadinho:login", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login já existe", "Tente outro login."));
            return;
        }

        // Verificar duplicidade de CNPJ
        if (!ManagerDao.getCurrentInstance()
                .readAll("select m from Mercadinho m where m.cnpj='" + mercadinhoCadastro.getCnpj() + "'", Mercadinho.class)
                .isEmpty()) {
            context.addMessage("formCadMercadinho:cnpj", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "CNPJ já existe", "Tente outro CNPJ."));
            return;
        }

        // Inserir o mercadinho
        ManagerDao.getCurrentInstance().insert(this.mercadinhoCadastro);
        this.mercadinhoCadastro = new Mercadinho();

        context.addMessage(null, new FacesMessage("Sucesso", "Mercadinho cadastrado com sucesso!"));
    }

    public void alterar() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            ManagerDao.getCurrentInstance().update(selection);
            context.addMessage(null, new FacesMessage("Sucesso", "Mercadinho atualizado com sucesso!"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao atualizar mercadinho."));
        }
    }

    public void deletar() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            ManagerDao.getCurrentInstance().delete(selection);
            selection = new Mercadinho();
            context.addMessage(null, new FacesMessage("Sucesso", "Mercadinho deletado com sucesso!"));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao deletar mercadinho."));
        }
    }

    public List<Mercadinho> readMercadinhos() {
        return ManagerDao.getCurrentInstance().readAll("select m from Mercadinho m", Mercadinho.class);
    }
    
    public Mercadinho buscarMercadinho(int p) {
        Mercadinho mercadinho = (Mercadinho) ManagerDao.getCurrentInstance().readById(p, Mercadinho.class);
        return mercadinho;
    }

    // Getters e Setters
    public Mercadinho getMercadinhoCadastro() {
        return mercadinhoCadastro;
    }

    public void setMercadinhoCadastro(Mercadinho mercadinhoCadastro) {
        this.mercadinhoCadastro = mercadinhoCadastro;
    }

    public Mercadinho getSelection() {
        return selection;
    }

    public void setSelection(Mercadinho selection) {
        this.selection = selection;
    }

    public String getModalType() {
        return modalType;
    }

    public void setModalType(String modalType) {
        this.modalType = modalType;
    }
    
    public void alterarSenha(String senha, String novaSenha, String confirma) {

        //código para recuperar qualquer atributo na sessão
        Mercadinho mLogado = selection;

        if (!mLogado.getSenha().equals(senha)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A senha digitada está incorreta. "
                            + "Por favor, tente novamente"));
            return;
        }

        if (!novaSenha.equals(confirma)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("A nova senha não bate com a confirmação. "
                            + "Por favor, tente novamente"));
            return;
        }

        mLogado.setSenha(novaSenha);

        ManagerDao.getCurrentInstance().update(mLogado);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Senha alterada com sucesso!"));
    }
}
