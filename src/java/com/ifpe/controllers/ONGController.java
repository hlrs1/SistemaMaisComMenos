package com.ifpe.controllers;

import com.ifpe.model.dao.ManagerDao;
import com.ifpe.model.negocio.LoteProduto;
import com.ifpe.model.negocio.ONG;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "ongController")  // Certifique-se de que o nome está correto
@SessionScoped
public class ONGController {

    private ONG ongCadastro;
    private ONG selection = new ONG();
    private ONG ongLogado = new ONG();
    private String modalType;
    private int idMercadinho;

    @PostConstruct
    public void init() {
        this.ongCadastro = new ONG();
        this.modalType = "create";
    }

    public ONG getOngLogada() {
        ongLogado = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getAttribute("loginController")).getOngLogada();
        return this.ongLogado;
    }

    public void inserir(String confirma) {
        
        FacesContext context = FacesContext.getCurrentInstance();
        String contatoRegex = "\\d{10,11}";
        
        boolean valid = true;
        
        
        List<ONG> u = ManagerDao.getCurrentInstance()
                .readAll("select o from ONG o where o.login='" + ongCadastro.getLogin() + "'", ONG.class);
        
        if (!Pattern.matches(contatoRegex, ongCadastro.getContato())) {
            context.addMessage("formCadOng:contato", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Contato inválido", "O contato deve conter 10 ou 11 dígitos."));
            valid = false;
        }

        if (u.isEmpty()) {
            if (!this.ongCadastro.getSenha().equals("") && !confirma.equals("")) {
                if (!this.ongCadastro.getSenha().equals(confirma)) {
                    FacesContext.getCurrentInstance().addMessage("formCadONG:pswSenha",
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro Severo", "A senha e a confirmação não batem!"));
                    return;
                }

                ManagerDao.getCurrentInstance().insert(this.ongCadastro);
                this.ongCadastro = new ONG();

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("ONG cadastrada com sucesso!"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "A senha não pode ficar em branco!", "Crie uma senha válida."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Login de ONG já existe!", "Tente outro login"));
        }
    }

    public List<ONG> readONGs() {
        return ManagerDao.getCurrentInstance().readAll("select o from ONG o", ONG.class);
    }

    public ONG getOngCadastro() {
        return ongCadastro;
    }

    public void setOngCadastro(ONG ongCadastro) {
        this.ongCadastro = ongCadastro;
    }

    public ONG getSelection() {
        return selection;
    }

    public void setSelection(ONG selection) {
        this.selection = selection;
    }

    public void alterar() {
        ManagerDao.getCurrentInstance().update(this.selection);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "ONG atualizada com sucesso"));

    }

    public void deletar() {
        ManagerDao.getCurrentInstance().delete(this.selection);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Sucesso!", "ONG deletada"));
    }
    
    public void alterarSenha(String senha, String novaSenha, String confirma) {

        //código para recuperar qualquer atributo na sessão
        ONG oLogado = selection;

        if (!oLogado.getSenha().equals(senha)) {
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

        oLogado.setSenha(novaSenha);

        ManagerDao.getCurrentInstance().update(oLogado);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Senha alterada com sucesso!"));
    }
    
    public int getIdMercadinho(){
        return idMercadinho;
    }
    
    public void setIdMercadinho(LoteProduto p){
        idMercadinho = p.getMercadinho().getId();
    }
}
