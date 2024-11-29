package com.ifpe.model.negocio;

import com.ifpe.model.negocio.Mercadinho;
import com.ifpe.model.negocio.ONG;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-11-29T16:19:14")
@StaticMetamodel(LoteProduto.class)
public class LoteProduto_ { 

    public static volatile SingularAttribute<LoteProduto, String> marca;
    public static volatile SingularAttribute<LoteProduto, Double> precoInicial;
    public static volatile SingularAttribute<LoteProduto, Mercadinho> mercadinho;
    public static volatile SingularAttribute<LoteProduto, String> produto;
    public static volatile SingularAttribute<LoteProduto, ONG> ongInteressada;
    public static volatile SingularAttribute<LoteProduto, String> lote;
    public static volatile SingularAttribute<LoteProduto, Double> precoFinal;
    public static volatile SingularAttribute<LoteProduto, Integer> id;
    public static volatile SingularAttribute<LoteProduto, String> quantidade;
    public static volatile SingularAttribute<LoteProduto, Date> validade;
    public static volatile SingularAttribute<LoteProduto, String> status;

}