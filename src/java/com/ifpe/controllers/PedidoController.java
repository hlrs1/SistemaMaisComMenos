package com.ifpe.controllers;

import com.ifpe.model.dao.ManagerDao;
import com.ifpe.model.negocio.LoteProduto;
import com.ifpe.model.negocio.Mercadinho;
import com.ifpe.model.negocio.ONG;
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
import org.primefaces.push.annotation.Singleton;

@ManagedBean (name = "pedidoController")
@SessionScoped
@Singleton
public class PedidoController implements Serializable {

    private static PedidoController myself = null;
    public static PedidoController getCurrentInstance(){
        if(myself == null)
            myself = new PedidoController();
        
        return myself;
    }
    private LoteProduto produtoEscolhido = null;
    private Mercadinho mercadinho = null;
    private ONG ong = null;
    
    private int idMercadinho;

    public LoteProduto getProdutoEscolhido() {
        return produtoEscolhido;
    }
    
    public void setProdutoEscolhido(LoteProduto produtoEscolhido) {
        this.produtoEscolhido = produtoEscolhido;
    }
    
    public Mercadinho getMercadinho() {
        return mercadinho;
    }
    
    public void setMercadinho(Mercadinho mercadinho) {
        this.mercadinho = mercadinho;
    }
    
    public ONG getOng() {
        return ong;
    }

    public void setOng(ONG ong) {
        this.ong = ong;
    }

    public static PedidoController getMyself() {
        return myself;
    }

    public static void setMyself(PedidoController myself) {
        PedidoController.myself = myself;
    }

    public int getIdMercadinho() {
        return idMercadinho;
    }

    public void setIdMercadinho(LoteProduto produto) {
        this.idMercadinho = produto.getMercadinho().getId();
    }
    
    
    
    public void fazerPedido(ONG o, LoteProduto l ){
        setOng(o);
        setProdutoEscolhido(l);
        l.setOngInteressada(o);
        ManagerDao.getCurrentInstance().update(l);
        setIdMercadinho(l);
    }
    
    

    
    
    public void setProdutoSelecionado(LoteProduto p){
        this.produtoEscolhido = p;
    }


}
