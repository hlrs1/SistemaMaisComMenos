package com.ifpe.model.negocio;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "produto")
public class LoteProduto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_mercadinho", nullable = false)
    private Mercadinho mercadinho;

    @Column(name = "produto")
    private String produto;

    @Column(name = "marca")
    private String marca;

    @Column(name = "lote")
    private String lote;

    @Column(name = "quantidade")
    private String quantidade;

    @Column(name = "preco_inicial")
    private Double precoInicial;

    @Column(name = "validade")
    private Date validade;

    @Column(name = "preco_final")
    private Double precoFinal;
    
    @Column (name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "ong_interessada") // Verifique se o nome da coluna está correto
    private ONG ongInteressada;

    // Método para calcular dias restantes
    public int diasParaVencer() {
        try {
            LocalDate validade = this.validade.toLocalDate();
            LocalDate hoje = LocalDate.now();
            return (int) ChronoUnit.DAYS.between(hoje, validade);
        } catch (Exception e) {
            return 0;
        }
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Mercadinho getMercadinho() {
        return mercadinho;
    }

    public void setMercadinho(Mercadinho mercadinho) {
        this.mercadinho = mercadinho;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Double getPrecoInicial() {
        return precoInicial;
    }

    public void setPrecoInicial(Double precoInicial) {
        this.precoInicial = precoInicial;
    }

    public String getPrecoInicialFormatado() {
        return String.format("R$%.2f", this.precoInicial);
    }

    public Date getValidade() {
        return validade;
    }

    public String getValidadeFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        return sdf.format(this.validade);
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public Double getPrecoFinal() {
        return precoFinal;
    }

    public String getPrecoFinalFormatado() {
        return String.format("R$%.2f", this.precoFinal);
    }

    public void setPrecoFinal(Double precoFinal) {
        this.precoFinal = precoFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    public ONG getOngInteressada() {
        return ongInteressada;
    }

    public String getProduto() {
        return produto;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public void setOngInteressada(ONG ongInteressada) {
        this.ongInteressada = ongInteressada;
    }
}
