package com.ifpe.controllers;

import com.ifpe.model.dao.ManagerDao;
import com.ifpe.model.negocio.LoteProduto;
import com.ifpe.model.negocio.Mercadinho;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class ProdutoController implements Serializable {

    private LoteProduto loteProdutoCadastro;
    private LoteProduto loteProdutoSelecionado;
    private List<LoteProduto> lotesProdutos;
    private List<String> produtosPredefinidos;
    
    private String validadeString; // Usado para receber a validade como string
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String filtroProduto = "";
    
    PedidoController pedidoCOntroller = PedidoController.getCurrentInstance();

    @PostConstruct
    public void init() {
        this.loteProdutoCadastro = new LoteProduto();
        this.loteProdutoSelecionado = new LoteProduto();
        this.validadeString = "";
        produtosPredefinidos = Arrays.asList("Feijão", "Macarrão", "Arroz", "Carne vermelha", "Frango", "Peixe", "Leite em pó", "Frutas", "Verduras", "Molho", "Temperos", "Doces");
    }

    public List<String> getProdutosPredefinidos() {
        return produtosPredefinidos;
    }

    private Mercadinho getMercadinhoLogado() {

        Mercadinho mercadinho = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(true)).getAttribute("loginController")).getMercadinhoLogado();

        if (mercadinho == null) {
            throw new IllegalStateException("Nenhum mercadinho logado encontrado na sessão.");
        }
        return mercadinho;
    }

    public List<LoteProduto> carregarLotesProdutos() {
        Mercadinho mercadinho = ((LoginController) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(true)).getAttribute("loginController")).getMercadinhoLogado();

        if (mercadinho == null) {
            throw new IllegalStateException("Nenhum mercadinho logado encontrado na sessão.");
        }
        if (mercadinho == null) {
            return Collections.emptyList();
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", mercadinho.getId());

        return ManagerDao.getCurrentInstance()
                .readWithParameters("select l from LoteProduto l where l.mercadinho.id = :id", LoteProduto.class, parameters);
    }
    
    public List<LoteProduto> readAll() {
        return ManagerDao.getCurrentInstance()
                .readAll("select l from LoteProduto l", LoteProduto.class);
    }
    
    public List<LoteProduto> readAllDisponiveis() {
        return ManagerDao.getCurrentInstance()
                .readAll("select l from LoteProduto l where l.ongInteressada=null", LoteProduto.class);
    }
    
    

    public void inserir() {
        try {
            // Convertendo a string de validade para Date antes de inserir no banco
            if (!validadeString.isEmpty()) {
                Date validade = dateFormat.parse(validadeString);
                loteProdutoCadastro.setValidade(new java.sql.Date(validade.getTime()));

                // Calculando dias restantes
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate validadeLocal = LocalDate.parse(validadeString, formatter);
                LocalDate hoje = LocalDate.now();
                int diasRestantes = (int) ChronoUnit.DAYS.between(hoje, validadeLocal);
            }

            loteProdutoCadastro.setMercadinho(getMercadinhoLogado());
            loteProdutoCadastro.setStatus("pendente");
            ManagerDao.getCurrentInstance().insert(loteProdutoCadastro);
            lotesProdutos = carregarLotesProdutos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto cadastrado com sucesso!"));
        } catch (ParseException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data de validade inválida."));
            e.printStackTrace(); // Para depuração
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao cadastrar produto."));
            e.printStackTrace(); // Para depuração
        }
    }


    public void alterar() {
        try {
            // Convertendo a validade string para Date antes de salvar as alterações
            if (!validadeString.isEmpty()) {
                Date validade = dateFormat.parse(validadeString);
                loteProdutoSelecionado.setValidade(new java.sql.Date(validade.getTime()));

                // Calculando dias restantes
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate validadeLocal = LocalDate.parse(validadeString, formatter);
                LocalDate hoje = LocalDate.now();
                int diasRestantes = (int) ChronoUnit.DAYS.between(hoje, validadeLocal);
            }

            ManagerDao.getCurrentInstance().update(loteProdutoSelecionado);
            lotesProdutos = carregarLotesProdutos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto atualizado com sucesso!"));
        } catch (ParseException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Data de validade inválida."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao atualizar produto."));
        }
    }
    
    public void alterarStatus() {
        try {
            loteProdutoSelecionado.setStatus("finalizado");
            ManagerDao.getCurrentInstance().update(loteProdutoSelecionado);
            lotesProdutos = carregarLotesProdutos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Transação finalizada com sucesso!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao alterar status do produto."));
        }
    }

    public void deletar() {
        try {
            ManagerDao.getCurrentInstance().delete(loteProdutoSelecionado);
            loteProdutoSelecionado = new LoteProduto();
            lotesProdutos = carregarLotesProdutos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto deletado com sucesso!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao deletar produto."));
        }
    }

    public List<LoteProduto> readProdutoByMercadinho(LoginController loginController) {

        lotesProdutos = ManagerDao.getCurrentInstance().readProdutosByMercadinho("select p from LoteProduto p where p.mercadinho=" + loginController.getMercadinhoLogado().getId(), LoteProduto.class);

        return lotesProdutos;

    }
    
    public List<LoteProduto> readFiltroProduto(String produto){
        return ManagerDao.getCurrentInstance().readAll("select p from LoteProduto p where p.produto='"+produto+"' and p.ongInteressada=null", LoteProduto.class);
    }
    
    public List<LoteProduto> buscarLoteManager(){
        if(filtroProduto.equals("")){
            return readAllDisponiveis();
        }else{
            List<LoteProduto> lotes = readFiltroProduto(filtroProduto);
            
            for(int i=0;i<lotes.size();i++){
                if(lotes.get(i).getOngInteressada() != null){
                    lotes.remove(i);
                }
            }
            return lotes;
        }
    }


    public void setLoteProdutoSelecionado(LoteProduto loteProdutoSelecionado) {
        this.loteProdutoSelecionado = loteProdutoSelecionado;
        if (loteProdutoSelecionado != null) {
            // Convertendo a validade para string formatada
            this.validadeString = dateFormat.format(loteProdutoSelecionado.getValidade());

            // Atualizando a string de validade para calcular dias restantes
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate validadeLocal = LocalDate.parse(this.validadeString, formatter);
            LocalDate hoje = LocalDate.now();
            int diasRestantes = (int) ChronoUnit.DAYS.between(hoje, validadeLocal);
        }
    }

    public LoteProduto setarLote(LoteProduto codigo) {
        LoteProduto p = new LoteProduto();
        List<LoteProduto> lotes = carregarLotesProdutos();
        for (LoteProduto pAux : lotes) {
            if (pAux.getId() == codigo.getId()) {
                p = pAux;
                this.loteProdutoSelecionado = p;
                // Convertendo a validade para string formatada
                this.validadeString = dateFormat.format(loteProdutoSelecionado.getValidade());
            }
        }

        System.out.println("setou lote atual " + loteProdutoSelecionado.getProduto());
        return p;
    }

    // Método para calcular dias restantes
    public int getDiasParaVencer() {
        if (validadeString == null || validadeString.isEmpty()) {
            return 0;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate validade = LocalDate.parse(validadeString, formatter);
            LocalDate hoje = LocalDate.now();
            return (int) ChronoUnit.DAYS.between(hoje, validade);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public void inserirOng(){
        loteProdutoSelecionado = ManagerDao.getCurrentInstance().readById(pedidoCOntroller.getProdutoEscolhido().getId(), LoteProduto.class);
        loteProdutoSelecionado.setOngInteressada(pedidoCOntroller.getOng());
        
        alterar();
    }
    

    // Getters e Setters
    public LoteProduto getLoteProdutoCadastro() {
        return loteProdutoCadastro;
    }

    public void setLoteProdutoCadastro(LoteProduto loteProdutoCadastro) {
        this.loteProdutoCadastro = loteProdutoCadastro;
    }

    public LoteProduto getLoteProdutoSelecionado() {
        return loteProdutoSelecionado;
    }

    public List<LoteProduto> getLotesProdutos() {
        return lotesProdutos;
    }

    public void setLotesProdutos(List<LoteProduto> lotesProdutos) {
        this.lotesProdutos = lotesProdutos;
    }

    public String getValidadeString() {
        return validadeString;
    }

    public void setValidadeString(String validadeString) {
        this.validadeString = validadeString;
    }
    
    public void setFiltroCategoria(){
        this.filtroProduto = loteProdutoCadastro.getProduto();
    }
    
    public String getFiltroProduto(){
        return this.loteProdutoCadastro.getProduto();
    }
    
}
