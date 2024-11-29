package com.ifpe.model.negocio;

import com.ifpe.model.negocio.LoteProduto;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-11-29T16:19:14")
@StaticMetamodel(Mercadinho.class)
public class Mercadinho_ { 

    public static volatile SingularAttribute<Mercadinho, String> senha;
    public static volatile SingularAttribute<Mercadinho, String> endereco;
    public static volatile ListAttribute<Mercadinho, LoteProduto> lotes;
    public static volatile SingularAttribute<Mercadinho, String> nome;
    public static volatile SingularAttribute<Mercadinho, Integer> id;
    public static volatile SingularAttribute<Mercadinho, String> cnpj;
    public static volatile SingularAttribute<Mercadinho, String> login;
    public static volatile SingularAttribute<Mercadinho, String> contato;

}